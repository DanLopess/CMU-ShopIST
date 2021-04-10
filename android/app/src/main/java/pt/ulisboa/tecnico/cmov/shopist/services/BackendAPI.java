package pt.ulisboa.tecnico.cmov.shopist.services;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.Product;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendAPI {
    String BASE_URL = "localhost:8080";

    @POST("/product")
    Observable<String> createProduct(@Body Product product);
}
