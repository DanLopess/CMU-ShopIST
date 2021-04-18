package pt.ulisboa.tecnico.cmov.shopist.pojo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.PantryDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.remoteSource.BackendService;


public class PantryRepository {

    private static final PantryRepository instance = new PantryRepository();

    PantryDao pantryDao;

    BackendService backendService = BackendService.getInstance();

    List<Pantry> mCache = new ArrayList<>();

    private boolean mCacheIsDirty = false;

    private PantryRepository() {
        Observable.interval(30, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> mCacheIsDirty = true);
    }

    public static PantryRepository getInstance() {
        return instance;
    }

    public Observable<List<Pantry>> getPantries() {
        if(mCache != null && !mCacheIsDirty) {
            return Observable.just(mCache);
        }
        Observable<List<Pantry>> list = getPantriesFromDb().mergeWith(getPantriesFromApi());
        list.subscribe(pantries -> {mCache = pantries; mCacheIsDirty = false;});
        return list;
    }

    private Observable<List<Pantry>> getPantriesFromDb() {
        return pantryDao.getPantries();
    }

    private Observable<List<Pantry>> getPantriesFromApi() {
        return backendService.getPantries();
    }


}
