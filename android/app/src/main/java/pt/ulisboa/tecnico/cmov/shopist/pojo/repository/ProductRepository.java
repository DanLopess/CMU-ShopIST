package pt.ulisboa.tecnico.cmov.shopist.pojo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.ProductDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.pojo.remoteSource.BackendService;

public class ProductRepository {

    private static final ProductRepository instance = new ProductRepository();

    private List<ProductAndPrincipalImage> mCache = new ArrayList<>();

    private BackendService backendService = BackendService.getInstance();

    ProductDao productDao;

    private boolean mCacheIsDirty = false;

    private ProductRepository() {
        Observable.interval(35, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> mCacheIsDirty = true);
    }

    public static ProductRepository getInstance() {
        return instance;
    }

    public Observable<List<Product>> getProducts() {
        if(mCache != null && !mCacheIsDirty) {
            return Observable.just((List<Product>) mCache.stream().map(m -> m.product));
        }
        Observable<List<Product>> list = getProductsFromDB().mergeWith(getProductsFromAPI());
        return list;
    }

    public Observable<List<ProductAndPrincipalImage>> getProductsAndImage() {
        if(mCache != null && !mCacheIsDirty) {
            return Observable.just(mCache);
        }
        Observable<List<ProductAndPrincipalImage>> list = getProductsAndPrincipalImageFromDB().mergeWith(getProductsAndPrincipalImageFromAPI());
        list.subscribe(productsAndImage -> {mCache = productsAndImage; mCacheIsDirty = false;});
        return list;
    }

    private Observable<List<Product>> getProductsFromDB() {
        return productDao.getProducts();
    }

    private Observable<List<Product>> getProductsFromAPI() {
        return backendService.getProducts();
    }

    private Observable<List<ProductAndPrincipalImage>> getProductsAndPrincipalImageFromDB() { return productDao.getProductsAndImage(); }
    private Observable<List<ProductAndPrincipalImage>> getProductsAndPrincipalImageFromAPI() { return backendService.getProductsAndPrincipalImage(); }


}
