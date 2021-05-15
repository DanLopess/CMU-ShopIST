package pt.ulisboa.tecnico.cmov.shopist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.util.PermissionUtils;
import pt.ulisboa.tecnico.cmov.shopist.util.ShopISTUtils;

import static pt.ulisboa.tecnico.cmov.shopist.util.Constants.LOCATION_EXTRA;

public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    // Location
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location resultLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 17;

    // Views
    private View mapView;
    private TextView selectedLocationTextView;

    // Variables
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initViews();
        initialize();
    }

    private void initViews() {
        Button submitLocationButton = findViewById(R.id.button_confirmLocation);
        selectedLocationTextView = findViewById(R.id.tv_display_marker_location);
        //materialSearchBar = findViewById(R.id.searchBar);
        submitLocationButton.setOnClickListener(v -> returnResult());
    }

    private void initialize() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        } else {
            Log.e(this.toString(), "Failed to obtain map fragment.");
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        //enable location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);

        mMap.setOnMarkerDragListener(createOnMarkerDragListener());

        //move location button to the required position and adjust params such margin
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 60, 500);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //if task is successful means the gps is enabled so go and get device location amd move the camera to that location
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        //if task failed means gps is disabled so ask user to enable gps
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private GoogleMap.OnMarkerDragListener createOnMarkerDragListener() {
        return new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker arg0) {
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker arg0) {
                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                resultLocation = ShopISTUtils.latLngToLocation(arg0.getPosition());
                setLocationOnText();
            }

            @Override
            public void onMarkerDrag(@NonNull Marker arg0) {
                Log.i("System out", "onMarkerDrag...");
            }
        };
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mFusedLocationProviderClient != null) {
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        resultLocation = task.getResult();
                        if (resultLocation == null) {
                            askForNewLocation();
                        }
                        moveCameraToLocation();
                        setMapMarkerOnResultLocation();
                        setLocationOnText();
                    } else {
                        Toast.makeText(MapsActivity.this, "Unable to get last location ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            // leave the activity if location is not permitted (could change to only searchable)
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void returnResult() {
        Intent intent = getIntent();
        intent.putExtra(LOCATION_EXTRA, resultLocation);
        setResult(RESULT_OK, intent);
        finish();
    }

    // Auxiliary methods

    @SuppressLint("MissingPermission")
    private void askForNewLocation() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                resultLocation = locationResult.getLastLocation();
                //remove location updates in order not to continues check location unnecessarily
                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, null);
    }

    private void moveCameraToLocation() {
        LatLng latLng = ShopISTUtils.locationToLatLng(resultLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    private void setMapMarkerOnResultLocation() {
        LatLng latLng = ShopISTUtils.locationToLatLng(resultLocation);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));

    }

    private void setLocationOnText() {
        List<Address> addresses = ShopISTUtils.getAddressesByLocation(getBaseContext(), resultLocation);
        if (addresses.size() > 0) {
            String locationInfo = ShopISTUtils.joinStringsByComma(addresses.get(0).getThoroughfare(),
                    addresses.get(0).getLocality(),
                    addresses.get(0).getPostalCode());

            selectedLocationTextView.setText(locationInfo);
        }
    }
}