package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;

public class StoreProductDetailsDialog extends DialogFragment {

    private final Context mContext;
    private View mDialogView;
    private final StoreProduct storeProduct;

    public StoreProductDetailsDialog(Context context, StoreProduct storeProduct) {
        this.mContext = context;
        this.storeProduct = storeProduct;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_FullScreen);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_store_product_details, null);


        alertDialogBuilder.setTitle(R.string.product_details)
                .setView(mDialogView)
                .setPositiveButton(R.string.update, (dialog, id) -> {
                    setStoreProductChanges();
                    ((StoreActivity) mContext).getViewModel().updateStoreProduct(storeProduct);
                })
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(StoreProductDetailsDialog.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }

    private void setStoreProductChanges() {
        NumberPicker cartPicker = mDialogView.findViewById(R.id.store_product_details_cart);
        storeProduct.setQttCart(cartPicker.getValue());

        EditText price = mDialogView.findViewById(R.id.store_product_details_price);
        storeProduct.setPrice(Double.parseDouble(price.getText().toString()));
        if (storeProduct.getPrice() != 0.0f) {
            ((StoreActivity) mContext).getViewModel()
                    .postProductPrice(storeProduct.getProduct().getCode(),storeProduct.getPrice());
        }
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

        setupDialogValues();
    }

    @SuppressLint("SetTextI18n")
    private void setupDialogValues() {
        TextView name = mDialogView.findViewById(R.id.store_product_details_name);
        TextView desc = mDialogView.findViewById(R.id.store_product_details_desc);
        ImageView image = mDialogView.findViewById(R.id.store_product_details_image);
        EditText price = mDialogView.findViewById(R.id.store_product_details_price);
        TextView needed = mDialogView.findViewById(R.id.store_product_details_needed);
        NumberPicker cartPicker = mDialogView.findViewById(R.id.store_product_details_cart);
        cartPicker.setMinValue(0);
        cartPicker.setMaxValue(99);
        name.setText(storeProduct.getProduct().getProductName());
        desc.setText(storeProduct.getProduct().getProductDescription());
        needed.setText(storeProduct.getQttNeeded().toString());
        cartPicker.setValue(storeProduct.getQttCart());
        price.setText(storeProduct.getPrice().toString());
        if(storeProduct.getProduct().getImagePath() != null) {
            ((StoreActivity) mContext).getViewModel().getProductImage(storeProduct.getProduct().getImagePath()).
                    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res ->
                    image.setImageBitmap(Bitmap.createScaledBitmap(res, res.getWidth(), res.getHeight(), false)));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
