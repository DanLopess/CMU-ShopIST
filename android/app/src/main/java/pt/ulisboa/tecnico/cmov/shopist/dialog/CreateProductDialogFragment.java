package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;

public class CreateProductDialogFragment extends DialogFragment {

    private final Context context;
    // private ImageView imageView;
    private View dialogView;

    public static final int PICK_IMAGE = 1;

    public CreateProductDialogFragment(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_product_creation, null);

        // Button insertImageButton = dialogView.findViewById(R.id.create_product_insert_image_button);

        // insertImageButton.setOnClickListener(v -> onClickChooseImage());

        Spinner spinner = dialogView.findViewById(R.id.product_categories);
        spinner.setPrompt("Categories");

        alertDialogBuilder.setTitle("Create New Product");
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton(R.string.create_ok, (dialog, id) -> {
            onClickCreateProduct(dialogView);
        });

        return alertDialogBuilder.create();
    }

    private void onClickCreateProduct(View dialogView) {
        EditText editText = dialogView.findViewById(R.id.product_name);
        String name = editText.getText().toString();
        AppContextData data = (AppContextData) context.getApplicationContext();
        // TODO fix images
        /*if(imageView != null) {
            data.addProduct(new Product(name, ((BitmapDrawable) imageView.getDrawable()).getBitmap()));
        } else {
            data.addProduct(new Product(name, null));
        }*/
        data.addProduct(new Product(null, name, null, null));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /* if (requestCode == PICK_IMAGE) {
            onSelectFromGalleryResult(data);
        } else {
            Toast.makeText(context, "You haven't picked Image",Toast.LENGTH_LONG).show();
        } */

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
