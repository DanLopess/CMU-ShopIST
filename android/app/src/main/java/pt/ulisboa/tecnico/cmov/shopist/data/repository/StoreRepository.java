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
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.StoreDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;

@Singleton
public class StoreRepository implements Cache {

    StoreDao storeDao;

    BackendService backendService = BackendService.getInstance();

    List<Store> mCache = new ArrayList<>();

    private boolean mCacheIsDirty = false;

    public StoreRepository(Application application) {
        this.storeDao = ShopIstDatabase.getInstance(application).storeDao();
    }

    public Observable<List<Store>> getStores() {
        if(mCache != null && !mCacheIsDirty) {
            return Observable.just(mCache).mergeWith(getStoresFromDb());
        }
        Observable<List<Store>> list = getStoresFromDb();
        list.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(stores -> {mCache = stores; mCacheIsDirty = false;});
        return list;
    }

    public Observable<Store> getStore(Long id) {
        return storeDao.getStore(id);
    }

    public void addStore(Store store) {
        insertStoreToDb(store).subscribe(aBoolean -> mCache.add(store), throwable -> Log.d("DB ERROR", throwable.toString()));
    }

    public void deleteStore(Store store) {
        deleteStoreFromDb(store).subscribe(aBoolean -> mCache.remove(store));
    }

    private Observable<List<Store>> getStoresFromDb() {
        return storeDao.getStores();
    }

    private Observable<Boolean> insertStoreToDb(@NonNull Store store) {
        return Observable.fromCallable(() -> {
            storeDao.insert(store);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deleteStoreFromDb(@NonNull Store store) {
        return Observable.fromCallable(() -> {
            storeDao.delete(store);
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
