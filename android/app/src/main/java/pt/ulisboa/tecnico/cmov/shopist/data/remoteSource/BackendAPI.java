package pt.ulisboa.tecnico.cmov.shopist.data.remoteSource;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import retrofit2.Call;
import pt.ulisboa.tecnico.cmov.shopist.data.pojo.BeaconTime;
import pt.ulisboa.tecnico.cmov.shopist.data.pojo.Coordinates;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendAPI {
    String BASE_URL = "http://daniellopes.ddns.net/";

    String pantryUrl = "/api/pantry";

    @POST("/api/store/")
    @Headers("Cache-Control: no-cache")
    Observable<Store> postStore(@Body Store store);

    @GET(pantryUrl)
    Observable<List<Pantry>> getPantries();

    @GET("/api/product/")
    Observable<List<Product>> getProducts();

    @GET(pantryUrl)
    Call<PantryDto> getPantryByUUID(@Query("uuid") String uuid);

    @POST(pantryUrl)
    @Headers("Cache-Control: no-cache")
    Single<String> createPantry(@Body PantryDto pantryDto);

    @PUT(pantryUrl)
    @Headers("Cache-Control: no-cache")
    Call<PantryDto> updatePantry(@Body PantryDto pantryDto);

    @POST("/api/beacon/in")
    @Headers("Cache-Control: no-cache")
    Observable<UUID> postInTime(@Body BeaconTime beaconTime);

    @POST("/api/beacon/out")
    void postOutTime(@Body BeaconTime beaconTime);

    @GET("/api/beacon/queueTime")
    Observable<Long> getQueueTime(@Body Coordinates coordinates);
}
