package pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "locations")
public class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long locationId;

    public Double latitude;
    public Double longitude;

    public LocationEntity(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
