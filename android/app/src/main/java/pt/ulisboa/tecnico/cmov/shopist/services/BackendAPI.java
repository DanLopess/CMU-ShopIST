package pt.ulisboa.tecnico.cmov.shopist.services;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.Store;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendAPI {
    String BASE_URL = "http://daniellopes.ddns.net/";

    @POST("/api/store/")
    Observable<Store> postStore(@Body Store store);
}
