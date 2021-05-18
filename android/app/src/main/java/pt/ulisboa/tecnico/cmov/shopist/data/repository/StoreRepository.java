package pt.ulisboa.tecnico.cmov.shopist.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.ShopIstDatabase;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.StoreDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;
import pt.ulisboa.tecnico.cmov.shopist.data.dto.Beacon;
import pt.ulisboa.tecnico.cmov.shopist.data.dto.Coordinates;
import pt.ulisboa.tecnico.cmov.shopist.data.dto.QueueTimeRequestDTO;
import pt.ulisboa.tecnico.cmov.shopist.data.dto.QueueTimeResponseDTO;

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
            if(store.getLocationWrapper() != null) {
                backendService.postBeacon(new Beacon(new Coordinates(store.getLocationWrapper().getLatitude(), store.getLocationWrapper().getLongitude())))
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<Beacon>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Beacon beacon) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> deleteStoreFromDb(@NonNull Store store) {
        return Observable.fromCallable(() -> {
            storeDao.delete(store);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void updateStore(Store store) {
        insertStoreToDb(store).subscribe(aBoolean -> {});
    }

    @Override
    public void clearCache() {
        mCache = new ArrayList<>();
    }

    @Override
    public void makeCacheDirty() {
        mCacheIsDirty = true;
    }

    public Observable<QueueTimeResponseDTO> getStats(Coordinates coordinates, UUID uuid) {
        return backendService.getQueueTime(new QueueTimeRequestDTO(coordinates, uuid));
    }
}
