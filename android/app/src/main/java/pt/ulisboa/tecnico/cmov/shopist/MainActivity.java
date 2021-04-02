package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private String currCategory;
    private DialogFragment createListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load local lists
        //if wifi available sync lists

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView titleTextView = findViewById(R.id.textView_title);
        createListDialog = new CreateListDialogFragment(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    currCategory = item.getTitle().toString(); // To set the default for the list creation
                    titleTextView.setText(currCategory);
                    //update shown lists
                    return false;
                }
        );

        // Select initial list TODO based on location
        //for now select Pantry list
        bottomNavigationView.setSelectedItemId(R.id.action_pantry_lists);

    }

    // TODO call to the dialog
    // In order to have a pre-selected value displayed, you can call setText(CharSequence text, boolean filter) on the AutoCompleteTextView with the filter set to false.
    // Set Category default to current category
    // Set location default to use current location, other options are none or pick location on map
    public void createNewList(MenuItem item) {
        createListDialog.show(getSupportFragmentManager(), "create list");
    }

    public void addListWithCode(MenuItem item) {
        //TODO
    }

    public void addListWithQR(MenuItem item) {
        //TODO
    }
}