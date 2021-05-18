package pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;

@Dao
public interface PantryDao {

    @Query("SELECT * FROM pantries")
    Observable<List<Pantry>> getPantries();

    @Query("SELECT * FROM pantries where pantryId == :id")
    Observable<Pantry> getPantry(Long id);

    @Query("SELECT * FROM pantries where uuid == :uuid")
    Single<Pantry> getPantryByUuid(String uuid);

    @Transaction
    @Query("SELECT * FROM pantries")
    Observable<List<PantryProduct>> getPantriesProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pantry pantry);

    @Delete
    void delete(Pantry pantry);
}
