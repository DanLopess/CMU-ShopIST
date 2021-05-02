package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters.LocationEntityConverter;

@Data
@Entity(tableName = "pantries", foreignKeys = {
        @ForeignKey(
                entity = LocationEntity.class,
                parentColumns = "locationId",
                childColumns = "location"
        )
})
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long pantryId;

    public String name;
    public String description;

    @TypeConverters(LocationEntityConverter.class)
    private LocationEntity location;

    public boolean shared;

    public Pantry(String name, String description, LocationEntity location) {
        this.name = name;
        this.description = description;
        this.location = location;
        shared = false;
    }
}
