package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.adapter.PantryProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.dialog.CreateProductDialogFragment;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class PantryActivity extends ProductListActivity {

    private static final int SCAN_REQ_CODE = 1;

    private Pantry pantry;
    private RecyclerView rvProducts;
    private PantryProductsAdapter adapter;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        initialize();
    }

    public void addProducts(MenuItem item) {
        Intent intent = new Intent(this, AddPantryProductsActivity.class);
        intent.putExtra("pantryId", myId);
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
                            CreateProductDialogFragment dialog = new CreateProductDialogFragment(this, code, CreateProductDialogFragment.PANTRY);
                            dialog.show(getSupportFragmentManager(), "create product");
                        }
                        viewModel.getProductByCode(code).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(product -> {
                            viewModel.addPantryProducts(myId, Collections.singletonList(product));
                            adapter.notifyDataSetChanged();
                        });
            });
        }
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.select_products_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView title = findViewById(R.id.add_product_title);
        TextView description = findViewById(R.id.activity_pantry_list_desc);

        myId = getIntent().getLongExtra("PantryId", -1);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getPantry(myId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(pantry -> {
                    this.pantry = pantry;
                    title.setText(this.pantry.getName());
                    description.setText(this.pantry.getDescription());
                    description.setVisibility(description.getText().toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                    initializeMap();
                });

        rvProducts = findViewById(R.id.pantry_prod_list);

        viewModel.getPantryProducts(myId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
                    adapter = new PantryProductsAdapter(list);
                    rvProducts.setAdapter(adapter);
                    rvProducts.setLayoutManager(new LinearLayoutManager(this));
                });
        initializeMap();
    }

    @Override
    public Location getListLocation() {
        if (pantry.getLocationWrapper() == null) return null;
        return pantry.getLocationWrapper().toLocation();
    }
}