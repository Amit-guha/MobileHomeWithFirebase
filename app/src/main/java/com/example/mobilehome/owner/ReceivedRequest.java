package com.example.mobilehome.owner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.messaging.SendRequestProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReceivedRequest extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mprofileFriendCount;
    private Button mProfileSendReqButton, mProfileDeclineReqButton;

    ProgressDialog mProgressDialog;
    private String mCurrent_state;


    DatabaseReference mfriendReqReference;
    DatabaseReference mDatabaseReference;
    DatabaseReference mFriendDatabase;
    DatabaseReference mNotificationReference;
    DatabaseReference mRootReference;
    DatabaseReference getmDatabaseReference;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_request);

        mAuth = FirebaseAuth.getInstance();
        // String User_id=mAuth.getCurrentUser().getUid();


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
        // mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationReference = FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        mProgressDialog = new ProgressDialog(ReceivedRequest.this);
        mProgressDialog.setTitle("Fetching Details");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();



/*
        //Extra node----------------------->>get data
        DatabaseReference mExNode=FirebaseDatabase.getInstance().getReference().child("ExtraNode");
        mExNode.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String data=snapshot.child("from").getValue().toString();
               // final String mdbkey=snapshot.child("k").getValue().toString();
                mprofileFriendCount.setText(data);
                mfriendReqReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild(data) && snapshot.equals(data)) {

                            String request_type = snapshot.child(data).child("request_type").getValue().toString();

                            if (request_type.equals("received")) {
                                mCurrent_state = "req_received";
                                mProfileSendReqButton.setText("Accept Friend Request");
                                mProfileDeclineReqButton.setVisibility(View.VISIBLE);
                                mProfileDeclineReqButton.setEnabled(true);
                            }
                            mProgressDialog.dismiss();

                        }

                        //---USER IS FRIEND----
                        else {

                            mFriendDatabase.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                    mProfileDeclineReqButton.setEnabled(false);

                                    if (dataSnapshot.hasChild(data)) {
                                        mCurrent_state = "friends";
                                        mProfileSendReqButton.setText("Unfriend This Person");
                                    }
                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final String txt = dataSnapshot.child("thumb_image").getValue().toString();
                    // mprofileFriendCount.append(txt);

                    //--SEEING THE FRIEND STATE OF THE USER---
                    //----ADDING THE TWO BUTTON----- mfriendReqReference.child(mFirebaseUser.getUid())
                    final DatabaseReference mRef = mDatabaseReference.getParent().child("friend_request").child(mFirebaseUser.getUid());
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long length=snapshot.getChildrenCount();
                            String s=Long.toString(length);

                            if (snapshot.hasChild(txt)) {

                                String tt = txt.toString();
                                mProfileName.setText(tt);
                                String request_type = snapshot.child(txt).child("request_type").getValue().toString();

                                if (request_type.equals("received")) {
                                    mCurrent_state = "req_received";
                                    mProfileSendReqButton.setText("Accept Friend Request");
                                    mProfileDeclineReqButton.setVisibility(View.VISIBLE);
                                    mProfileDeclineReqButton.setEnabled(true);


                                } else if (request_type.equals("sent")) {
                                    mCurrent_state = "req_sent";
                                    mProfileSendReqButton.setText("Cancel Friend Request");
                                    mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                    mProfileDeclineReqButton.setEnabled(false);
                                }

                                mProgressDialog.dismiss();

                            }

                            else if (!snapshot.hasChild(txt) &&  length!=0) {
                                Toast.makeText(ReceivedRequest.this, "Data not found state", Toast.LENGTH_SHORT).show();
                              //  mRef.removeEventListener(this);
                            }


                            //---USER IS FRIEND----
                            else {

                                mFriendDatabase.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        long a=dataSnapshot.getChildrenCount();

                                        mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                        mProfileDeclineReqButton.setEnabled(false);

                                        if (dataSnapshot.hasChild(txt)) {
                                            String data=txt;
                                            mprofileFriendCount.setText(data);
                                            mCurrent_state = "friends";
                                            mProfileSendReqButton.setText("Unfriend This Person");

                                        }
                                        else if(!dataSnapshot.hasChild(txt) && a==0){
                                            Toast.makeText(ReceivedRequest.this, "THis method is calling", Toast.LENGTH_SHORT).show();
                                            mProfileSendReqButton.setText("You Have 0 Request Left");
                                            mProfileSendReqButton.setEnabled(false);
                                        }
                                        mProgressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                        mProgressDialog.dismiss();
                                    }
                                });


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
        });


//---------------------------------------------------------------------------------------------------------//

        //-------SEND REQUEST BUTTON IS PRESSED----


        mProfileSendReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUserId = mFirebaseUser.getUid();
                String m;

                    m=mProfileName.getText().toString();



                if (mUserId.equals(m)) {
                    Toast.makeText(ReceivedRequest.this, "Cannot send request to your own", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e("m_current_state is : ", mCurrent_state);
               // mProfileSendReqButton.setText("Can't send Friend request");
                mProfileSendReqButton.setEnabled(false);


                //-------NOT FRIEND STATE--------
                if (mCurrent_state.equals("not_friends")) {
                  //  mProfileSendReqButton.setEnabled(true);
                    mProfileSendReqButton.setText("You have 0 request");

/*
                    DatabaseReference newNotificationReference = mRootReference.child("notifications").child(m).push();

                    String newNotificationId = newNotificationReference.getKey();

                    HashMap<String, String> notificationData = new HashMap<String, String>();
                    notificationData.put("from", mFirebaseUser.getUid());
                    notificationData.put("type", "request");

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/" + mFirebaseUser.getUid() + "/" + m + "/request_type", "sent");
                    requestMap.put("friend_request/" + m + "/" + mFirebaseUser.getUid() + "/request_type", "received");
                    requestMap.put("notifications/" + m + "/" + newNotificationId, notificationData);

                    //----FRIEND REQUEST IS SEND----
                    mRootReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                Toast.makeText(ReceivedRequest.this, "Friend Request sent successfully", Toast.LENGTH_SHORT).show();

                                mProfileSendReqButton.setEnabled(false);
                               // mProfileSendReqButton.setEnabled(true);
                                mCurrent_state = "req_sent";
                               // mProfileSendReqButton.setText("Cancel Friend Request");
                                mProfileSendReqButton.setText("You have 0 request");

                            } else {
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ReceivedRequest.this, "Some error in sending friend Request", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });*/
                }

                //-------CANCEL--FRIEND--REQUEST-----

                if (mCurrent_state.equals("req_sent")) {
                    mProfileSendReqButton.setText("You have 0 request");
                    mProfileSendReqButton.setEnabled(false);

      /*              Map valueMap = new HashMap();
                    valueMap.put("friend_request/" + mFirebaseUser.getUid() + "/" + m, null);
                    valueMap.put("friend_request/" + m + "/" + mFirebaseUser.getUid(), null);

                    //----FRIEND REQUEST IS CANCELLED----
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                mCurrent_state = "not_friends";
                               // mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setText("You have 0 request");
                                //mProfileSendReqButton.setEnabled(true);
                                mProfileSendReqButton.setEnabled(false);
                                Toast.makeText(ReceivedRequest.this, "Friend Request Cancelled Successfully...", Toast.LENGTH_SHORT).show();
                            } else {

                                mProfileSendReqButton.setEnabled(true);
                                //Toast.makeText(ReceivedRequest.this, "Cannot cancel friend request...", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });*/
                }


                //-------ACCEPT FRIEND REQUEST------
                if (mCurrent_state.equals("req_received")) {
                    //-----GETTING DATE-----
                    Date current_date = new Date(System.currentTimeMillis());

                    //Log.e("----Date---",current_date.toString());
                    String date[] = current_date.toString().split(" ");
                    final String todays_date = (date[1] + " " + date[2] + "," + date[date.length - 1] + " " + date[3]);

                    Map friendMap = new HashMap();
                    friendMap.put("friends/" + mFirebaseUser.getUid() + "/" + m + "/date", todays_date);
                    friendMap.put("friends/" + m + "/" + mFirebaseUser.getUid() + "/date", todays_date);

                    friendMap.put("friend_request/" + mFirebaseUser.getUid() + "/" + m, null);
                    friendMap.put("friend_request/" + m + "/" + mFirebaseUser.getUid(), null);

                    //**************************************
                    // friendMap.put("ExtraNode/"+mFirebaseUser.getUid()+"/",null);

                    //-------BECAME FRIENDS------
                    mRootReference.updateChildren(friendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state = "friends";
                                mProfileSendReqButton.setText("Unfriend this person");
                                mProfileDeclineReqButton.setEnabled(false);
                                mProfileDeclineReqButton.setVisibility(View.INVISIBLE);

                            } else {
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ReceivedRequest.this, "Error is " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


                //----UNFRIEND---THIS---PERSON----
                if (mCurrent_state.equals("friends")) {
                    String x=mProfileName.getText().toString();

                        Map valueMap = new HashMap();
                        valueMap.put("friends/" + mFirebaseUser.getUid() + "/" + x, null);
                        valueMap.put("friends/" + x + "/" + mFirebaseUser.getUid(), null);


                        valueMap.put("friends/" + mFirebaseUser.getUid() + "/" + mprofileFriendCount.getText().toString(), null);
                        valueMap.put("friends/" + mprofileFriendCount.getText().toString() + "/" + mFirebaseUser.getUid(), null);

                        //----UNFRIENDED THE PERSON---
                        mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqButton.setText("Can't Send Friend Request");
                                   // mProfileSendReqButton.setEnabled(true);
                                    Toast.makeText(ReceivedRequest.this, "Successfully Unfriended...", Toast.LENGTH_SHORT).show();
                                } else {
                                    mProfileSendReqButton.setEnabled(true);
                                    Toast.makeText(ReceivedRequest.this, "Cannot Unfriend..Contact Amit..", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });




                }

            }
        });


//********************************************************************************************
        //-----DECLING THE FRIEND REQUEST-----

        mProfileDeclineReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String mUserId = mFirebaseUser.getUid();
                // String m=mprofileFriendCount.getText().toString();
                String m = mProfileName.getText().toString();

                Map valueMap = new HashMap();
                valueMap.put("friend_request/" + mFirebaseUser.getUid() + "/" + m, null);
                valueMap.put("friend_request/" + m + "/" + mFirebaseUser.getUid(), null);


                mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                        if (error == null) {
                            mCurrent_state = "not_friends";
                           // mProfileSendReqButton.setText("Send Friend Request");
                            mProfileSendReqButton.setText("Can't Send Friend Request THis Person");
                            mProfileSendReqButton.setEnabled(false);
                            Toast.makeText(ReceivedRequest.this, "Friend Request Declined Successfully...", Toast.LENGTH_SHORT).show();

                            mProfileDeclineReqButton.setEnabled(false);
                            mProfileDeclineReqButton.setVisibility(View.INVISIBLE);


                        } else {
                            mProfileSendReqButton.setEnabled(true);
                            Toast.makeText(ReceivedRequest.this, "Cannot decline friend request...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }
}
