package pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductWithImages;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    Observable<List<Product>> getProducts();

    @Query("SELECT * FROM products")
    Observable<List<ProductAndPrincipalImage>> getProductsAndImage();

    @Query("SELECT * FROM products where productId == :id")
    Observable<List<ProductWithImages>> getProductWithImages(Long id);

    @Query("SELECT * FROM products where productId == :id")
    Observable<List<ProductAndPrincipalImage>> getProductAndImage(Long id);

    @Query("SELECT * FROM pantry_product where pantryId == :id")
    Observable<List<PantryProduct>> getPantryProducts(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPantryProduct(PantryProductCrossRef pantry_product);

}
