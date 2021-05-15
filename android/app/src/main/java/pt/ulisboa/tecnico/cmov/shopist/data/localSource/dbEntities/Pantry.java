package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters.LocationConverter;
import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

@Data
@Entity(tableName = "pantries")
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long pantryId;
    // TODO add uuid
    private String name;
    private String description;

    @TypeConverters(LocationConverter.class)
    private LocationWrapper locationWrapper;

    public boolean shared;

    public Pantry(String name, String description, LocationWrapper location) {
        this.name = name;
        this.description = description;
        this.locationWrapper = location;
        shared = false;
    }

    public Pantry(String name, String description) {
        this.name = name;
        this.description = description;
        shared = false;
    }
}
