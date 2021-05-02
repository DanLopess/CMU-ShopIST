package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.UUID;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters.LocationEntityConverter;

@Data
@Entity(tableName = "stores", foreignKeys = {
        @ForeignKey(
                entity = LocationEntity.class,
                parentColumns = "locationId",
                childColumns = "location"
        )
})
public class Store {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long storeId;

    private String name;

    @TypeConverters(LocationEntityConverter.class)
    private LocationEntity location;

    public Store(String name, LocationEntity location) {
        this.name = name;
        this.location = location;
    }
}
