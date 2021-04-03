package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DialogFragment mCreateListDialog;
    private String mCurrCategory;
    private RecyclerView rvLists;
    private ListsAdapter mPantryAdapter;
    private ListsAdapter mShoppingAdapter;

    // for testing, will probably stay in application context
    private List<ProductList> pantryLists;
    private List<ProductList> shoppingLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load local lists
        //if wifi available sync lists

        pantryLists = new ArrayList<>();
        shoppingLists = new ArrayList<>();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView titleTextView = findViewById(R.id.textView_title);
        mCreateListDialog = new CreateListDialogFragment(this);
        setUpLists();
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
        mCreateListDialog.show(getSupportFragmentManager(), "create list");
    }

    public void addListWithCode(MenuItem item) {
        //TODO
    }

    public void addListWithQR(MenuItem item) {
        //TODO
    }

    private void setUpLists() {
        // populate lists for testing
        pantryLists.add(new ProductList("Test1", "Pantry"));
        pantryLists.add(new ProductList("Test2", "Pantry"));
        pantryLists.add(new ProductList("Test3", "Pantry"));
        shoppingLists.add(new ProductList("Test4", "Shopping"));
        shoppingLists.add(new ProductList("Test5", "Shopping"));

        rvLists = findViewById(R.id.recyclerView);
        mPantryAdapter = new ListsAdapter(pantryLists);
        mShoppingAdapter = new ListsAdapter(shoppingLists);
    }
}