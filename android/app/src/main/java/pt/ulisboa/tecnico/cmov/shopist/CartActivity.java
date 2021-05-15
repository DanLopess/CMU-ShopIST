package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.adapter.CartProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class CartActivity extends AppCompatActivity {

    private Long storeId;
    private ViewModel viewModel;
    private CartProductsAdapter adapter;
    private RecyclerView rvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialize();
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    private void finishShopping() {

    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView cartTotalCost = findViewById(R.id.cart_total_cost);
        AtomicReference<String> costText = new AtomicReference<>(getString(R.string.total) + ": ");
        AtomicReference<Double> totalCost = new AtomicReference<>(0.0);

        storeId = getIntent().getLongExtra("storeId", -1);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        rvProducts = findViewById(R.id.cart_prod_list);

        Button finishShoppingBt = findViewById(R.id.cartFinishShopping_bt);
        finishShoppingBt.setOnClickListener(v -> finishShopping());

        viewModel.getStoreProductsInCart(storeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            adapter = new CartProductsAdapter(list);
            rvProducts.setAdapter(adapter);
            rvProducts.setLayoutManager(new LinearLayoutManager(this));
            for (StoreProduct prod : list) {
                totalCost.updateAndGet(v -> v + prod.getQttCart() * prod.getPrice());
            }
            costText.updateAndGet(v -> v + totalCost + " €");
            cartTotalCost.setText(costText.get());
        });
    }
}