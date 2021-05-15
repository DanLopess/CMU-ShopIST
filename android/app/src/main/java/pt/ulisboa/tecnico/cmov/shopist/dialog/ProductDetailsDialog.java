package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.ScanCodeActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

import static android.app.Activity.RESULT_OK;

public class ProductDetailsDialog extends DialogFragment {

    private static final int SCAN_REQ_CODE = 5;
    public static final int PICK_IMAGE = 1;

    private final Context mContext;
    private View mDialogView;
    private final Product product;
    private Bitmap image;
    private ImageView imageView;


    public ProductDetailsDialog(Context context, Product product) {
        this.mContext = context;
        this.product = product;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_FullScreen);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_product_details, null);

        imageView = mDialogView.findViewById(R.id.product_details_image);

        imageView.setOnClickListener(v -> selectImage(mContext));

        alertDialogBuilder.setTitle(R.string.edit_product)
                .setView(mDialogView)
                .setPositiveButton(R.string.update, (dialog, id) -> {
                    setProductChanges();
                    ((MainActivity) mContext).getViewModel().updateProduct(product, image);
                })
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(ProductDetailsDialog.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }

    private void setProductChanges() {
        EditText prodName = mDialogView.findViewById(R.id.product_details_name);
        EditText prodDesc = mDialogView.findViewById(R.id.product_details_desc);

        if (!prodName.getText().toString().isEmpty())
            product.setProductName(prodName.getText().toString());
        product.setProductDescription(prodDesc.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        assert dialog != null;
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        setupDialog();
    }

    private void setupDialog() {
        EditText name = mDialogView.findViewById(R.id.product_details_name);
        EditText desc = mDialogView.findViewById(R.id.product_details_desc);
        TextView code = mDialogView.findViewById(R.id.product_details_code);
        imageView = mDialogView.findViewById(R.id.product_details_image);
        Button scan = mDialogView.findViewById(R.id.scan_code_bt);
        name.setText(product.getProductName());
        desc.setText(product.getProductDescription());
        code.setText(product.getCode());

        if(product.getImagePath() != null) {
            ((MainActivity) mContext).getViewModel().getProductImage(product.getImagePath()).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                        image = res;
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(res, res.getWidth(), res.getHeight(), false));
            });
        }
        scan.setOnClickListener(v -> scanProduct());
//            image.setImageBitmap(BitmapFactory.decodeByteArray(productAndPrincipalImage.productImage.getImage(), 0, productAndPrincipalImage.productImage.getImage().length));
    }

    public void scanProduct() {
        Intent intent = new Intent(mContext, ScanCodeActivity.class);
        startActivityForResult(intent, SCAN_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = mDialogView.findViewById(R.id.product_details_image);

        if (requestCode == SCAN_REQ_CODE && resultCode == RESULT_OK) {
            assert data != null;
            String code = data.getStringExtra("code");
            product.setCode(code);
            TextView codeText = mDialogView.findViewById(R.id.product_details_code);
            codeText.setText(product.getCode());
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            onSelectFromGalleryResult(data);
        } else if (resultCode == RESULT_OK && requestCode == 0) {
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            image = selectedImage;
            imageView.setImageBitmap(selectedImage);
        } else {
            Toast.makeText(mContext, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image = selectedImage;
            imageView.setImageBitmap(selectedImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    void onClickChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Remove Photo", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your image");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals("Remove Photo")) {
                imageView.setImageResource(R.drawable.ic_baseline_image_24);
                image = null;
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
