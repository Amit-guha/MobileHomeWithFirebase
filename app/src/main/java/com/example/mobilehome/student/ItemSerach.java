package com.example.mobilehome.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemSerach extends AppCompatActivity implements StudenAdapter.OnItemCLICKLISITINER,SimilarDataAdapter.ONShopiFyClikLisitiner {
    private RecyclerView itemRecyclerview, similarRecyclerview;

    private ArrayList<Upload> mUploads;
    private ArrayList<Upload> mSimilarSHow;


    private StudenAdapter mAdapter;
    private SimilarDataAdapter adapter;


    private DatabaseReference mDatabaseRef;
    private LinearLayout LinShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_serach);

        //Add custom toolbar
        Toolbar toolbar=findViewById(R.id.tvsearchRsult);
        setSupportActionBar(toolbar);

        //adding back button
        if(getSupportActionBar()!=null){

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        //Change the status bar background color
        Window w=ItemSerach.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(ItemSerach.this,R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        itemRecyclerview = findViewById(R.id.item_RecyclerView);
        similarRecyclerview = findViewById(R.id.similarRecyclerview);

        mUploads = new ArrayList<>();
        mSimilarSHow = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Gopalganj");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        itemRecyclerview.setLayoutManager(layoutManager);


        itemRecyclerview.setHasFixedSize(true);


        mAdapter = new StudenAdapter(this, mUploads);
        itemRecyclerview.setAdapter(mAdapter);

        //2nd
       // similarRecyclerview.setLayoutManager(layoutManager);
        final LinearLayoutManager manager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);

        similarRecyclerview.setLayoutManager(manager);
        similarRecyclerview.setHasFixedSize(true);

        adapter = new SimilarDataAdapter(this, mSimilarSHow);
        similarRecyclerview.setAdapter(adapter);



        mAdapter.setOnItemClickListener(this);
        adapter.setOnItemClickListener(this);
        //similarAdapter.setonShopifyclick((SimilarAdapter.onItemClickShopifyLisitiner) this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String PP = bundle.getString("p");

            final String tt = bundle.getString("t");
            final long taka = Long.parseLong(tt);

            final String tt2 = bundle.getString("t2");
            final long taka2 = Long.parseLong(tt2);

            final String gg = bundle.getString("g");

            mDatabaseRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean fun = false;
                    mUploads.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Upload upload = dataSnapshot.getValue(Upload.class);

                        String money = upload.getmTaka();
                        long mon = Long.parseLong(money);

                        String place = upload.getmPlace();
                        String gender = upload.getmGender();
                       // String imagedata=upload.getmImgeUrl();

                        //Chcek any of them are empty or not
                        // if(!TextUtils.isEmpty(PP) && !TextUtils.isEmpty(tt) &&!TextUtils.isEmpty(gg) ){
                        //Query part-----------customize all data
                        if (mon >= taka && mon <= taka2 && PP.equals(place) && gg.equals(gender)) {
                            Upload upload1 = dataSnapshot.getValue(Upload.class);
                            upload1.setmKey(upload1.getmKey());
                            mUploads.add(upload);
                            fun = true;
                           // Toast.makeText(ItemSerach.this, "" + upload1.getmTaka(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(ItemSerach.this, "Loading...", Toast.LENGTH_SHORT).show();
                        }

                        //  }
                    }

                    if (fun == false) {
                        Toast.makeText(ItemSerach.this, "data not found ", Toast.LENGTH_SHORT).show();

                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(ItemSerach.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            //for similar Data..................
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean value = false;
                    mSimilarSHow.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Upload up = dataSnapshot.getValue(Upload.class);

                        String money = up.getmTaka();
                        long mon = Long.parseLong(money);

                        String place = up.getmPlace();
                        String gender = up.getmGender();

                        if ((mon >= taka && mon <= taka2) && (gg!=null && gg.equals(gender) && !PP.equals(place))) {
                            Upload upload1 = dataSnapshot.getValue(Upload.class);
                            upload1.setmKey(upload1.getmKey());
                            mSimilarSHow.add(up);
                            value = true;
                          //  Toast.makeText(ItemSerach.this, "" + upload1.getmTaka(), Toast.LENGTH_SHORT).show();
                           // Toast.makeText(ItemSerach.this, "Loading...", Toast.LENGTH_SHORT).show();
                        }

                    }

                    if (value == false) {
                        Toast.makeText(ItemSerach.this, "data not found", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ItemSerach.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onItemClick(int position) {
        Upload SelectedItem = mUploads.get(position);
        String place = SelectedItem.getmPlace();
        String HomeAddress=SelectedItem.getmHomeAddress();
        String userId = SelectedItem.getmUserId();
        String imageData=SelectedItem.getmImgeUrl();
        String Room=SelectedItem.getmRoom();
        String Gender=SelectedItem.getmGender();
        String Tk=SelectedItem.getmTaka();


        Intent i = new Intent(ItemSerach.this, ContactDetails.class);
        i.putExtra("Place", place);
        i.putExtra("Hm_address",HomeAddress);
        i.putExtra("user_id", userId);
        i.putExtra("profileimag",imageData);
        i.putExtra("Gender",Gender);
        i.putExtra("Tk",Tk);
        i.putExtra("Room",Room);

       // Toast.makeText(this, "" + userId, Toast.LENGTH_SHORT).show();
        startActivity(i);
       // Toast.makeText(this, "" + position + " " + place, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnITEMCLICK(int position) {
        Upload SelectedItem = mSimilarSHow.get(position);
        String place = SelectedItem.getmPlace();
        String HomeAddress=SelectedItem.getmHomeAddress();
        String userId = SelectedItem.getmUserId();
        String userImage=SelectedItem.getmImgeUrl();
        String Room=SelectedItem.getmRoom();
        String Gender=SelectedItem.getmGender();
        String Tk=SelectedItem.getmTaka();

        Intent i = new Intent(ItemSerach.this, ContactDetails.class);
        i.putExtra("Place", place);
        i.putExtra("Hm_address",HomeAddress);
        i.putExtra("user_id", userId);
        i.putExtra("profileimag",userImage);
        i.putExtra("Gender",Gender);
        i.putExtra("Tk",Tk);
        i.putExtra("Room",Room);

       // Toast.makeText(this, "" + userId, Toast.LENGTH_SHORT).show();
        startActivity(i);
       // Toast.makeText(this, "" + position + " " + place, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
