package pt.ulisboa.tecnico.cmov.shopist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;

public class CreatePantryDialogFragment extends DialogFragment {
    private final Context mContext;
    private View mDialogView;

    public CreatePantryDialogFragment(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_create_list, null);

        Spinner spinnerCategory = mDialogView.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.available_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        Spinner spinnerLocation = mDialogView.findViewById(R.id.spinner_location);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.location_options, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Button btn = mDialogView.findViewById(R.id.button_pickLocation);
                if (position == 2 && btn.getVisibility() != View.VISIBLE)
                    btn.setVisibility(View.VISIBLE);
                else if (btn.getVisibility() != View.GONE)
                    btn.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(R.string.create_list_dialog_title)
                .setView(mDialogView)
                .setPositiveButton(R.string.create_ok, null)
                .setNegativeButton(R.string.cancel, (dialog, id) -> Objects
                        .requireNonNull(CreatePantryDialogFragment.this.getDialog()).cancel());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null) {
            EditText inputTitle = mDialogView.findViewById(R.id.editText_listName);
            EditText inputDesc = mDialogView.findViewById(R.id.editText_listDescription);
            Spinner spinnerCat = mDialogView.findViewById(R.id.spinner_category);
            Spinner spinnerLoc = mDialogView.findViewById(R.id.spinner_location);
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener( v -> {
                String listTitle = inputTitle.getText().toString();
                String listDesc = inputDesc.getText().toString();
                Location listLoc = getLocationOption(spinnerLoc);

                if (listTitle.trim().isEmpty() ||
                        spinnerCat.getSelectedItemPosition() == 0) {
                    Toast.makeText(mContext, R.string.create_list_error, Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (listDesc.trim().isEmpty()) {
                        listDesc = null;
                    } if (spinnerLoc.getSelectedItemPosition() == 0) {
                        listLoc = null;
                    } if (spinnerCat.getSelectedItemPosition() == 1) {
                        ((MainActivity) mContext).getViewModel().addPantry(new Pantry(listTitle));
                    } else {
//                        mContextData.addShoppingList(listTitle, listDesc, listLoc);
                    }
                    RecyclerView rv = getActivity().findViewById(R.id.recyclerView);
                    rv.getAdapter().notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        }
    }

    private Location getLocationOption(Spinner spinnerLoc) {
        int option = spinnerLoc.getSelectedItemPosition();
        if (option == 0 || option == 3)
            return null;
        else if (option == 1)
            return null; // return curr location
        else { // TODO pick location, open map and pick location
            return null;
        }
    }
}