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
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.PantryDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;

@Singleton
public class PantryRepository implements Cache {

    PantryDao pantryDao;

    BackendService backendService = BackendService.getInstance();

    List<Pantry> mCache = new ArrayList<>();

    private boolean mCacheIsDirty = false;

    public PantryRepository(Application application) {
        this.pantryDao = ShopIstDatabase.getInstance(application).pantryDao();
    }

    public Observable<List<Pantry>> getPantries() {
        if(mCache != null && !mCacheIsDirty) {
            return Observable.just(mCache).mergeWith(getPantriesFromDb());
        }
        Observable<List<Pantry>> list = getPantriesFromDb();
        list.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(pantries -> {mCache = pantries; mCacheIsDirty = false;});
        return list;
    }

    public Observable<Pantry> getPantry(Long id) {
        return pantryDao.getPantry(id);
    }

    public void addPantry(Pantry pantry) {
        insertPantryToDb(pantry).subscribe(aBoolean -> mCache.add(pantry), throwable -> Log.d("DB ERROR", throwable.toString()));
    }

    public void deletePantry(Pantry pantry) {
        deletePantryFromDb(pantry).subscribe(aBoolean -> mCache.remove(pantry));
    }

    private Observable<List<Pantry>> getPantriesFromDb() {
        return pantryDao.getPantries();
    }
    private Observable<List<Pantry>> getPantriesFromApi() {
        return backendService.getPantries();
    }

    private Observable<Boolean> insertPantryToDb(@NonNull Pantry pantry) {
        return Observable.fromCallable(() -> {
            pantryDao.insert(pantry);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deletePantryFromDb(@NonNull Pantry pantry) {
        return Observable.fromCallable(() -> {
            pantryDao.delete(pantry);
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
