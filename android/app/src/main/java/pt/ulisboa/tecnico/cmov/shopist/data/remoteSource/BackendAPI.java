package pt.ulisboa.tecnico.cmov.shopist.data.remoteSource;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendAPI {
    //String BASE_URL = "http://daniellopes.ddns.net/";

    String BASE_URL = "http://MBP-de-Daniel.ubnt.lopes:8999";
    String pantryUrl = "/api/pantry";

    @POST("/api/store/")
    Observable<Store> postStore(@Body Store store);

    @GET(pantryUrl)
    Observable<List<Pantry>> getPantries();

    @GET("/api/product/")
    Observable<List<Product>> getProducts();

    @GET(pantryUrl)
    Call<PantryDto> getPantryByUUID(@Query("uuid") String uuid);

    @POST(pantryUrl)
    Call<PantryDto> createPantry(@Body PantryDto pantryDto);

    @PUT(pantryUrl)
    Call<PantryDto> updatePantry(@Body PantryDto pantryDto);
}
