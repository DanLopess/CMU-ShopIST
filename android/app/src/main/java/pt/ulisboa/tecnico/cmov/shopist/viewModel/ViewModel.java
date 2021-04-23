package pt.ulisboa.tecnico.cmov.shopist.viewModel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.LocationEntity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.repository.PantryRepository;
import pt.ulisboa.tecnico.cmov.shopist.data.repository.ProductRepository;

@Singleton
public class ViewModel extends AndroidViewModel {

    PantryRepository pantryRepository;
    ProductRepository productRepository;

    public ViewModel(@NonNull Application application) {
        super(application);
        pantryRepository = new PantryRepository(application);
        productRepository = new ProductRepository(application);
    }

    public Observable<List<Pantry>> getPantries() {
        return pantryRepository.getPantries();
    }

    public Observable<List<Product>> getProducts() {
        return productRepository.getProducts();
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
        pantryRepository.deletePantry(pantry);
    }

    public Observable<List<PantryProduct>> getPantryProducts(Long pantryId) {
        return productRepository.getPantryProducts(pantryId);
    }

    public void addProduct(String name, String description) {
        productRepository.addProduct(new Product(name, description));
    }

    public void addPantryProducts(Long pantryId, List<Product> products) {
        productRepository.addPantryProducts(pantryId, products);
    }
}
