package com.example.mobilehome.netWordkInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobilehome.R;
import com.google.android.material.snackbar.Snackbar;

public class OfflineActivity extends AppCompatActivity implements ConnectivityInfo.ConnectivityLisitiner {

    Button btnCheck;

    //unregister receiver
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        btnCheck = findViewById(R.id.btm);

        //check manually
        CheckinternetConnection();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckinternetConnection();
            }
        });
    }

    private void CheckinternetConnection() {
        boolean isConnected = ConnectivityInfo.isConnected();
        if (isConnected) {
            showSnackbar(isConnected);
        }

        if (!isConnected) {
            changeActivity();
        }
    }

    private void showSnackbar(boolean isConnected) {
        String msg;
        int color;

        if (isConnected) {
            msg = "You're Online..!";
            color = Color.WHITE;
        } else {
            msg = "You're OFline..!";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.RL), msg, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(color);

        snackbar.show();

    }

    private void changeActivity() {
        Intent intent = new Intent(this, Nonet.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityInfo connectivityInfo = new ConnectivityInfo();
        registerReceiver(connectivityInfo, intentFilter);

        MyApp.getInstance().setConnectivityLisitiner(this);
    }

 /*   @Override
    protected void onPause() {
        super.onPause();

        ConnectivityInfo connectivityInfo=new ConnectivityInfo();
        if(connectivityInfo!=null){
            unregisterReceiver(connectivityInfo);
        }
    }*/

    @Override
    public void OnNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            changeActivity();
        }
        showSnackbar(isConnected);

    }
}