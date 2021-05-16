package pt.ulisboa.tecnico.cmov.shopist.data.remoteSource;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendService {

    private final BackendAPI backendAPI;
    private static final BackendService backendServiceInstance = new BackendService();

    private BackendService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BackendAPI.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        backendAPI = retrofit.create(BackendAPI.class);

        Log.d("BACKENDSERVICE", "BackendService running");
    }

    public static BackendService getInstance() {
        return backendServiceInstance;
    }

    public Observable<Store> postStore(Store store) {
        Log.d("STORE-SENT", store.toString());
        return backendAPI.postStore(store);
    }

    public Observable<List<Pantry>> getPantries() {
        return backendAPI.getPantries();
    }

    public Observable<List<Product>> getProducts() {
        return Observable.just(null);
    }

    public Call<PantryDto> getPantry(String uuid) {
        return backendAPI.getPantryByUUID(uuid);
    }

    public Call<PantryDto> postPantryDto(PantryDto pantryDto) {
        return backendAPI.createPantry(pantryDto);
    }

    public void putPantryDto(PantryDto pantryDto) {
        backendAPI.updatePantry(pantryDto).enqueue(new Callback<PantryDto>() {
            @Override
            public void onResponse(@NonNull Call<PantryDto> call, @NonNull Response<PantryDto> response) {
                // Doesnt need the response yet
            }

            @Override
            public void onFailure(@NonNull Call<PantryDto> call, @NonNull Throwable t) {
                Log.e("BackendService", "Failed to update pantry on server");
            }
        });
    }
}
