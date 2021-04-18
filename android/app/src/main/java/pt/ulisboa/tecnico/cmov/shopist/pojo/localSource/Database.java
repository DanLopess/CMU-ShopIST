package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource;

import androidx.room.RoomDatabase;

import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.PantryDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.ProductImageDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.ProductDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.ProductImage;

@androidx.room.Database(entities = {Product.class, ProductImage.class, Pantry.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract ProductImageDao productImageDao();
    public abstract PantryDao pantryDao();
}
