package com.example.mobilehome.netWordkInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityInfo extends BroadcastReceiver {

    public static ConnectivityLisitiner connectivityLisitiner;

    public ConnectivityInfo() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (connectivityLisitiner != null) {
            connectivityLisitiner.OnNetworkConnectionChanged(isConnected);
        }
    }

    //ceate on method to check manualy internet connection
    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    //Create an interface
    public interface ConnectivityLisitiner {
        void OnNetworkConnectionChanged(boolean isConnected);
    }
}
