package pt.ulisboa.tecnico.cmov.shopist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.repository.PantryRepository;

@Singleton
public class ViewModel extends AndroidViewModel {

    PantryRepository pantryRepository;

    public ViewModel(@NonNull Application application) {
        super(application);
        pantryRepository = new PantryRepository(application);
    }

//    @Inject
//    ProductRepository productRepository = ProductRepository.getInstance();



    public Observable<List<Pantry>> getPantries() {
        return pantryRepository.getPantries();
    }

    public void addPantry(Pantry pantry) {
        pantryRepository.addPantry(pantry);
    }

    public void deletePantry(Pantry pantry) {
        pantryRepository.deletePantry(pantry);
    }
}
