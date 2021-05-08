package pt.ulisboa.tecnico.cmov.shopist.data.localSource;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.PantryDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.StoreDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.LocationEntity;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.PantryProductCrossRef;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.daos.ProductDao;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Store;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.StoreProductCrossRef;

@androidx.room.Database(
        entities = {
                Product.class,
                Pantry.class,
                PantryProductCrossRef.class,
                Store.class,
                StoreProductCrossRef.class,
                LocationEntity.class
        }, version = 8)
public abstract class ShopIstDatabase extends RoomDatabase {

    private static ShopIstDatabase instance = null;

    public abstract ProductDao productDao();
    public abstract PantryDao pantryDao();
    public abstract StoreDao storeDao();

    public static synchronized ShopIstDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ShopIstDatabase.class, "shop_ist_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
