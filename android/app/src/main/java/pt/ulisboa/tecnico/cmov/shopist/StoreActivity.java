package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.adapter.PantryProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class StoreActivity extends AppCompatActivity {

    private Long myId;
    private Pantry pantry;
    private RecyclerView rvProducts;
    private PantryProductsAdapter adapter;
    private ViewModel viewModel;
    private ProductAndPrincipalImage productToShowDetails;

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
        //TODO scan product code
        //if new product, create and associate code
        //if code existent, add to pantry very fast
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.select_products_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView title = findViewById(R.id.add_product_title);

        myId = getIntent().getLongExtra("PantryId", -1);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getPantry(myId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(pantry -> {
            this.pantry = pantry;
            title.setText(this.pantry.name);
        });

        rvProducts = findViewById(R.id.pantry_prod_list);

        viewModel.getPantryProducts(myId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            adapter = new PantryProductsAdapter(list);
            rvProducts.setAdapter(adapter);
            rvProducts.setLayoutManager(new LinearLayoutManager(this));
        });
    }
}