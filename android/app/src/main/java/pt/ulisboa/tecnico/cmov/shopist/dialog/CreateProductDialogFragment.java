package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import pt.ulisboa.tecnico.cmov.shopist.AddPantryProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.AddStoreProductsActivity;
import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

public class CreateProductDialogFragment extends DialogFragment {

    public static final int PICK_IMAGE = 1;
    public static final int PANTRY = 2;
    public static final int STORE = 3;

    private final Context mContext;
    // private ImageView imageView;
    private View mDialogView;
    private String code = null;
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

        // TODO add images
        // Button insertImageButton = dialogView.findViewById(R.id.create_product_insert_image_button);

        // insertImageButton.setOnClickListener(v -> onClickChooseImage());

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
                            ((AddPantryProductsActivity) mContext).getViewModel().addProduct(prodName, prodDesc);
                        else
                            ((PantryActivity) mContext).getViewModel().addProduct(prodName, prodDesc, code);
                    } else if (flag == STORE) {
                        if (code == null)
                            ((AddStoreProductsActivity) mContext).getViewModel().addProduct(prodName, prodDesc);
                        else
                            ((StoreActivity) mContext).getViewModel().addProduct(prodName, prodDesc, code);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    /* private void onSelectFromGalleryResult(Intent data) {
       try {
           final Uri imageUri = data.getData();
           final InputStream imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUri);
           final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
           imageView = dialogView.findViewById(R.id.create_product_image);
           imageView.setImageBitmap(selectedImage);

       } catch (FileNotFoundException e) {
           e.printStackTrace();
           Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
       }

    }*/

    /* void onClickChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    } */
}
