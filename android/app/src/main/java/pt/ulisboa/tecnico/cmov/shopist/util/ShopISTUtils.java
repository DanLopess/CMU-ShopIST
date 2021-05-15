package pt.ulisboa.tecnico.cmov.shopist.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ShopISTUtils {
    private ShopISTUtils() {}

    public static List<Address> getAddressesByLocation(Context context, Location location) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location
                    .getLongitude(), 1);
            if (addresses == null) {
                addresses = new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("ShopISTUtils", "Failed to obtain addresses by location");
        }
        return addresses;
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static Location latLngToLocation(LatLng latLng) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLongitude(latLng.longitude);
        location.setLatitude(latLng.latitude);
        return location;
    }

    public static String joinStringsByComma(String... strings) {
        return String.join(", ", strings);
    }
}
