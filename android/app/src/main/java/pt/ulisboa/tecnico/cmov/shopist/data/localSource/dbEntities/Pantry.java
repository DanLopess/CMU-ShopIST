package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import lombok.Data;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters.LocationConverter;
import pt.ulisboa.tecnico.cmov.shopist.dto.PantryDto;
import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

@Data
@Entity(tableName = "pantries")
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long pantryId;

    public String uuid;
    private String name;
    private String description;
    public boolean shared;
    public boolean isOwner;

    @TypeConverters(LocationConverter.class)
    private LocationWrapper locationWrapper;

    public Pantry(String name, String description, LocationWrapper location) {
        this.name = name;
        this.description = description;
        this.locationWrapper = location;
        shared = false;
        isOwner = true;
    }

    public Pantry(String name, String description) {
        this.name = name;
        this.description = description;
        shared = false;
        isOwner = true;
    }

    public Pantry(PantryDto pantryDto) {
        this.uuid = pantryDto.getUuid();
        this.name = pantryDto.getName();
        this.description = pantryDto.getDescription();
        this.locationWrapper = new LocationWrapper(pantryDto.getLocation().getLatitude(), pantryDto.getLocation().getLongitude());
        this.isOwner = true;
        this.shared = true;
    }
}
