package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.ulisboa.tecnico.cmov.shopist.adapter.ListsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.pojo.AppContextData;
import pt.ulisboa.tecnico.cmov.shopist.dialog.CreateListDialogFragment;

public class MainActivity extends AppCompatActivity {
    private DialogFragment mCreateListDialog;
    private String mCurrCategory;
    private RecyclerView rvLists;
    private ListsAdapter mPantryAdapter;
    private ListsAdapter mShoppingAdapter;
    private AppContextData mContextData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load local lists
        //if wifi available sync lists

        mContextData = (AppContextData) getApplicationContext();
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
                    return true;
                }
        );

        // Select initial list TODO based on location
        // If we can get the current location and associate it with  only one list, open that list by default
        //for now select Pantry list
        bottomNavigationView.setSelectedItemId(R.id.action_pantry_lists);
    }

    /*private void loadProducts() {
        RecyclerView rvProducts = (RecyclerView) findViewById(R.id.recyclerView);
        PantryProductsAdapter adapter = new PantryProductsAdapter(mContextData.getProducts());
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
    }*/

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
        rvLists = findViewById(R.id.recyclerView);
        mPantryAdapter = new ListsAdapter(mContextData.getPantryLists());
        mShoppingAdapter = new ListsAdapter(mContextData.getShoppingLists());
    }

    /*public void createNewProduct(MenuItem item) {
        DialogFragment createListDialog = new CreateProductDialogFragment(this);
        createListDialog.show(getSupportFragmentManager(), "create list");
    }*/
}