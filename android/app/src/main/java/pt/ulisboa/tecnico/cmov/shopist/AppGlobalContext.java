package pt.ulisboa.tecnico.cmov.shopist;

import android.Manifest;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.shopist.dto.BeaconTime;
import pt.ulisboa.tecnico.cmov.shopist.dto.Coordinates;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;

public class AppGlobalContext extends Application implements SimWifiP2pManager.PeerListListener {

    public static final String TERMITE_TAG = "ShopIST";

    private static UUID uuid = null;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private Boolean inQueue = false;
    private BackendService mBackendService;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager mLocationManager;
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplicationContext(), getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    public static UUID getUUID() {
        return uuid;
    }

    public void startService() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new SimWifiP2pBroadcastReceiver(this);
        registerReceiver(mReceiver, filter);
        Intent intent = new Intent(this, SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mBackendService = BackendService.getInstance(getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void peersChanged() {
        if (mBound) {
            mManager.requestPeers(mChannel, this);
        }
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            if (device.deviceName.endsWith(TERMITE_TAG)) {
                if (!inQueue) {
                    LocalDateTime now = LocalDateTime.now();
                    Task<Location> task = getLastKnownLocation();
                    if(task != null) {
                        getLastKnownLocation().addOnSuccessListener(location -> {
                            if (location == null) {
                                return;
                            }
                            BeaconTime beaconTime = new BeaconTime(now.format(formatter), getCoordinates(location), null);
                            mBackendService.postInTime(beaconTime).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<UUID>() {
                                @Override
                                public void onNext(@NonNull UUID res) {
                                    inQueue = true;
                                    uuid = res;
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Peers Changed and beacon recognized", Toast.LENGTH_SHORT).show();
                        });

                    }
                }
                return;
            }
        }
        if (inQueue) {
            LocalDateTime now = LocalDateTime.now();
            Task<Location> task = getLastKnownLocation();
            if(task != null) {
                task.addOnSuccessListener(location -> {
                    if (location == null) {
                        return;
                    }
                    BeaconTime beaconTime = new BeaconTime(now.format(formatter), getCoordinates(location), uuid);
                    mBackendService.postOutTime(beaconTime).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver<UUID>() {
                        @Override
                        public void onNext(@NonNull UUID res) {
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                    inQueue = false;
                    uuid = null;
                    Toast.makeText(getApplicationContext(), "Peers Changed and beacon no longer in sight", Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Peers Changed but nothing happened", Toast.LENGTH_SHORT).show();
        }
    }

    private Task<Location> getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return fusedLocationClient.getLastLocation();
        }
        return null;
    }

    private Coordinates getCoordinates(Location location) {
        return new Coordinates(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onTerminate() {
        unbindService(mConnection);
        unregisterReceiver(mReceiver);
        super.onTerminate();
    }
}
