package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.ulisboa.tecnico.cmov.shopist.adapter.ListOfPantriesAdapter;
import pt.ulisboa.tecnico.cmov.shopist.dialog.CreatePantryDialogFragment;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class MainActivity extends AppCompatActivity {
    private DialogFragment mCreateListDialog;
    private String mCurrCategory;
    private RecyclerView rvLists;
    private ListOfPantriesAdapter mPantryAdapter;
    private ListOfPantriesAdapter mShoppingAdapter;
    public ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        //load local lists
        //if wifi available sync lists

        mCreateListDialog = new CreatePantryDialogFragment(this);
        setUpLists();
        setUpBottomNavigation();
    }

    public void createNewList(MenuItem item) {
        mCreateListDialog.show(getSupportFragmentManager(), "create list");
    }

    public void addListWithCode(MenuItem item) {
        //TODO
    }

    public void addListWithQR(MenuItem item) {
        //TODO
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    private void setUpLists() {
        rvLists = findViewById(R.id.recyclerView);
        mPantryAdapter = new ListOfPantriesAdapter(this, viewModel.getPantries());
        mShoppingAdapter = new ListOfPantriesAdapter(this, viewModel.getPantries());
    }

    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView titleTextView = findViewById(R.id.textView_title);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    mCurrCategory = item.getTitle().toString();
                    titleTextView.setText(mCurrCategory);
                    if (mCurrCategory.equals("Pantry")) {
                        rvLists.setAdapter(mPantryAdapter);
                        rvLists.setLayoutManager(layoutManager);
                    } else if (mCurrCategory.equals("Shopping")) {
                        rvLists.setAdapter(mShoppingAdapter);
                        rvLists.setLayoutManager(layoutManager);
                    }
                    return true;
                }
        );

        // Select initial list TODO based on location
        // If we can get the current location and associate it with  only one list, open that list by default
        //for now select Pantry list
        bottomNavigationView.setSelectedItemId(R.id.action_pantry_lists);
    }
}