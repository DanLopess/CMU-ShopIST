package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.ScanCodeActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.ProductImage;

import static android.app.Activity.RESULT_OK;

public class ProductDetailsDialog extends DialogFragment {

    private static final int SCAN_REQ_CODE = 1;

    private final Context mContext;
    private View mDialogView;
    private final Product product;
    private byte[] image;

    public ProductDetailsDialog(Context context, Product product, ProductImage productImage) {
        this.mContext = context;
        this.product = product;
        this.image = productImage.getImage();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_FullScreen);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_product_details, null);

        alertDialogBuilder.setTitle(R.string.edit_product)
                .setView(mDialogView)
                .setPositiveButton(R.string.update, (dialog, id) -> {
                    setProductChanges();
                    ((MainActivity) mContext).getViewModel().updateProduct(product);
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
        ImageView image = mDialogView.findViewById(R.id.product_details_image);
        Button scan = mDialogView.findViewById(R.id.scan_code_bt);
        name.setText(product.getProductName());
        desc.setText(product.getProductDescription());
        code.setText(product.getCode());
        if(image == null) {
        }
        scan.setOnClickListener(v -> scanProduct());
//            image.setImageBitmap(BitmapFactory.decodeByteArray(productAndPrincipalImage.productImage.getImage(), 0, productAndPrincipalImage.productImage.getImage().length));
    }

    public void scanProduct() {
        Intent intent = new Intent(mContext, ScanCodeActivity.class);
        startActivityForResult(intent, SCAN_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQ_CODE && resultCode == RESULT_OK) {
            assert data != null;
            String code = data.getStringExtra("code");
            product.setCode(code);
            TextView codeText = mDialogView.findViewById(R.id.product_details_code);
            codeText.setText(product.getCode());
        }
    }
}
