package com.example.mobilehome.multifileupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ImageUpload extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private Button mBtn,btnshow,btnsubmit;
    private EditText tvUpBtn;
    private RecyclerView mRecclerview;
    private List<String> fileNameList;
    private List<String> fileDoneList;

    ArrayList<String> urlstring;


    //2nd method
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private int uploads = 0;

    private FirebaseAuth mAuth;

    UploadCleassAdapter uploadCleassAdapter;
    private StorageReference mstorage;

    //check
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);



        tvUpBtn=findViewById(R.id.tvtext);
        mAuth = FirebaseAuth.getInstance();

        mBtn = findViewById(R.id.btnselectmultiplefile);
        btnshow=findViewById(R.id.btnshow);
        btnsubmit=findViewById(R.id.btnImageanddata);
        mRecclerview = findViewById(R.id.RecyUpload);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();


        uploadCleassAdapter = new UploadCleassAdapter(fileNameList, fileDoneList);

        mRecclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecclerview.setHasFixedSize(true);
        mRecclerview.setAdapter(uploadCleassAdapter);

        //Firebase
        mstorage= FirebaseStorage.getInstance().getReference();

        //check
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Imagebox");

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ImageUpload.this,Fileshow.class);
                startActivity(intent);
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                urlstring=new ArrayList();
                for(uploads=0;uploads<ImageList.size();uploads++){
                   // Log.d("Uploadid", String.valueOf(uploads));

                    Uri Image  = ImageList.get(uploads);
                    Toast.makeText(ImageUpload.this, ""+Image, Toast.LENGTH_SHORT).show();

                    final StorageReference filetoUpload=mstorage.child(Image.getLastPathSegment());

                    //ImageList.get(uploads)
                    filetoUpload.putFile(Image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri  downloadUri = uriTask.getResult();
                            String url = String.valueOf(downloadUri);
                            urlstring.add(url);

                            if(urlstring.size()==ImageList.size()){
                                storeLink(urlstring);
                            }


                          //  Log.d("Url", url);
                            // String data=tvUpBtn.getText().toString();
                            // SendLink(url,data);


                           // int i=uploads;


                            //if(mAuth.getCurrentUser()!=null){

                          //  }
                            Toast.makeText(ImageUpload.this, "Done!", Toast.LENGTH_SHORT).show();

                        }

                    });

                }
            }
        });
    }

    private void storeLink(ArrayList<String> urlstring) {
        HashMap<String,Object>map= new HashMap<>();
        HashMap<String,Object> data=new HashMap<>();


        String key=databaseReference.push().getKey();

        data.put("text",tvUpBtn.getText().toString());
        data.put("UserId",mAuth.getCurrentUser().getUid());
        data.put("key",key);

        for(int i=0;i<urlstring.size();i++){
            map.put("link"+i,urlstring.get(i));
          //  Log.d("somethihg","link"+i);
        }


        databaseReference.child(mAuth.getCurrentUser().getUid()).child(key).updateChildren(data);

        databaseReference.child(mAuth.getCurrentUser().getUid()).child(key).child("Url").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ImageUpload.this, "Uploded successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalSelectedItem = data.getClipData().getItemCount();

                //generate id
                final String Uploadid = databaseReference.push().getKey();

                for (int i = 0; i < totalSelectedItem; i++) {
                    Uri fileuri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileuri);

                    fileNameList.add(fileName);
                    uploadCleassAdapter.notifyDataSetChanged();


                    StorageReference filetoUpload=mstorage.child(fileName);

                    final int finalI1 = i;
                    filetoUpload.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri  downloadUri = uriTask.getResult();
                            String url=String.valueOf(downloadUri);

                            Log.d("url",url);

                           // MultiImage multiImage=new MultiImage(tvUpBtn.getText().toString(),url);

                           // HashMap<String,Object>hashMap=(HashMap<String, Object>) multiImage.toMap();
                           // hashMap.put("text",hashMap);

                            HashMap<String,Object>map= new HashMap<>();
                            map.put("link"+ finalI1,url);

                            HashMap<String,Object> data=new HashMap<>();
                            data.put("text",tvUpBtn.getText().toString());

                            if(mAuth.getCurrentUser()!=null) {

                                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(data);

                                databaseReference.child(mAuth.getCurrentUser().getUid()).child("Url").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ImageUpload.this, "Uploded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                           *//* MultiImage multiImage = new MultiImage(url, tvUpBtn.getText().toString());
                            databaseReference.child("1234").push().setValue(multiImage);*//*

                            // String key=databaseReference.push().getKey();

                            Toast.makeText(ImageUpload.this, "done", Toast.LENGTH_SHORT).show();

                        }
                    });

                   // Toast.makeText(this, ""+fileNameList.get(i), Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(this, "Select Multiple files", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null) {
                Toast.makeText(this, "Select Single Images", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            if (data.getClipData() != null){
                int totalSelectedItem = data.getClipData().getItemCount();

                for(int i=0;i<totalSelectedItem;i++){
                    final Uri fileuri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileuri);
                    fileNameList.add(fileName);
                  

                    ImageList.add(fileuri);
                    uploadCleassAdapter.notifyDataSetChanged();
                }

            }else if (data.getData() != null) {
                Toast.makeText(this, "Select Single Images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }
}