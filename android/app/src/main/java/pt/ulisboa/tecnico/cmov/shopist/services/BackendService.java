package pt.ulisboa.tecnico.cmov.shopist.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import pt.ulisboa.tecnico.cmov.shopist.data.Product;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendService extends Service {

    private BackendAPI backendAPI;

    BackendService() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BackendAPI.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        backendAPI = retrofit.create(BackendAPI.class);
    }


    Observable<String> createProduct(Product product) {
        return backendAPI.createProduct(product);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
