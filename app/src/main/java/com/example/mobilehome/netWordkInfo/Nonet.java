package com.example.mobilehome.netWordkInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mobilehome.R;

public class Nonet extends AppCompatActivity implements ConnectivityInfo.ConnectivityLisitiner {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonet);

        //check manually
        CheckinternetConnection();
        Log.d("Check...","Manually check");

        button=findViewById(R.id.btnNetrefresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Nonet.this,OfflineActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void CheckinternetConnection() {
        boolean isConnected = ConnectivityInfo.isConnected();

       // showSnackbar(isConnected);
        if (isConnected) {
            Intent intent=new Intent(Nonet.this,OfflineActivity.class);
            startActivity(intent);
            finish();
        }
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void OnNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            Intent intent=new Intent(Nonet.this,OfflineActivity.class);
            startActivity(intent);
            finish();
        }
    }
}