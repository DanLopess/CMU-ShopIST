package pt.ulisboa.tecnico.cmov.shopist;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.adapter.StoreProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.dialog.CreateProductDialogFragment;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class StoreActivity extends ProductListActivity {

    private static final int SCAN_REQ_CODE = 1;

    private Long myId;
    private Store store;
    private RecyclerView rvProducts;
    private StoreProductsAdapter adapter;
    private ViewModel viewModel;
    private ProductAndPrincipalImage productToShowDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        initialize();
    }

    @Override
    public Location getListLocation() {
        if (store.getLocationWrapper() == null) return null;
        return store.getLocationWrapper().toLocation();
    }

    public void addProducts(MenuItem item) {
        Intent intent = new Intent(this, AddStoreProductsActivity.class);
        intent.putExtra("storeId", myId);
        startActivity(intent);
    }

    public void scanProduct(MenuItem item) {
        Intent intent = new Intent(this, ScanCodeActivity.class);
        startActivityForResult(intent, SCAN_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQ_CODE && resultCode == RESULT_OK) {
            assert data != null;
            String code = data.getStringExtra("code");
            viewModel.checkIfProdExistsByCode(code).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(exists -> {
                if (!exists) {
                    CreateProductDialogFragment dialog = new CreateProductDialogFragment(this, code, CreateProductDialogFragment.STORE);
                    dialog.show(getSupportFragmentManager(), "create product");
                }
                viewModel.getProductByCode(code).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(product -> {
                    viewModel.addStoreProducts(myId, Collections.singletonList(product));
                    adapter.notifyDataSetChanged();
                });
            });
        }
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.store_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView title = findViewById(R.id.store_title);

        myId = getIntent().getLongExtra("StoreId", -1);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getStore(myId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(store -> {
            this.store = store;
            title.setText(this.store.getName());
            initializeMap();
        });

        rvProducts = findViewById(R.id.store_prod_list);

        viewModel.getShownStoreProducts(myId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            adapter = new StoreProductsAdapter(list);
            rvProducts.setAdapter(adapter);
            rvProducts.setLayoutManager(new LinearLayoutManager(this));
        });
    }
}