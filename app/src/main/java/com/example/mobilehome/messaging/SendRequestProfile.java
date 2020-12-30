package com.example.mobilehome.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.studentverifyphonelogin.PhoneLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendRequestProfile extends AppCompatActivity {
    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mprofileFriendCount;
    private Button mProfileSendReqButton, mProfileDeclineReqButton;

    ProgressDialog mProgressDialog;
    private String mCurrent_state;

    //Send Request for customer---------->>>Studetn

    DatabaseReference mfriendReqReference;
    DatabaseReference mDatabaseReference;
    DatabaseReference mDatabaseReferenceid;
    DatabaseReference mFriendDatabase;
    DatabaseReference mNotificationReference;
    DatabaseReference mRootReference;
    DatabaseReference getmDatabaseReference;

    FirebaseUser mFirebaseUser;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request_profile);


        //Change the status bar background color
        Window w= SendRequestProfile.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(SendRequestProfile.this,R.color.background));

        //To change the status bar Text and icon color
       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        //---GETTING ID OF USER WHOSE PROFILE IS OPENED----
        user_id = getIntent().getStringExtra("user_id");

   /*    //---AFTER CLICKING NOTIFICATION , IF USER_ID IS NOT FETCHED
       if(user_id==null){
          Intent intent = new Intent(SendRequestProfile.this,StudentLogIn.class);
           startActivity(intent);
            finish();
        }*/


        mProfileImage = (ImageView) findViewById(R.id.profileUserImage);
        mProfileName = (TextView) findViewById(R.id.profileUserName);
        mProfileStatus = (TextView) findViewById(R.id.profileUserStatus);
        mprofileFriendCount = (TextView) findViewById(R.id.profileUserFriends);
        mProfileSendReqButton = (Button) findViewById(R.id.profileSendReqButton);
        mProfileDeclineReqButton = (Button) findViewById(R.id.profileDeclineReqButton);

        //----IT WILL BECOME VISIBLE ONLY WHEN WE GET THE FRIEND REQUEST FROM THAT PERSON-----
        mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
        mProfileDeclineReqButton.setEnabled(false);



        mfriendReqReference = FirebaseDatabase.getInstance().getReference().child("friend_request");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
       // mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationReference = FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //ProgressDialogue
        mProgressDialog=new ProgressDialog(SendRequestProfile.this);
        mProgressDialog.setTitle("Fetching Details");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mCurrent_state = "not_friends"; // 4 types--- "not_friends" , "req_sent"  , "req_received" & "friends"

        //----ADDING NAME , STATUS AND IMAGE OF USER----
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String display_name=snapshot.child("name").getValue().toString();
                String display_status=snapshot.child("Phone").getValue().toString();
              //  String display_image=snapshot.child("image").getValue().toString();
             //  final String text= (String) snapshot.child("thumb_image").getValue();
                mProfileName.setText(display_name);
                mProfileStatus.setText(display_status);
               // mProfileStatus.setText(display_status);
                //Picasso.with(ProfileActivity.this).load(display_image).placeholder(R.drawable.user_img).into(mProfileImage);

                //----ADDING TOTAL  NO OF FRIENDS---
                mFriendDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        long len = snapshot.getChildrenCount();
                        mprofileFriendCount.setText("TOTAL FRIENDS : "+len);



                        //----SEEING THE FRIEND STATE OF THE USER---
                        //----ADDING THE TWO BUTTON-----

                        mfriendReqReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //----CHECKING IF FRIEND REQUEST IS SEND OR RECEIVED----
                                if(snapshot.hasChild(user_id)) {
                                    String request_type = (String) snapshot.child(user_id).child("request_type").getValue();

                                    if (request_type.equals("sent")) {

                                        mCurrent_state = "req_sent";
                                        mProfileSendReqButton.setText("Cancel Friend Request");
                                        mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                        mProfileDeclineReqButton.setEnabled(false);
                                    } else if (request_type.equals("received")) {
                                        mCurrent_state = "req_received";
                                        mProfileSendReqButton.setText("Accept Friend Request");
                                        mProfileDeclineReqButton.setVisibility(View.VISIBLE);
                                        mProfileDeclineReqButton.setEnabled(true);
                                    }
                                    mProgressDialog.dismiss();

                                }
                                    //---USER IS FRIEND----
                                else{
                                    mFriendDatabase.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                            mProfileDeclineReqButton.setEnabled(false);

                                            if(snapshot.hasChild(user_id)){
                                                mCurrent_state="friends";
                                                mProfileSendReqButton.setText("Unfriend This Person");
                                            }
                                            mProgressDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            mProgressDialog.dismiss();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SendRequestProfile.this, "Error fetching Friend request data", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mProgressDialog.dismiss();
            }
        });




        //-------SEND REQUEST BUTTON IS PRESSED----
        mProfileSendReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUserId= mFirebaseUser.getUid();

                if(mUserId.equals(user_id)){
                    Toast.makeText(SendRequestProfile.this, "Cannot send request to your own", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e("m_current_state is : ",mCurrent_state);
                mProfileSendReqButton.setEnabled(false);

                //-------NOT FRIEND STATE--------
                if(mCurrent_state.equals("not_friends")){

                    DatabaseReference newNotificationReference = mRootReference.child("notifications").child(user_id).push();
                    String newNotificationId = newNotificationReference.getKey();

                    HashMap<String,String> notificationData=new HashMap<String, String>();
                    notificationData.put("from",mFirebaseUser.getUid());
                    notificationData.put("type","request");

                    //************************************

                    //Extra node for getting the user id
                    DatabaseReference mdbExtra=mRootReference.child("ExtraNode").child(user_id);
                    String mdbKey=mdbExtra.push().getKey();
                    HashMap<String,String> exMap=new HashMap<>();
                    exMap.put("from",mFirebaseUser.getUid());
                   // exMap.put("k",mdbKey);

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/"+mFirebaseUser.getUid()+ "/"+user_id + "/request_type","sent");
                    requestMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid()+"/request_type","received");
                    requestMap.put("notifications/"+user_id+"/"+newNotificationId,notificationData);
                    //extra Map
                    requestMap.put("ExtraNode/"+user_id+"/",exMap);


//************************************************************
                    //----FRIEND REQUEST IS SEND----
                    mRootReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error==null){

                                Toast.makeText(SendRequestProfile.this, "Friend Request sent successfully", Toast.LENGTH_SHORT).show();

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state= "req_sent";
                                mProfileSendReqButton.setText("Cancel Friend Request");

                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(SendRequestProfile.this, "Some error in sending friend Request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


                //-------CANCEL--FRIEND--REQUEST-----
                if(mCurrent_state.equals("req_sent")){
                    Map valueMap=new HashMap();
                    valueMap.put("friend_request/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    valueMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //----FRIEND REQUEST IS CANCELLED----
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            if(error == null){

                                mCurrent_state = "not_friends";
                                mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(SendRequestProfile.this, "Friend Request Cancelled Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(SendRequestProfile.this, "Cannot cancel friend request...", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }


                //-------ACCEPT FRIEND REQUEST------
                if(mCurrent_state.equals("req_received")){
                    //-----GETTING DATE-----
                    Date current_date=new Date(System.currentTimeMillis());

                    //Log.e("----Date---",current_date.toString());
                    String date[]=current_date.toString().split(" ");
                    final String todays_date=(date[1] + " " + date[2] + "," + date[date.length-1]+" "+date[3]);

                    Map friendMap=new HashMap();
                    friendMap.put("friends/"+mFirebaseUser.getUid()+"/"+user_id+"/date",todays_date);
                    friendMap.put("friends/"+user_id+"/"+mFirebaseUser.getUid()+"/date",todays_date);

                    friendMap.put("friend_request/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    friendMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid(),null);
           //***************************************************************
                    friendMap.put("ExtraNode/"+user_id+"/",null);

                    //-------BECAME FRIENDS------
                    mRootReference.updateChildren(friendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            if(error==null){

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state = "friends";
                                mProfileSendReqButton.setText("Unfriend this person");
                                mProfileDeclineReqButton.setEnabled(false);
                                mProfileDeclineReqButton.setVisibility(View.INVISIBLE);

                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(SendRequestProfile.this, "Error is " +error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                //----UNFRIEND---THIS---PERSON----
                if(mCurrent_state.equals("friends")){
                    Map valueMap=new HashMap();
                    valueMap.put("friends/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    valueMap.put("friends/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //----UNFRIENDED THE PERSON---
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            if(error == null){
                                mCurrent_state = "not_friends";
                                mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(SendRequestProfile.this, "Successfully Unfriended...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(SendRequestProfile.this, "Cannot Unfriend..Contact Kshitiz..", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }



            }
        });


/*        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                   // final String text= (String) dataSnapshot.child("thumb_image").getValue();
                   // mProfileName.setText(text);

                    mProfileName.setText(user_id);


                    //--SEEING THE FRIEND STATE OF THE USER---
                    //----ADDING THE TWO BUTTON-----
                    mfriendReqReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //----CHECKING IF FRIEND REQUEST IS SEND OR RECEIVED----

                            if(snapshot.hasChild(user_id)){
                                String request_type = (String) snapshot.child(user_id).child("request_type").getValue();


                                if(request_type.equals("sent")){

                                    mCurrent_state="req_sent";
                                    mProfileSendReqButton.setText("Cancel Friend Request");
                                    mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                    mProfileDeclineReqButton.setEnabled(false);
                                }

                                if(request_type.equals("received")){
                                    mCurrent_state = "req_received";
                                    mProfileSendReqButton.setText("Accept Friend Request");
                                    mProfileDeclineReqButton.setVisibility(View.VISIBLE);
                                    mProfileDeclineReqButton.setEnabled(true);
                                }
                              //  mProgressDialog.dismiss();

                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }
}