package pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductWithImages;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    Observable<List<Product>> getProducts();

    @Query("SELECT * FROM products")
    Observable<List<ProductAndPrincipalImage>> getProductsAndImage();

    @Query("SELECT * FROM products where productId == :id")
    Observable<List<ProductWithImages>> getProductWithImages(String id);

    @Query("SELECT * FROM products where productId == :id")
    Observable<List<ProductAndPrincipalImage>> getProductAndImage(String id);
}
