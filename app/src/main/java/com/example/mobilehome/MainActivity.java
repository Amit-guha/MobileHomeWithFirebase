package com.example.mobilehome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilehome.actualstudent.ChatLogin;
import com.example.mobilehome.actualstudent.StudentCenterPage;
import com.example.mobilehome.multifileupload.ImageUpload;
import com.example.mobilehome.netWordkInfo.OfflineActivity;
import com.example.mobilehome.owner.UpDelCheck;
import com.example.mobilehome.owner.UploadStatus;
import com.example.mobilehome.phoneverification.LoginPhone;
import com.example.mobilehome.phoneverification.RegestrationPhone;
import com.example.mobilehome.student.LocationSearch;
import com.example.mobilehome.student.StudentUpdateCheck;

public class MainActivity extends AppCompatActivity {
    // private TextView tvStudent,tvOwner,tv_chatBoard;
    private LinearLayout mFindHome, mRentHome, mChatboard;
    private TextView tvcorona, tvdeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvcorona = findViewById(R.id.tvCorona);
        tvcorona.setSelected(true);

        tvdeveloper = findViewById(R.id.tvdeveloper);
        tvdeveloper.setSelected(true);

        //Change the status bar background color
        Window w = MainActivity.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

/*
        tvStudent=findViewById(R.id.textView);
        tvOwner=findViewById(R.id.textView3);
        tv_chatBoard=findViewById(R.id.tv_chatboard);

        tvStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent
                Intent intent=new Intent(MainActivity.this, LocationSearch.class);
                startActivity(intent);
            }
        });

        tvOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // Intent intent=new Intent(MainActivity.this, EmailPassword.class);
               Intent intent=new Intent(MainActivity.this, LoginPhone.class);
               startActivity(intent);
            }
        });


        tv_chatBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatLogin.class));
                //startActivity(new Intent(MainActivity.this, OfflineActivity.class));
               // startActivity(new Intent(MainActivity.this, ImageUpload.class));
            }
        });*/

        //Fullscreen Mode
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );*/

        mFindHome = findViewById(R.id.FindHome);
        mRentHome = findViewById(R.id.RentHome);
        mChatboard = findViewById(R.id.chatboard);


        mFindHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSearch.class);
                startActivity(intent);
            }
        });

        mRentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPhone.class);
                startActivity(intent);
            }
        });


        mChatboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatLogin.class));
            }
        });
    }
}
