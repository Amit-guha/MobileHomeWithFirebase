package com.example.mobilehome.owner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mobilehome.R;
import com.example.mobilehome.student.SearchAnotherWay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class UploadStatus extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_Button2 = 2;
    private static final int PICK_IMAGE_Button3 = 3;

    private ImageView Fimg, Seimg, Thimg;
    private TextInputEditText EtRoom, EtCost, EtHomeAddress;
    //private TextView ChoImg1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button BtnSubmit, ChoImg1;
    private Spinner mSpinner;


    private String[] Place;

    private Uri mImageUri, mImaguri1, mImgauri2;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, mDbSecond;
    private StorageTask mUploadTask;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_status);

        //Change the status bar background color
        Window w = UploadStatus.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(UploadStatus.this, R.color.white));


        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Fimg = findViewById(R.id.Firstimag);


        EtRoom = findViewById(R.id.RoomNum);
        EtCost = findViewById(R.id.perMonthBdtField);
        EtHomeAddress = findViewById(R.id.Homeaddress);


        ChoImg1 = findViewById(R.id.Btnimag1);
        BtnSubmit = findViewById(R.id.btnSubmit);

        radioGroup = findViewById(R.id.readioGroup);

        mSpinner = findViewById(R.id.Spinnerid);
        Place = getResources().getStringArray(R.array.List_Of_Place);

        ArrayAdapter<String> TotalPlace = new ArrayAdapter<>(this, R.layout.smaple_spinner, R.id.tv_Spinner, Place);
        mSpinner.setAdapter(TotalPlace);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser Currentuser = mAuth.getCurrentUser();
        String m = Currentuser.toString();

        //Retrieve an instance of your database using getInstance() and reference the location you want to write to.
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Gopalganj");
        mDbSecond = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Click ChoImg1 button for selecting an image
        ChoImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        //Upload file--------------->>>Firebase Database
        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadStatus.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    UploadFile();
                    //  uploadfile2();

                }
            }
        });

    }


    //OpenFileChooser===>>Gallery
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    //OpenfileChooser------>>Second image
    private void openFileChooser1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_Button2);
    }

    //OpenfileChooser------>>Third image
    private void openFileChooser2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_Button3);
    }


    //Load data and get into the FirstImageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(Fimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(mImageUri).into(Fimg);
                    Log.d("Crash", "Context context.....");
                }
            });
        }

        //to get image from gallery------------>>>Select second image
        if (requestCode == PICK_IMAGE_Button2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImaguri1 = data.getData();

            Picasso.with(this).load(mImaguri1).networkPolicy(NetworkPolicy.OFFLINE).into(Seimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(mImaguri1).into(Seimg);
                    Log.d("Crash", "Context context.....");
                }
            });
        }

        //For 3rd button-------->>Select image from gallery
        if (requestCode == PICK_IMAGE_Button3 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImgauri2 = data.getData();

            Picasso.with(this).load(mImgauri2).networkPolicy(NetworkPolicy.OFFLINE).into(Thimg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(mImgauri2).into(Thimg);
                    Log.d("Crash", "Context context.....");
                }
            });
        }


    }


    //File Extension
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //For second image........................................................


    public void UploadFile() {
        final String Uploadid = mDatabaseRef.push().getKey();
        //radiobutton
        final int radioId = radioGroup.getCheckedRadioButtonId();

        String RoomNo = EtRoom.getText().toString().trim();
        String MOney = EtCost.getText().toString().trim();
        String HomeAddress = EtHomeAddress.getText().toString().trim();


        radioButton = findViewById(radioId);


        if ((mImageUri != null) && (radioId != -1) && (!TextUtils.isEmpty(RoomNo)) && (!TextUtils.isEmpty(MOney))
                && (!TextUtils.isEmpty(HomeAddress)) && (!mSpinner.getSelectedItem().toString().trim().equals("Select any area"))) {
            //First image
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            // final String userid=mAuth.getCurrentUser().getUid().toString();
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(UploadStatus.this, "Upload Successfully", Toast.LENGTH_SHORT).show();

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadUri = uriTask.getResult();

                    String gender = radioButton.getText().toString();


                    Upload upload = new Upload(downloadUri.toString(), EtRoom.getText().toString(),
                            gender, EtCost.getText().toString(), mSpinner.getSelectedItem().toString(), EtHomeAddress.getText().toString(),
                            mAuth.getCurrentUser().getUid(), Uploadid);

                    // final String Uploadid = mDatabaseRef.push().getKey();
                    if (mAuth.getCurrentUser() != null) {
                        mDatabaseRef.child(Uploadid).setValue(upload);
                        Toast.makeText(UploadStatus.this, "Uplaod All data", Toast.LENGTH_SHORT).show();

                        EtRoom.setText("");
                        EtCost.setText("");
                        EtHomeAddress.setText("");
                    }



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadStatus.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            });


        } else {
            Toast.makeText(this, "Please Fill up all the Options", Toast.LENGTH_SHORT).show();
        }


    }
}
