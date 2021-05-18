package pt.ulisboa.tecnico.cmov.shopist;


import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class ProductListActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected Long myId;
    protected GoogleMap mMap;
    protected MapView mapView;
    protected Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMap();
    }

    protected void initializeMap() {
        mapView = findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(new Bundle());
            mapView.getMapAsync(this);
            mapView.onResume();
        } else {
            Log.e(this.toString(), "Failed to obtain map view.");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Location location = getListLocation();
        if (location != null) {
            mapView.setVisibility(View.VISIBLE);
            mMap = googleMap;
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(loc)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

            //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // TODO why does it need clicks to load the full map?
        } else {
            mapView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.getMapAsync(this);
            mapView.onResume();
        }
        super.onResume();
    }

    public abstract Location getListLocation();
}
