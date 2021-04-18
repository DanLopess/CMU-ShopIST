package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.ProductImage;

@Dao
public interface ProductImageDao {

    @Query("SELECT * FROM productsImages WHERE productId == :pid")
    Observable<List<ProductImage>> getImagesByProduct(String pid);
}
