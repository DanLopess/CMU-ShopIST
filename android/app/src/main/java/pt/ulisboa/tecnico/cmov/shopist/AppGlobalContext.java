package pt.ulisboa.tecnico.cmov.shopist;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;

public class AppGlobalContext extends Application implements SimWifiP2pManager.PeerListListener {

    public static final String TERMITE_TAG = "ShopIST";

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private Boolean inQueue = false;

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
                    //TODO timestamp to enter queue and if possible tell server that entered queue (anonym)
                    Toast.makeText(getApplicationContext(), "Peers Changed and beacon recognized", Toast.LENGTH_SHORT).show();
                    inQueue = true;
                }
                return;
            }
        }
        if (inQueue) {
            Toast.makeText(getApplicationContext(), "Peers Changed and beacon no longer in sight", Toast.LENGTH_SHORT).show();
            //TODO timestamp to exit queue and if possible tell server that exited queue (anonym)
            //TODO calculate difference in timestamps and send to server when possible
            inQueue = false;
        } else {
            Toast.makeText(getApplicationContext(), "Peers Changed but nothing happened", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTerminate() {
        unbindService(mConnection);
        unregisterReceiver(mReceiver);
        super.onTerminate();
    }
}
