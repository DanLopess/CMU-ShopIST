package pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.StoreProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.PantryProduct;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductAndPrincipalImage;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.ProductWithImages;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations.StoreProduct;

@Dao
public interface ProductDao {

    //============= Products =============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM products")
    Observable<List<Product>> getProducts();

    @Query("SELECT * FROM products")
    Observable<List<ProductAndPrincipalImage>> getProductsAndImage();

    @Query("SELECT * FROM products where productId == :id")
    Observable<List<ProductWithImages>> getProductWithImages(Long id);

    @Query("SELECT * FROM products p JOIN productsImages pi where p.productId == :id and p.imageId == pi.imageId")
    Observable<ProductAndPrincipalImage> getProductAndImage(Long id);

    //============= PantryProducts =============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPantryProduct(PantryProductCrossRef pantry_product);

    @Delete
    void deletePantryProduct(PantryProductCrossRef pantryProd);

    @Query("SELECT * FROM pantry_product pp JOIN products p where pantryId == :id and pp.productId == p.productId")
    Observable<List<PantryProduct>> getPantryProducts(Long id);

    @Query("SELECT COUNT(*) FROM pantry_product WHERE pantryId == :id")
    Observable<Integer> getPantrySize(Long id);

    //============= StoreProducts =============

    @Query("SELECT * FROM store_product sp JOIN products p where storeId == :id and sp.productId == p.productId")
    Observable<List<StoreProduct>> getStoreProducts(Long id);

    @Query("SELECT COUNT(*) FROM store_product WHERE storeId == :id")
    Observable<Integer> getStoreSize(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStoreProduct(StoreProductCrossRef store_product);

    @Delete
    void deleteStoreProduct(StoreProductCrossRef storeProd);
}
