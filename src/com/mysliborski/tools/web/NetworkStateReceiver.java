package com.mysliborski.tools.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

import static com.mysliborski.tools.helper.LogHelper.log;

public class NetworkStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        log("Network connectivity change");
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            log("Connection restored");
            NetworkStateNotifier.notifyObservers();
        }
    }

    public interface NetworkStateObserver {
        void connectionRestored();
    }

    public static class NetworkStateNotifier {

        static Set<NetworkStateObserver> observers = new HashSet<NetworkStateObserver>();

        public static void registerObserver(NetworkStateObserver observer) {
            observers.add(observer);
        }

        static void notifyObservers() {
            for (NetworkStateObserver observer : observers) {
                observer.connectionRestored();
            }
        }

    }
}
