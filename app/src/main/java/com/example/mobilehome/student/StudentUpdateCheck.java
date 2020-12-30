package com.example.mobilehome.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.CheckActivity;
import com.example.mobilehome.owner.Upload;
import com.example.mobilehome.owner.UploadAdapter;
import com.example.mobilehome.owner.UploadImage1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StudentUpdateCheck extends AppCompatActivity implements StudenAdapter.OnItemCLICKLISITINER {
    private RecyclerView mRecyclerView;
    private ArrayList<Upload> mUploads;
    private ArrayList<UploadImage1>Second;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,mDatabaseSecond;
    private FirebaseAuth mAuth;

    private StudenAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update_check);

        //Change the status bar background color
        Window w=StudentUpdateCheck.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(StudentUpdateCheck.this,R.color.white));

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        mRecyclerView=findViewById(R.id.studentRecyclerview);

            //Initialize of  Storage && database Reference
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Gopalganj");
            //mStorageRef = FirebaseStorage.getInstance().getReference("Gopalganj").child(mAuth.getCurrentUser().getUid());

            //  mDatabaseSecond=FirebaseDatabase.getInstance().getReference("Image2");


        mUploads=new ArrayList<>();
        Second=new ArrayList<>();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);
        // mAdapter=new UploadAdapter(this,mUploads,Second);
        mAdapter=new StudenAdapter(this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);


        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            final String OP=bundle.getString("Location");
            //First iamge
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mUploads.clear();
                    Boolean fun=false;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Upload upload = ds.getValue(Upload.class);


                        //Toast.makeText(StudentUpdateCheck.this, ""+upload.getmUserId(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(StudentUpdateCheck.this, ""+upload.getmUserId()+" "+upload.getmKey(), Toast.LENGTH_SHORT).show();
                        //upload.setmKey(ds.getKey());
                        // Toast.makeText(StudentUpdateCheck.this, ""+upload.getmRoom(), Toast.LENGTH_SHORT).show();
                        String room = upload.getmPlace();
                            if(room.equals(OP)){
                                Upload upload1 = ds.getValue(Upload.class);
                                upload1.setmKey(upload1.getmKey());
                                mUploads.add(upload);
                                fun=true;
                               // Toast.makeText(StudentUpdateCheck.this, "Loading...", Toast.LENGTH_SHORT).show();

                        }

                    }
                    if(fun==false){
                        Toast.makeText(StudentUpdateCheck.this, "data not found", Toast.LENGTH_SHORT).show();
                    }

                    mAdapter.notifyDataSetChanged();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(StudentUpdateCheck.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


       /* mDatabaseRef.addValueEventListener(new ValueEventListener() {
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

                Toast.makeText(StudentUpdateCheck.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/


    }

    @Override
    public void onItemClick(int position) {
        Upload SelectedItem=mUploads.get(position);
        String place = SelectedItem.getmPlace();
        String homneAddress=SelectedItem.getmHomeAddress();
        String userId = SelectedItem.getmUserId();
        String userImage=SelectedItem.getmImgeUrl();
        String Room=SelectedItem.getmRoom();
        String Gender=SelectedItem.getmGender();
        String Tk=SelectedItem.getmTaka();

        Intent i =new Intent(StudentUpdateCheck.this,ContactDetails.class);
        i.putExtra("Place", place);
        i.putExtra("Hm_address",homneAddress);
        i.putExtra("user_id", userId);
        i.putExtra("profileimag",userImage);
        i.putExtra("Gender",Gender);
        i.putExtra("Tk",Tk);
        i.putExtra("Room",Room);

       // Toast.makeText(this, ""+position+" "+place, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();
        startActivity(i);

    }
}
