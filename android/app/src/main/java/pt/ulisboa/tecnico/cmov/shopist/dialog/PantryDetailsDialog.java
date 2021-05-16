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
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

import static android.app.Activity.RESULT_OK;
import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_DATA;
import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_EXTRA;

public class PantryDetailsDialog extends DialogFragment {

    private final Context mContext;
    private View mDialogView;
    private final Pantry pantry;
    private Location pickedLocation;

    public PantryDetailsDialog(Context context, Pantry pantry) {
        this.mContext = context;
        this.pantry = pantry;
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
                    setPantryChanges();
                    ((MainActivity) mContext).getViewModel().updatePantry(pantry);
                })
                .setNegativeButton(R.string.cancel,  (dialog, id) ->
                        Objects.requireNonNull(PantryDetailsDialog.this.getDialog()).cancel());
        return alertDialogBuilder.create();
    }

    private void setPantryChanges() {
        EditText pantryName = mDialogView.findViewById(R.id.pantry_store_details_name);
        EditText pantryDesc = mDialogView.findViewById(R.id.pantry_store_details_desc);

        if (!pantryName.getText().toString().isEmpty())
            pantry.setName(pantryName.getText().toString());
        pantry.setDescription(pantryDesc.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        if (dialog != null) {
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));

            setupDialog();
        }
    }

    private void setupDialog() {
        EditText name = mDialogView.findViewById(R.id.pantry_store_details_name);
        EditText desc = mDialogView.findViewById(R.id.pantry_store_details_desc);
        Button changeLoc = mDialogView.findViewById(R.id.pantry_store_details_change_loc_bt);
        name.setText(pantry.getName());
        desc.setText(pantry.getDescription());

        changeLoc.setOnClickListener(v -> {
            pickLocation();
        });
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
            pickedLocation = Objects.requireNonNull(data.getExtras()).getParcelable(LOCATION_EXTRA);
            pantry.setLocationWrapper(new LocationWrapper(pickedLocation));
        }
    }
}
