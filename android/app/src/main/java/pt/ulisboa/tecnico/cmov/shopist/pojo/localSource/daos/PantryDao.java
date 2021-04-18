package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.relations.PantryWithProducts;

@Dao
public interface PantryDao {

    @Query("SELECT * FROM pantries")
    Observable<List<Pantry>> getPantries();

    @Transaction
    @Query("SELECT * FROM pantries")
    List<PantryWithProducts> getPantriesWithProducts();
}
