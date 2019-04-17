package com.lpk806studio.ive_st_cafe.Model;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Check_network {

    boolean connect = false;

    public Check_network(ConnectivityManager connectivityManager) {

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connect = true;
        } else {
            connect = false;
        }

    }

    public boolean get_network_state() {
        return connect;
    }
}
