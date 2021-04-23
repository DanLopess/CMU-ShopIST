package pt.ulisboa.tecnico.cmov.shopist.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.ShopIstDatabase;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.ProductDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;

@Singleton
public class ProductRepository implements Cache {

    private List<Product> mCache = new ArrayList<>();

    private BackendService backendService = BackendService.getInstance();

    ProductDao productDao;

    private boolean mCacheIsDirty = false;

    public ProductRepository(Application application) {
        this.productDao = ShopIstDatabase.getInstance(application).productDao();
    }

    public Observable<List<Product>> getProducts() {
        // if(mCache != null && !mCacheIsDirty) {
        //     return Observable.just(mCache);
        // }
        return getProductsFromDB();
        // .mergeWith(getProductsFromAPI());
    }

    // public Observable<List<ProductAndPrincipalImage>> getProductsAndImage() {
    //     if(mCache != null && !mCacheIsDirty) {
    //         return Observable.just(mCache);
    //     }
    //     Observable<List<ProductAndPrincipalImage>> list = getProductsAndPrincipalImageFromDB().mergeWith(getProductsAndPrincipalImageFromAPI());
    //     list.subscribe(productsAndImage -> {mCache = productsAndImage; mCacheIsDirty = false;});
    //     return list;
    // }

    private Observable<List<Product>> getProductsFromDB() {
        return productDao.getProducts();
    }

    private Observable<List<Product>> getProductsFromAPI() {
        return backendService.getProducts();
    }

    private Observable<List<ProductAndPrincipalImage>> getProductsAndPrincipalImageFromDB() { return productDao.getProductsAndImage(); }
    private Observable<List<ProductAndPrincipalImage>> getProductsAndPrincipalImageFromAPI() { return backendService.getProductsAndPrincipalImage(); }

    public void addProduct(Product product) {
        insertProductToDb(product).subscribe(aBoolean -> mCache.add(product), throwable -> Log.d("DB ERROR", throwable.toString()));
    }

    private Observable<Boolean> insertProductToDb(@NonNull Product product) {
        return Observable.fromCallable(() -> {
            productDao.insert(product);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //===================================== Pantry Products ==================================

    public Observable<List<PantryProduct>> getPantryProducts(Long pantryId) {
        return productDao.getPantryProducts(pantryId);
    }

    public void addPantryProducts(Long pantryId, List<Product> products) {
        for (Product prod : products) {
            PantryProductCrossRef pantryProduct = new PantryProductCrossRef(pantryId, prod.productId);
            insertPantryProductToDb(pantryProduct);
        }
    }

    private Observable<Boolean> insertPantryProductToDb(@NonNull PantryProductCrossRef pantryProd) {
        return Observable.fromCallable(() -> {
            productDao.insertPantryProduct(pantryProd);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void clearCache() {
        mCache = new ArrayList<>();
    }

    @Override
    public void makeCacheDirty() {
        mCacheIsDirty = true;
    }

}
