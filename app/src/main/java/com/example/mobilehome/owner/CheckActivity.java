package com.example.mobilehome.owner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity  implements UploadAdapter.OnItemClickListener {

    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef,mDatabaseSecond;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerview;
    private ArrayList<Upload> mUploads;
    private ArrayList<UploadImage1>Second;
    private ValueEventListener mDBListener;

    private UploadAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        //Change the status bar background color
        Window w= CheckActivity.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(CheckActivity.this,R.color.white));


        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);





        mRecyclerview=findViewById(R.id.mRecylerView);
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null) {

            //Initialize of  Storage && database Reference
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Gopalganj");
            mStorageRef = FirebaseStorage.getInstance();

          //  mDatabaseSecond=FirebaseDatabase.getInstance().getReference("Image2");
        }

        mUploads=new ArrayList<>();
        Second=new ArrayList<>();

       // mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        int Number_Of_Colums=2;
        LinearLayoutManager layoutManager = new GridLayoutManager(this, Number_Of_Colums);
        mRecyclerview.setLayoutManager(layoutManager);

        mRecyclerview.setHasFixedSize(true);

       // mAdapter=new UploadAdapter(this,mUploads,Second);
        mAdapter=new UploadAdapter(this,mUploads);
        mRecyclerview.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(this);



        //First iamge
        //mDatabaseRef.child(mAuth.getCurrentUser().getUid()).
       mDBListener= mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();;

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Upload upload=dataSnapshot.getValue(Upload.class);
                    String userId=upload.getmUserId();

                    if(userId.equals(mAuth.getCurrentUser().getUid())){
                        Upload upload1=dataSnapshot.getValue(Upload.class);
                       // upload1.setmKey(upload1.getmKey());
                       // Toast.makeText(CheckActivity.this, ""+upload1.getmKey(), Toast.LENGTH_SHORT).show();
                        mUploads.add(upload1);
                        //Toast.makeText(CheckActivity.this, ""+upload1.getmRoom(), Toast.LENGTH_SHORT).show();
                    }


                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CheckActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

   /*     mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // Second.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UploadImage1 uploadImage1=dataSnapshot.getValue(UploadImage1.class);
                    //uploadImage1.setKey(dataSnapshot.getKey());
                    Second.add(uploadImage1);
                }
               // mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CheckActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(this, "To delete an item hold on long press", Toast.LENGTH_SHORT).show();
    }


    //Delete item form firebase Database and Firebase Storage

    @Override
    public void onDeleteClick(int position) {

        Upload SelectedItem=mUploads.get(position);
        final String SelectedKey=SelectedItem.getmKey();

        StorageReference mImgeRef=mStorageRef.getReferenceFromUrl(SelectedItem.getmImgeUrl());
        mImgeRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(SelectedKey).removeValue();
                Toast.makeText(CheckActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}
