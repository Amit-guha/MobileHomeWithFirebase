package com.example.mobilehome.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.messaging.StudentLogIn;
import com.example.mobilehome.studentverifyphonelogin.PhoneLogin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetails extends AppCompatActivity {
    private TextView Tv_contactname,Tv_contDesg,Tv_contDept,Tv_conactFirstnum,Tv_gmail,Tv_contactSecond;
    private TextView tvGender,tvRoom,tvtaka;
    private ImageView Img_contact,Img_call,Img_msg,Img_gmail,Img_callerSecond,Img_msgsecond;
    private ImageView Circle_contact;

    public static  final int REQUEST_CALL=1;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);


        //Add custom toolbar
        Toolbar toolbar=findViewById(R.id.tvtoolbarcontact);
        setSupportActionBar(toolbar);

        //adding back button
        if(getSupportActionBar()!=null){

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        //Change the status bar background color
        Window w=ContactDetails.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(ContactDetails.this,R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Set data in titlebar
        Bundle place=getIntent().getExtras();
        if(place!=null){
            String uPlace=place.getString("Place");
            TextView tvplace=findViewById(R.id.tvset);

            tvplace.setText(uPlace);
        }





        //Firebase Database Reference
        //Database Portion
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");

        Tv_contactname=findViewById(R.id.Tv_contactname);
        //Tv_contDesg=findViewById(R.id.Tv_contDesg);
        tvGender=findViewById(R.id.tvGen);
        tvRoom=findViewById(R.id.tvrm);
        tvtaka=findViewById(R.id.tvtk);

        Tv_contactname=findViewById(R.id.Tv_contactname);

        // Tv_contDept=findViewById(R.id.Tv_contDept);
        Tv_conactFirstnum=findViewById(R.id.Tv_conactFirstnum);
       // Tv_gmail=findViewById(R.id.Tv_gmail);
        Tv_contactSecond=findViewById(R.id.Tv_contactSecond);

        Circle_contact=findViewById(R.id.Circle_contact);
        Img_call=findViewById(R.id.Img_call);
        Img_msg=findViewById(R.id.Img_msg);
       // Img_gmail=findViewById(R.id.Img_gmail);
       // Img_callerSecond=findViewById(R.id.Img_callerSecond);
        Img_msgsecond=findViewById(R.id.Img_msgsecond);



       final Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String UserPlace=bundle.getString("Place");
            String HomeAddress=bundle.getString("Hm_address");
            final String userId=bundle.getString("user_id");
            final String userImage=bundle.getString("profileimag");
            String Taka=bundle.getString("Tk");
            String Room=bundle.getString("Room");
            String Gender=bundle.getString("Gender");

            //Tv_conactFirstnum.setText(UserPlace);
            //Tv_conactFirstnum.setText("Place : "+UserPlace);
            Tv_contactname.setText(HomeAddress);
            tvGender.setText("Status : "+Gender);
            tvRoom.setText("Total Room: "+Room);
            tvtaka.setText("Total cost : "+Taka+" TK");



            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String PhoneNumber= (String) snapshot.child("Phone").getValue();
                    Log.d("PhoneNumber",PhoneNumber);
                    Tv_conactFirstnum.setText(PhoneNumber);


                    Picasso.with(ContactDetails.this).load(userImage).networkPolicy(NetworkPolicy.OFFLINE).into(Circle_contact, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ContactDetails.this)
                                    .load(userImage)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .centerCrop()
                                    .into(Circle_contact);


                        }
                    });



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Img_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ContextCompat.checkSelfPermission(ContactDetails.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(ContactDetails.this,new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CALL);
                    }
                    else{

                        Intent amit = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +Tv_conactFirstnum.getText()));
                       // Toast.makeText(ContactDetails.this, ""+Tv_conactFirstnum.getText(), Toast.LENGTH_SHORT).show();
                        startActivity(amit);
                    }
                }
            });


            Img_msgsecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ContactDetails.this, PhoneLogin.class);
                    intent.putExtra("user_id",userId);
                    //Toast.makeText(ContactDetails.this, ""+userId, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });

            Img_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",Tv_conactFirstnum.getText().toString(), null)));
                }
            });

          /*  Img_callerSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ContextCompat.checkSelfPermission(ContactDetails.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(ContactDetails.this,new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CALL);
                    }
                    else{

                        Intent amit = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +Tv_contactSecond.getText()));
                         Toast.makeText(ContactDetails.this, ""+Tv_contactSecond.getText(), Toast.LENGTH_SHORT).show();
                        startActivity(amit);
                    }
                }
            });
*/
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
