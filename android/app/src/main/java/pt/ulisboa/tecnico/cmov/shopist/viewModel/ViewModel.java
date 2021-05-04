package pt.ulisboa.tecnico.cmov.shopist.viewModel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.LocationEntity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.ProductImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.StoreProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.repository.PantryRepository;
import pt.ulisboa.tecnico.cmov.shopist.data.repository.ProductRepository;
import pt.ulisboa.tecnico.cmov.shopist.data.repository.StoreRepository;

@Singleton
public class ViewModel extends AndroidViewModel {

    PantryRepository pantryRepository;
    StoreRepository storeRepository;
    ProductRepository productRepository;

    public ViewModel(@NonNull Application application) {
        super(application);
        pantryRepository = new PantryRepository(application);
        storeRepository = new StoreRepository(application);
        productRepository = new ProductRepository(application);
    }

    //================================== Products ==================================

    public Observable<List<Product>> getProducts() {
        return productRepository.getProducts();
    }

    public void addProduct(String name, String description) {
        productRepository.addProduct(new Product(name, description));
    }

    public Observable<ProductAndPrincipalImage> getProductAndPrincipalImage(long id) {
        //return productRepository.getProductAndPrincipalImage(id);
        return Observable.just(new ProductAndPrincipalImage());
    }

    public Observable<ProductImage> getProductImage(Long productId) {
        return Observable.just(new ProductImage());
    }

    public Observable<Integer> getQttNeeded(Product product) {
        return productRepository.getQttNeeded(product.getProductId());
    }

    //================================== Pantry ==================================

    public Observable<List<Pantry>> getPantries() {
        return pantryRepository.getPantries();
    }

    public Observable<Pantry> getPantry(Long id) {
        return pantryRepository.getPantry(id);
    }

    public void addPantry(String name, String description, Location location) {
        if (location != null)
            pantryRepository.addPantry(new Pantry(name, description, new LocationEntity(location.getLatitude(), location.getLongitude())));
        else
            pantryRepository.addPantry(new Pantry(name, description, null));
    }

    public void deletePantry(Pantry pantry) {
        productRepository.deletePantryProducts(pantry.getPantryId());
        pantryRepository.deletePantry(pantry);
    }

    public void addPantryProducts(Long pantryId, List<Product> products) {
        productRepository.addPantryProducts(pantryId, products);
    }

    public Observable<List<PantryProduct>> getPantryProducts(Long pantryId) {
        return productRepository.getPantryProducts(pantryId);
    }

    public Observable<Integer> getPantrySize(Long pantryId) {
        return productRepository.getPantrySize(pantryId);
    }

    public void updatePantryProduct(PantryProduct pantryProduct) {
        productRepository.updatePantryProduct(pantryProdToCrossRef(pantryProduct));
    }

    public void deletePantryProduct(PantryProduct pantryProduct) {
        productRepository.deletePantryProduct(pantryProdToCrossRef(pantryProduct));
    }

    //================================== Store ==================================

    public Observable<List<Store>> getStores() {
        return storeRepository.getStores();
    }

    public Observable<Store> getStore(Long id) {
        return storeRepository.getStore(id);
    }

    public void addStore(String name, Location location) {
        if (location != null)
            storeRepository.addStore(new Store(name, new LocationEntity(location.getLatitude(), location.getLongitude())));
        else
            storeRepository.addStore(new Store(name, null));
    }

    public void deleteStore(Store store) {
        productRepository.deleteStoreProducts(store.getStoreId());
        storeRepository.deleteStore(store);
    }

    public void addStoreProducts(Long storeId, List<Product> products) {
        productRepository.addStoreProducts(storeId, products);
    }

    public Observable<List<StoreProduct>> getStoreProducts(Long storeId) {
        return productRepository.getStoreProducts(storeId);
    }

    public Observable<List<StoreProduct>> getShownStoreProducts(Long storeId) {
        return productRepository.getShownStoreProducts(storeId);
    }

    public Observable<Integer> getStoreSize(Long storeId) {
        return productRepository.getStoreSize(storeId);
    }

    public void updateStoreProduct(StoreProduct storeProduct) {
        productRepository.updateStoreProduct(storeProdToCrossRef(storeProduct));
    }

    public void deleteStoreProduct(StoreProduct storeProduct) {
        productRepository.deleteStoreProduct(storeProdToCrossRef(storeProduct));
    }

    //================================== Auxiliary ==================================

    private PantryProductCrossRef pantryProdToCrossRef(PantryProduct pantryProduct) {
        Long pantryId = pantryProduct.getPantry().getPantryId();
        Long productId = pantryProduct.getProduct().getProductId();
        return new PantryProductCrossRef(
                pantryId,
                productId,
                pantryProduct.getQttAvailable(),
                pantryProduct.getQttNeeded()
        );
    }

    private StoreProductCrossRef storeProdToCrossRef(StoreProduct storeProduct) {
        Long storeId = storeProduct.getStore().getStoreId();
        Long productId = storeProduct.getProduct().getProductId();
        return new StoreProductCrossRef(
                storeId,
                productId,
                storeProduct.getPrice(),
                storeProduct.getQttNeeded(),
                storeProduct.getQttCart(),
                storeProduct.getShown()
        );
    }
}
