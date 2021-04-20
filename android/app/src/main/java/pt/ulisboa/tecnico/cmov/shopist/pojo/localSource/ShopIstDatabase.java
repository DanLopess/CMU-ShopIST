package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.PantryDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.ProductImageDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.daos.ProductDao;
import pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities.ProductImage;

@androidx.room.Database(entities = {Product.class, ProductImage.class, Pantry.class, PantryProductCrossRef.class}, version = 1)
public abstract class ShopIstDatabase extends RoomDatabase {

    private static ShopIstDatabase instance = null;

    public abstract ProductDao productDao();
    public abstract ProductImageDao productImageDao();
    public abstract PantryDao pantryDao();

    public static synchronized ShopIstDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ShopIstDatabase.class, "shop_ist_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
