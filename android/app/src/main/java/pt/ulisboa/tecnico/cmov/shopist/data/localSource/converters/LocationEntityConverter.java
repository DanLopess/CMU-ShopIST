package pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.LocationEntity;

public class LocationEntityConverter {

    @TypeConverter
    public static LocationEntity fromString(String locationJson) {
        if (locationJson == null) return null;

        Gson gson = new Gson();
        LocationEntity locationEntity = gson.fromJson(locationJson, LocationEntity.class);
        return locationEntity;
    }

    @TypeConverter
    public static String toString(LocationEntity locationEntity) {
        if (locationEntity == null) return null;

        Gson gson = new Gson();
        String locationJson = gson.toJson(locationEntity);
        return locationJson;
    }
}
