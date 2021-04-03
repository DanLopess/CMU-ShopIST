package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.data.AppContextData;
import pt.ulisboa.tecnico.cmov.shopist.data.Product;

public class MainActivity extends AppCompatActivity {
    private String currCategory;

    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load local lists
        //if wifi available sync lists

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView titleTextView = findViewById(R.id.textView_title);



        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    currCategory = item.getTitle().toString(); // To set the default for the list creation
                    titleTextView.setText(currCategory);
                    return false;
                }
        );

        // Select initial list TODO based on location
        //for now select Pantry list
        bottomNavigationView.setSelectedItemId(R.id.action_pantry_lists);
    }

    private void loadProducts() {
        RecyclerView rvProducts = (RecyclerView) findViewById(R.id.recyclerView);
        AppContextData data = (AppContextData) getApplicationContext();
        ProductsAdapter adapter = new ProductsAdapter(data.getProducts());
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    // TODO call to the dialog
    // In order to have a pre-selected value displayed, you can call setText(CharSequence text, boolean filter) on the AutoCompleteTextView with the filter set to false.
    // Set Category default to current category
    // Set location default to use current location, other options are none or pick location on map
    public void createNewList(MenuItem item) {
        DialogFragment createListDialog = new CreateListDialogFragment(this);
        createListDialog.show(getSupportFragmentManager(), "create list");
    }

    public void createNewProduct(MenuItem item) {
        DialogFragment createListDialog = new CreateProductDialogFragment(this);
        createListDialog.show(getSupportFragmentManager(), "create list");
    }
}