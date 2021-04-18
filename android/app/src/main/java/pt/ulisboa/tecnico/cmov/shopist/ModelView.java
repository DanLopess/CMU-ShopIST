package pt.ulisboa.tecnico.cmov.shopist;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.repository.PantryRepository;
import pt.ulisboa.tecnico.cmov.shopist.pojo.repository.ProductRepository;

public class ModelView {
    PantryRepository pantryRepository = PantryRepository.getInstance();
    ProductRepository productRepository = ProductRepository.getInstance();

    public List<Pantry> getPantries() {
        pantryRepository.getPantries();
    }

    public List<Product> getProducts() {

    }
}
