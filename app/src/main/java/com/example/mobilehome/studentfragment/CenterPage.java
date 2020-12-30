package com.example.mobilehome.studentfragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.UpDelCheck;
import com.example.mobilehome.student.StudentUpdateCheck;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CenterPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ViewPager mViewPager;
    TabLayout mTablayout;
    MyFragMentPageAdapter mAdapter;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_page);

        //Change the status bar background color
        Window w= CenterPage.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(CenterPage.this,R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        mAuth=FirebaseAuth.getInstance();
        mTablayout=findViewById(R.id.tablayout);
        mViewPager=findViewById(R.id.viewpageer);

        //---ADDING ADAPTER FOR FRAGMENTS IN VIEW PAGER----
        mAdapter=new MyFragMentPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);

        mReference= FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(CenterPage.this);
        builder.setMessage("Really Exit ??");
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",new CenterPage.MyListener());
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }

    public class MyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            StartNew();
        }
    }

    private void StartNew() {
        Intent intent=new Intent(CenterPage.this, UpDelCheck.class);
        startActivity(intent);
        finish();
    }
}
