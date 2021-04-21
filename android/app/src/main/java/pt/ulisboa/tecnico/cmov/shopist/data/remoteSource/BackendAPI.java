package pt.ulisboa.tecnico.cmov.shopist.data.remoteSource;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BackendAPI {
    String BASE_URL = "http://daniellopes.ddns.net/";

    @POST("/api/store/")
    Observable<Store> postStore(@Body Store store);

    @GET("/api/pantry/")
    Observable<List<Pantry>> getPantries();

    @GET("/api/product/")
    Observable<List<Product>> getProducts();
}
