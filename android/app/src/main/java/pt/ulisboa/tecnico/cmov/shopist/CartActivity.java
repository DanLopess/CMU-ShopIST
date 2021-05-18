package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.adapter.CartProductsAdapter;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;
import pt.ulisboa.tecnico.cmov.shopist.viewModel.ViewModel;

public class CartActivity extends AppCompatActivity {

    private Long storeId;
    private ViewModel viewModel;
    private CartProductsAdapter adapter;
    private RecyclerView rvProducts;

    Map<Integer, Long> pantriesPos = new HashMap<>();
    List<String> pantriesNames = new ArrayList<>();
    List<StoreProduct> cartProds = new ArrayList<>();
    Integer time = null;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle(R.string.choose_a_pantry);

        String[] options = new String[pantriesNames.size()];
        pantriesNames.toArray(options);

        builder.setItems(options, (dialog, which) -> {
                    updateProducts(pantriesPos.get(which));
                    finish();
                });

        builder.setNegativeButton(R.string.cancel, (dialog, which) ->
                dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateProducts(Long pantryId) {
        AtomicInteger qtt = new AtomicInteger();
        viewModel.getPantryProducts(pantryId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<PantryProduct>>() {
                     @Override
                     public void onNext(@NonNull List<PantryProduct> pantryProducts) {
                         for (StoreProduct prod : cartProds) {
                             qtt.set(prod.getQttCart());
                             for (PantryProduct pp : pantryProducts) {
                                 if (pp.getProduct().getProductId().equals(prod.getProduct().getProductId())) {
                                     pp.increaseQttAvailable(qtt.get());
                                     pp.decreaseQttNeeded(qtt.get());
                                     viewModel.updatePantryProduct(pp);
                                     prod.setQttCart(0);
                                     viewModel.updateStoreProduct(prod);
                                     break;
                                 }
                             }
                             viewModel.addPantryProductFromCart(pantryId, prod.getProduct(), qtt.get());
                             prod.setQttCart(0);
                             viewModel.updateStoreProduct(prod);
                         }
                         onComplete();
                     }

                     @Override
                     public void onError(@NonNull Throwable e) {
                        dispose();
                     }

                     @Override
                     public void onComplete() {
                         dispose();
                     }
                });
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView cartTotalCost = findViewById(R.id.cart_total_cost);
        AtomicReference<Double> totalCost = new AtomicReference<>();
        AtomicInteger i = new AtomicInteger();

        storeId = getIntent().getLongExtra("storeId", -1);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        rvProducts = findViewById(R.id.cart_prod_list);

        TextView queueTime = findViewById(R.id.cart_queue_time);

        Button finishShoppingBt = findViewById(R.id.cartFinishShopping_bt);
        finishShoppingBt.setOnClickListener(v -> finishShopping());

        if(AppGlobalContext.getUUID() != null) {
            viewModel.getStore(storeId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(store1 -> {
                        viewModel.getEstimationStats(store1, AppGlobalContext.getUUID()).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(responseDTO -> {
                                    time = responseDTO.getEstimationTimeInQueue();
                                    if (time != null && time >= 0) {
                                        TextView text = findViewById(R.id.cart_queue_time_text);
                                        text.setText(getString(R.string.queue_time_estimation));
                                        new CountDownTimer(time * 1000, 1000) {

                                            public void onTick(long millisUntilFinished) {
                                                queueTime.setText(millisUntilFinished / 1000 + " " + getString(R.string.seconds));
                                            }

                                            public void onFinish() {
                                                queueTime.setText(0 + " " + getString(R.string.seconds));
                                            }

                                        }.start();
                                    }
                                }, Throwable::printStackTrace);
                    }, Throwable::printStackTrace);
        }


        viewModel.getStoreProductsInCart(storeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<StoreProduct>>() {
                       @Override
                       public void onNext(@NonNull List<StoreProduct> list) {
                           totalCost.set(0.0);
                           cartProds = list;
                           adapter = new CartProductsAdapter(list);
                           rvProducts.setAdapter(adapter);
                           rvProducts.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                           for (StoreProduct prod : list) {
                               totalCost.updateAndGet(v -> v + prod.getQttCart() * prod.getPrice());
                           }
                           String totalCostText = getString(R.string.total) + ": " + totalCost.get() + " €";
                           cartTotalCost.setText(totalCostText);
                       }

                       @Override
                       public void onError(@NonNull Throwable e) {
                           dispose();
                       }

                       @Override
                       public void onComplete() {
                           dispose();
                       }
                   });

        viewModel.getPantries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(pantries -> {
                    for (Pantry p : pantries) {
                        pantriesPos.put(i.getAndIncrement(), p.getPantryId());
                        pantriesNames.add(p.getName());
                    }
                });
    }
}