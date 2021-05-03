package pt.ulisboa.tecnico.cmov.shopist;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.adapter.SelectProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.dialog.CreateProductDialogFragment;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class AddStoreProductsActivity extends AppCompatActivity {

    private DialogFragment mCreateProductDialog;
    private RecyclerView rvProducts;
    private SelectProductsAdapter adapter;
    private ViewModel viewModel;
    private Long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store_products);

        initialize();
    }

    public void returnResult(View v) {
        List<Product> selectedProducts = adapter.getSelectedItems();
        if (!selectedProducts.isEmpty())
            viewModel.addStoreProducts(storeId, selectedProducts);
        finish();
    }

    public void cancel(View v) {
        finish();
    }

    public void createProduct(MenuItem item) {
        mCreateProductDialog.show(getSupportFragmentManager(), "create product");
    }

    private void initialize() {
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        storeId = getIntent().getLongExtra("storeId", -1);
        mCreateProductDialog = new CreateProductDialogFragment(this);

        rvProducts = findViewById(R.id.rv_existing_products);

        viewModel.getProducts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            adapter = new SelectProductsAdapter(list);
            rvProducts.setAdapter(adapter);
            rvProducts.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    public ViewModel getViewModel() {
        return viewModel;
    }
}