package pt.ulisboa.tecnico.cmov.shopist.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.ShopIstDatabase;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.PantryDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryProductDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        // first try to get from cache, then get fro
        return pantryDao.getPantry(id);
    }

    public Single<Pantry> getPantryByUuid(String uuid) {
        // first try to get from cache, then get fro
        return pantryDao.getPantryByUuid(uuid);
    }

    public Call<PantryDto> getAPIPantryByUuid(String uuid) {
        // first try to get from cache, then get fro
        return backendService.getPantry(uuid);
    }

    public void addPantry(Pantry pantry) {
        insertPantryToDb(pantry).subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                mCache.add(pantry);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                dispose();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public Observable<Boolean> addPantryWithProducts(Pantry pantry, PantryDto pantryDto) {
        return insertPantryToDb(pantry);
    }

    public void deletePantry(Pantry pantry) {
        deletePantryFromDb(pantry).subscribe(aBoolean -> mCache.remove(pantry));
    }

    /**
     * Method used for getting a shared pantry from the backend server and adding it to the local database
     */
    public Call<PantryDto> saveSyncedPantryFromBackend(String uuid) {
        return backendService.getPantry(uuid);
    }

    public void savePantryToBackend(Pantry pantry, List<PantryProduct> products) {
        PantryDto pantryDto = new PantryDto(pantry, products);
        Call<PantryDto> call = backendService.postPantryDto(pantryDto);
        call.enqueue(new Callback<PantryDto>() {
            @Override
            public void onResponse(Call<PantryDto> call, Response<PantryDto> response) {
                Log.i("PantryRepository", "Saved pantry on server");
                PantryDto receivedDto = response.body();
                if (receivedDto != null) {
                    pantry.setUuid(receivedDto.getUuid());
                    if (pantry.getUuid() != null) {
                        pantry.setShared(true);
                        addPantry(pantry); // update pantry
                    }
                }
            }
            @Override
            public void onFailure(Call<PantryDto> call, Throwable t) {
                Log.e("PantryRepository", "Failed to create pantry on server");
            }
        });
    }

    public void updatePantry(Pantry pantry, List<PantryProduct> products) {
        if (pantry.isShared() && pantry.getUuid() != null) {
            PantryDto pantryDto = new PantryDto(pantry, products);
            backendService.putPantryDto(pantryDto);
        }
        addPantry(pantry); // update pantry
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

    public void updatePantry(Pantry pantry) {
        insertPantryToDb(pantry).subscribe(aBoolean -> {});
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
