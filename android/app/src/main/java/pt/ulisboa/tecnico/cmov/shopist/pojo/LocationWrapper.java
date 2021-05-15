package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.location.Location;
import android.location.LocationManager;

import lombok.Data;

@Data
public class LocationWrapper {
    private Double latitude;
    private Double longitude;

    public LocationWrapper() {}

    public LocationWrapper(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationWrapper(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public Location toLocation() {
        if (longitude != null && latitude != null) {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLongitude(longitude);
            location.setLatitude(latitude);
            return location;
        }
        return null;
    }
}
