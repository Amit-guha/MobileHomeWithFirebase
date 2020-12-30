package com.example.mobilehome.multifileupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fileshow extends AppCompatActivity {

    private RecyclerView mSHowView;
    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef;

    private List<MultiImage> upload;
    private ValueEventListener mDBListener;
    // private ArrayList<String> fileDoneList;

    FileShowAdapter adapter;
    private TextView tvUrl;

    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ArrayList<HashMap<String, String>> Imagelist = new ArrayList<HashMap<String, String>>();

    //Hashmap
    HashMap<String, String> hashMap = new HashMap<>();
    HashMap<String, String> map = new HashMap<>();

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileshow);

        //Current Id
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Currentuser = mAuth.getCurrentUser();
        String m = Currentuser.toString();


        tvUrl = findViewById(R.id.tvUrl);

        mSHowView = findViewById(R.id.mshowmultiplefile);
        //Firebase
        mStorageRef = FirebaseStorage.getInstance();

        //check
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        upload = new ArrayList<>();
        //fileDoneList=new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // layoutManager.setOrientation(this,LinearLayoutManager.VERTICAL,false);
        // mSHowView.setLayoutManager(new LinearLayoutManager(this));
        mSHowView.setLayoutManager(layoutManager);
        mSHowView.setHasFixedSize(true);


        final DatabaseReference reference = mDatabaseRef.child("Imagebox");
        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                        // System.out.println(snapshot.getChildrenCount());

                        hashMap = (HashMap<String, String>) dataSnapshot.getValue();

                        String mp = hashMap.get("key");
                        Toast.makeText(Fileshow.this, "" + mp, Toast.LENGTH_SHORT).show();
                        // System.out.println(mp);
                        // list.add(hashMap);
                        // System.out.println(list);

                        if (mp != null) {
                            reference.child(mAuth.getCurrentUser().getUid()).child(mp).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    hashMap = (HashMap<String, String>) snapshot.getValue();
                                    String text = hashMap.get("text");
                                    list.add(hashMap);


                                    //  for (int i = 0; i < snapshot.child("Url").getChildrenCount(); i++) {
                                    Log.d("count", String.valueOf(snapshot.child("Url").getChildrenCount()));
                                    map = (HashMap<String, String>) snapshot.child("Url").getValue();
                                    // String url = map.get("link" + i);
                                    Imagelist.add(map);
                                    // }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                            Toast.makeText(Fileshow.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    adapter = new FileShowAdapter(Fileshow.this, list, Imagelist);
                    mSHowView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });



/*        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                // hashMap = (HashMap<String, String>) snapshot.getValue();
                // String text = hashMap.get("text");
                //  String userId=hashMap.get("UserId");
                //  String key=hashMap.get("key");

                // Toast.makeText(Fileshow.this, ""+userId+"\n"+key, Toast.LENGTH_SHORT).show();
*//*

                if(mAuth.getCurrentUser().getUid().equals(userId)){
                    reference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            hashMap = (HashMap<String, String>) snapshot.getValue();
                            String text = hashMap.get("text");
                            // String userId=hashMap.get("UserId");
                            list.add(hashMap);
                            for (int i = 0; i < snapshot.child("Url").getChildrenCount(); i++) {
                                Log.d("count", String.valueOf(snapshot.child("Url").getChildrenCount()));
                                map = (HashMap<String, String>) snapshot.child("Url").getValue();
                                String url = map.get("link" + i);
                                // Log.d("urlimgae", String.valueOf(url));
                                // tvUrl.append(url + "\n");
                                Imagelist.add(map);
                                // Toast.makeText(Fileshow.this, "" + Imagelist.get(i).get("link" + i), Toast.LENGTH_SHORT).show();
                            }

                            adapter = new FileShowAdapter(Fileshow.this, list, Imagelist);
                            mSHowView.setAdapter(adapter);

                        }

                        // String imgur= (String) snapshot.child("Url").getValue();
                        // Toast.makeText(Fileshow.this, "" + text + url, Toast.LENGTH_SHORT).show();

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }*//*
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        //.child(mAuth.getCurrentUser().getUid())


/*        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    hashMap = (HashMap<String, String>) dataSnapshot.getValue();
                    String key=hashMap.get("key");
                    Toast.makeText(Fileshow.this, ""+key, Toast.LENGTH_SHORT).show();
                    //String text = hashMap.get("text");
                   // String userId=hashMap.get("UserId");
                   // String uid=mAuth.getCurrentUser().getUid();

                    list.add(hashMap);
                    for (int i = 0; i < snapshot.child("Url").getChildrenCount(); i++) {
                        Log.d("count", String.valueOf(snapshot.child("Url").getChildrenCount()));
                        map = (HashMap<String, String>) snapshot.child("Url").getValue();
                        String url = map.get("link" + i);
                        Imagelist.add(map);
                        System.out.println(Imagelist);

                    }

                    adapter = new FileShowAdapter(Fileshow.this, list, Imagelist);
                    mSHowView.setAdapter(adapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }
}