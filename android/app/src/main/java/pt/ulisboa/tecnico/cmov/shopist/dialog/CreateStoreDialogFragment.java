package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.MapsActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

import static android.app.Activity.RESULT_OK;
import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_DATA;
import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_EXTRA;

public class CreateStoreDialogFragment extends DialogFragment {
    private final Context mContext;
    private View mDialogView;
    private Location pickedLocation;

    public CreateStoreDialogFragment(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_create_store, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(R.string.create_store_dialog_title)
                .setView(mDialogView)
                .setPositiveButton(R.string.create_ok, null)
                .setNegativeButton(R.string.cancel, (dialog, id) -> Objects
                        .requireNonNull(CreateStoreDialogFragment.this.getDialog()).cancel());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null) {
            EditText inputTitle = mDialogView.findViewById(R.id.editText_listName);
            EditText inputDesc = mDialogView.findViewById(R.id.editText_storeDescription);
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button pickLocationButton = mDialogView.findViewById(R.id.button_pickLocation);

            pickLocationButton.setOnClickListener(v->pickLocation());

            positiveButton.setOnClickListener( v -> {
                String listTitle = inputTitle.getText().toString();
                String listDesc = inputDesc.getText().toString();

                if (listTitle.trim().isEmpty()) {
                    Toast.makeText(mContext, R.string.create_list_error, Toast.LENGTH_LONG)
                            .show();
                } else {
                    ((MainActivity) mContext).getViewModel().addStore(listTitle, listDesc, new LocationWrapper(pickedLocation));
                    RecyclerView rv = Objects.requireNonNull(getActivity()).findViewById(R.id.recyclerView);
                    Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }
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
        }
    }
}