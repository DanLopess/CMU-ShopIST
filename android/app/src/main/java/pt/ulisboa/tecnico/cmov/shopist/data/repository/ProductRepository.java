package pt.ulisboa.tecnico.cmov.shopist.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.R;
import pt.ulisboa.tecnico.cmov.shopist.StoreActivity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.ShopIstDatabase;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.ProductDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.StoreProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;
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

    //================================== Products ==================================

    public Observable<List<Product>> getProducts() {
        // if(mCache != null && !mCacheIsDirty) {
        //     return Observable.just(mCache);
        // }
        return getProductsFromDB();
        // .mergeWith(getProductsFromAPI());
    }

    public Observable<ProductAndPrincipalImage> getProductAndPrincipalImage(Long id) {
        return productDao.getProductAndImage(id);
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

    public Observable<Integer> getQttNeeded(Long productId) {
        return productDao.getQttNeeded(productId);
    }

    public Observable<Product> getProductByCode(String code) {
        return productDao.getProductByCode(code);
    }

    public Observable<Boolean> checkIfProdExistsByCode(String code) {
        return productDao.checkIfProdExistsByCode(code);
    }

    public void updateProduct(Product product) {
        insertProductToDb(product).subscribe(aBoolean -> {});
    }

    public void deleteProduct(Product product) {
        deleteProductFromDb(product).subscribe(aBoolean -> {});
    }

    private Observable<Boolean> deleteProductFromDb(@NonNull Product product) {
        return Observable.fromCallable(() -> {
            productDao.deleteProduct(product);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //================================== Pantry Products ==================================

    public Observable<List<PantryProduct>> getPantryProducts(Long pantryId) {
        return productDao.getPantryProducts(pantryId);
    }

    public Observable<Integer> getPantrySize(Long pantryId) {
        return productDao.getPantrySize(pantryId);
    }

    public void addPantryProducts(Long pantryId, List<Product> products) {
        for (Product prod : products) {
            PantryProductCrossRef pantryProduct = new PantryProductCrossRef(pantryId, prod.productId);
            insertPantryProductToDb(pantryProduct).subscribe(aBoolean -> {});
        }
    }

    public void deletePantryProduct(PantryProductCrossRef pantryProd) {
        deletePantryProductFromDb(pantryProd).subscribe(aBoolean -> {});
    }

    public void deletePantryProducts(Long pantryId) {
        deletePantryProductsFromDb(pantryId).subscribe(aBoolean -> {});
    }

    public void updatePantryProduct(PantryProductCrossRef pantryProduct) {
        insertPantryProductToDb(pantryProduct).subscribe(aBoolean -> {});
    }

    private Observable<Boolean> insertPantryProductToDb(@NonNull PantryProductCrossRef pantryProd) {
        return Observable.fromCallable(() -> {
            productDao.insertPantryProduct(pantryProd);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deletePantryProductFromDb(@NonNull PantryProductCrossRef pantryProd) {
        return Observable.fromCallable(() -> {
            productDao.deletePantryProduct(pantryProd);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deletePantryProductsFromDb(@NonNull Long pantryId) {
        return Observable.fromCallable(() -> {
            productDao.deletePantryProducts(pantryId);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //================================== Store Products ==================================

    public Observable<List<StoreProduct>> getStoreProducts(Long storeId) {
        return productDao.getStoreProducts(storeId);
    }

    public Observable<List<StoreProduct>> getShownStoreProducts(Long storeId) {
        return productDao.getShownStoreProducts(storeId);
    }

    public Observable<Integer> getStoreSize(Long storeId) {
        return productDao.getStoreSize(storeId);
    }

    public void addStoreProducts(Long storeId, List<Product> products) {
        for (Product prod : products) {
            StoreProductCrossRef storeProduct = new StoreProductCrossRef(storeId, prod.productId);
            productDao.getQttNeeded(prod.getProductId())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(qtt -> {
                storeProduct.setQttNeeded(qtt);
                storeProduct.updateShown();
                insertStoreProductToDb(storeProduct).subscribe(aBoolean -> {});
            });
        }
    }

    public void deleteStoreProduct(StoreProductCrossRef storeProd) {
        deleteStoreProductFromDb(storeProd).subscribe(aBoolean -> {});
    }

    public void deleteStoreProducts(Long storeId) {
        deleteStoreProductsFromDb(storeId).subscribe(aBoolean -> {});
    }

    public void updateStoreProduct(StoreProductCrossRef storeProduct) {
        insertStoreProductToDb(storeProduct).subscribe(aBoolean -> {});
    }

    private Observable<Boolean> insertStoreProductToDb(@NonNull StoreProductCrossRef storeProd) {
        return Observable.fromCallable(() -> {
            productDao.insertStoreProduct(storeProd);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deleteStoreProductFromDb(@NonNull StoreProductCrossRef storeProd) {
        return Observable.fromCallable(() -> {
            productDao.deleteStoreProduct(storeProd);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deleteStoreProductsFromDb(@NonNull Long storeId) {
        return Observable.fromCallable(() -> {
            productDao.deleteStoreProducts(storeId);
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
