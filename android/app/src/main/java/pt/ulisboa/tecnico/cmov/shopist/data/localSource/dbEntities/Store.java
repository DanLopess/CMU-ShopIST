package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters.LocationConverter;
import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

@Data
@Entity(tableName = "stores")
public class Store {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long storeId;

    private String name;
    private String description;

    @TypeConverters(LocationConverter.class)
    private LocationWrapper locationWrapper;

    public Store(String name) {
        this.name = name;
    }

    public Store(String name, String description, LocationWrapper location) {
        this.name = name;
        this.locationWrapper = location;
        this.description = description;
    }
}
