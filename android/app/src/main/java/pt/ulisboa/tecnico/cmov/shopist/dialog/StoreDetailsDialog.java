package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.MapsActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

import static android.app.Activity.RESULT_OK;
import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_DATA;
import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_EXTRA;

public class StoreDetailsDialog extends DialogFragment {

    private final Context mContext;
    private View mDialogView;
    private final Store store;

    public StoreDetailsDialog(Context context, Store store) {
        this.mContext = context;
        this.store = store;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_FullScreen);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_pantry_store_details, null);

        alertDialogBuilder.setTitle(R.string.edit_list)
                .setView(mDialogView)
                .setPositiveButton(R.string.update, (dialog, id) -> {
                    setStoreChanges();
                    ((MainActivity) mContext).getViewModel().updateStore(store);
                })
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(StoreDetailsDialog.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }

    private void setStoreChanges() {
        EditText storeName = mDialogView.findViewById(R.id.pantry_store_details_name);
        EditText storeDesc = mDialogView.findViewById(R.id.pantry_store_details_desc);

        if (!storeName.getText().toString().isEmpty())
            store.setName(storeName.getText().toString());
        store.setDescription(storeDesc.getText().toString());
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
        EditText name = mDialogView.findViewById(R.id.pantry_store_details_name);
        EditText desc = mDialogView.findViewById(R.id.pantry_store_details_desc);
        Button changeLoc = mDialogView.findViewById(R.id.pantry_store_details_change_loc_bt);
        name.setText(store.getName());
        desc.setText(store.getDescription());

        changeLoc.setOnClickListener(v -> pickLocation());
    }

    private void pickLocation() {
        Activity act = getActivity();
        if (act != null) {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivityForResult(intent, LOCATION_DATA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == LOCATION_DATA && resultCode == RESULT_OK) {
            Location pickedLocation = Objects.requireNonNull(data.getExtras()).getParcelable(LOCATION_EXTRA);
            store.setLocationWrapper(new LocationWrapper(pickedLocation));
        }
    }
}
