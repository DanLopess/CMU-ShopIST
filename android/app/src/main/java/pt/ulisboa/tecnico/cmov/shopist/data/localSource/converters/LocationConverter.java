package pt.ulisboa.tecnico.cmov.shopist.data.localSource.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import pt.ulisboa.tecnico.cmov.shopist.pojo.LocationWrapper;

public class LocationConverter {
    @TypeConverter
    public static LocationWrapper fromString(String locationJson) {
        if (locationJson == null) return null;

        Gson gson = new Gson();
        return gson.fromJson(locationJson, LocationWrapper.class);
    }

    @TypeConverter
    public static String toString(LocationWrapper location) {
        if (location == null) return null;

        Gson gson = new Gson();
        return gson.toJson(location);
    }
}
