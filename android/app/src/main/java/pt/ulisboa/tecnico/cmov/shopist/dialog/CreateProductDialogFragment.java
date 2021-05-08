package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import pt.ulisboa.tecnico.cmov.shopist.AddPantryProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.AddStoreProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CAMERA_SERVICE;

public class CreateProductDialogFragment extends DialogFragment {

    public static final int PICK_IMAGE = 1;
    public static final int PANTRY = 2;
    public static final int STORE = 3;
    public static final int PRODUCT = 4;

    private final Context mContext;
    private ImageView imageView;
    private View mDialogView;
    private String code = null;
    private Bitmap imageSelected;
    private final int flag; //used to not need to make 2 separate dialogs

    public CreateProductDialogFragment(Context context, int flag) {
        this.mContext = context;
        this.flag = flag;
    }
    public CreateProductDialogFragment(Context context, String code, int flag) {
        this.mContext = context;
        this.code = code;
        this.flag = flag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_product_creation, null);

         ImageView image = mDialogView.findViewById(R.id.create_product_image);

         image.setOnClickListener(v -> selectImage(mContext));

        alertDialogBuilder.setTitle(R.string.new_product)
                .setView(mDialogView)
                .setPositiveButton(R.string.create_ok, null)
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(CreateProductDialogFragment.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }

    private void onClickCreateProduct(View dialogView) {
        EditText editText = dialogView.findViewById(R.id.product_name);
        String name = editText.getText().toString();

        // TODO fix images
        /*if(imageView != null) {
            data.addProduct(new Product(name, ((BitmapDrawable) imageView.getDrawable()).getBitmap()));
        } else {
            data.addProduct(new Product(name, null));
        }*/
    }
//        data.addProduct(new Product(null, name, null, null));

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null) {
            EditText inputName = mDialogView.findViewById(R.id.product_name);
            EditText inputDesc = mDialogView.findViewById(R.id.product_description_edit);
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener( v -> {
                String prodName = inputName.getText().toString();
                String prodDesc = inputDesc.getText().toString();

                if (prodName.trim().isEmpty()) {
                    Toast.makeText(mContext, R.string.create_product_error, Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (flag == PANTRY) {
                        if (code == null)
                            ((AddPantryProductsActivity) mContext).getViewModel().addProduct(prodName, prodDesc, imageSelected);
                        else
                            ((PantryActivity) mContext).getViewModel().addProduct(prodName, prodDesc, imageSelected, code);
                    } else if (flag == STORE) {
                        if (code == null)
                            ((AddStoreProductsActivity) mContext).getViewModel().addProduct(prodName, prodDesc, imageSelected);
                        else
                            ((StoreActivity) mContext).getViewModel().addProduct(prodName, prodDesc, imageSelected, code);
                    } else if (flag == PRODUCT) {
                        if (code == null)
                            ((MainActivity) mContext).getViewModel().addProduct(prodName, prodDesc, imageSelected);
                        else
                            ((MainActivity) mContext).getViewModel().addProduct(prodName, prodDesc, imageSelected, code);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = mDialogView.findViewById(R.id.create_product_image);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            onSelectFromGalleryResult(data);
        } else if (resultCode == RESULT_OK && requestCode == 0) {
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            imageSelected = selectedImage;
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
            imageSelected = selectedImage;
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
                imageSelected = null;
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
