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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import pt.ulisboa.tecnico.cmov.shopist.PantryActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.ProductImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;

public class PantryProductDetailsDialog extends DialogFragment {

    private final Context mContext;
    private View mDialogView;
    private PantryProduct pantryProduct;
    private byte[] image;

    public PantryProductDetailsDialog(Context context, PantryProduct pantryProduct, ProductImage productImage) {
        this.mContext = context;
        this.pantryProduct = pantryProduct;
        this.image = productImage.getImage();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_FullScreen);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_pantry_product_details, null);

        alertDialogBuilder.setTitle(R.string.product_details)
                .setView(mDialogView)
                .setPositiveButton(R.string.update, (dialog, id) -> {
                    setPantryProductChanges();
                    ((PantryActivity) mContext).getViewModel().updatePantryProduct(pantryProduct);
                })
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(PantryProductDetailsDialog.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }

    private void setPantryProductChanges() {
        NumberPicker availablePicker = mDialogView.findViewById(R.id.product_details_available);
        NumberPicker neededPicker = mDialogView.findViewById(R.id.product_details_needed);

        pantryProduct.setQttAvailable(availablePicker.getValue());
        pantryProduct.setQttNeeded(neededPicker.getValue());
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        if(dialog != null) {
            setupDialogValues();
        } else {
            // TODO: DIALOG FAILED TO LOAD
        }
    }

    private void setupDialogValues() {
        TextView name = mDialogView.findViewById(R.id.product_details_name);
        TextView desc = mDialogView.findViewById(R.id.product_details_desc);
        ImageView image = mDialogView.findViewById(R.id.product_details_image);
        NumberPicker availablePicker = mDialogView.findViewById(R.id.product_details_available);
        NumberPicker neededPicker = mDialogView.findViewById(R.id.product_details_needed);
        availablePicker.setMinValue(0);
        neededPicker.setMinValue(0);
        availablePicker.setMaxValue(99);
        neededPicker.setMaxValue(99);
        name.setText(pantryProduct.getProduct().productName);
        desc.setText(pantryProduct.getProduct().productDescription);
        availablePicker.setValue(pantryProduct.getQttAvailable());
        neededPicker.setValue(pantryProduct.getQttNeeded());
        if(image == null) {
        }
//            image.setImageBitmap(BitmapFactory.decodeByteArray(productAndPrincipalImage.productImage.getImage(), 0, productAndPrincipalImage.productImage.getImage().length));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
