package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;
import pt.ulisboa.tecnico.cmov.shopist.adapter.SelectProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.dialog.CreateProductDialogFragment;
import pt.ulisboa.tecnico.cmov.shopist.pojo.AppContextData;
import pt.ulisboa.tecnico.cmov.shopist.pojo.Product;

public class AddProductsActivity extends AppCompatActivity {

    private List<Product> selectedProducts;
    private DialogFragment mCreateProductDialog;
    private RecyclerView rvProducts;
    private SelectProductsAdapter adapter;
    private AppContextData mContextData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        initialize();
    }

    public void returnResult(View v) {
        selectedProducts = adapter.getSelectedItems();
        if (selectedProducts != null) {
            Intent data = new Intent();
            data.putExtra("Selected Products", (Serializable) selectedProducts);
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    public void cancel(View v) {
        finish();
    }

    public void createProduct(MenuItem item) {
        mCreateProductDialog.show(getSupportFragmentManager(), "create product");
    }

    private void initialize() {
        mContextData = (AppContextData) getApplicationContext();
        selectedProducts = new ArrayList<>();
        mCreateProductDialog = new CreateProductDialogFragment(this);
        rvProducts = findViewById(R.id.rv_existing_products);
        adapter = new SelectProductsAdapter(mContextData.getProducts());
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
    }
}