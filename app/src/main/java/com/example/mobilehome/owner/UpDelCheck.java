package com.example.mobilehome.owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.actualstudent.ChatLogin;
import com.example.mobilehome.messaging.SendRequestProfile;
import com.example.mobilehome.multifileupload.Fileshow;
import com.example.mobilehome.multifileupload.ImageUpload;
import com.example.mobilehome.student.SearchAnotherWay;
import com.example.mobilehome.student.StudentUpdateCheck;
import com.example.mobilehome.studentfragment.CenterPage;

public class UpDelCheck extends AppCompatActivity {
    private Button upBtn, ViewBtn, delBtn;
    private LinearLayout LinUpload, LinStaus, LinMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_del_check);


        //Add custom toolbar
        Toolbar toolbar = findViewById(R.id.tv_toolbar_updel);
        setSupportActionBar(toolbar);


        //adding back button
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Change the status bar background color---
        Window w = UpDelCheck.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(UpDelCheck.this, R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        LinUpload = findViewById(R.id.LinUpload);
        LinStaus = findViewById(R.id.LinStatus);
        LinMsg = findViewById(R.id.LinMsg);

        LinUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpDelCheck.this, UploadStatus.class));
                 //startActivity(new Intent(UpDelCheck.this, ImageUpload.class));
            }
        });

        LinStaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpDelCheck.this, CheckActivity.class));
               // startActivity(new Intent(UpDelCheck.this,Fileshow.class));
            }
        });

        LinMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(UpDelCheck.this, CenterPage.class));
                } catch (Exception e) {
                    Toast.makeText(UpDelCheck.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*upBtn=findViewById(R.id.uploadBtn);
        ViewBtn=findViewById(R.id.viewBtn);
        delBtn=findViewById(R.id.deleteBtn);

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpDelCheck.this,UploadStatus.class));
            }
        });

        ViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UpDelCheck.this,CheckActivity.class));
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(UpDelCheck.this, CenterPage.class));
                }catch (Exception e){
                    Toast.makeText(UpDelCheck.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
*/
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
