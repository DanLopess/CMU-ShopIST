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

import androidx.core.app.ActivityCompat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.shopist.data.pojo.BeaconTime;
import pt.ulisboa.tecnico.cmov.shopist.data.pojo.Coordinates;
import pt.ulisboa.tecnico.cmov.shopist.data.remoteSource.BackendService;

public class AppGlobalContext extends Application implements SimWifiP2pManager.PeerListListener {

    public static final String TERMITE_TAG = "ShopIST";

    private UUID uuid = null;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private Boolean inQueue = false;
    private BackendService backendService = BackendService.getInstance();
    private LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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
                    Timestamp now = new Timestamp(new Date().getTime());
                    Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location == null) {
                        continue;
                    }
                    BeaconTime beaconTime = new BeaconTime(now, getCoordinates(location), null);
                    backendService.postInTime(beaconTime).observeOn(AndroidSchedulers.mainThread()).subscribe(uuid -> {
                       this.uuid = uuid;
                    });
                    Toast.makeText(getApplicationContext(), "Peers Changed and beacon recognized", Toast.LENGTH_SHORT).show();
                    inQueue = true;
                }
                return;
            }
        }
        if (inQueue) {
            Timestamp now = new Timestamp(new Date().getTime());
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null) {
                return;
            }
            BeaconTime beaconTime = new BeaconTime(now, getCoordinates(location), null);
            backendService.postOutTime(beaconTime);
            Toast.makeText(getApplicationContext(), "Peers Changed and beacon no longer in sight", Toast.LENGTH_SHORT).show();
            inQueue = false;
        } else {
            Toast.makeText(getApplicationContext(), "Peers Changed but nothing happened", Toast.LENGTH_SHORT).show();
        }
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
