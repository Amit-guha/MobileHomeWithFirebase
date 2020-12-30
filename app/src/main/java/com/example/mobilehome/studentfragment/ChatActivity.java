package com.example.mobilehome.studentfragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.student.SearchAnotherWay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    //Appbar layout
    private String mChatUser;
    TextView mUserName;
    TextView mUserLastSeen;
    CircleImageView mUserImage;
    private FirebaseAuth mAuth;

    String mCurrentUserId;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mRootReference;

    //ChatActivity
    private ImageButton mChatSendButton, mChatAddButton;
    private EditText mMessageView;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;

    public static final int TOTAL_ITEM_TO_LOAD = 10;
    private int mCurrentPage = 1;

    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";

    private static final int GALLERY_PICK = 1;
    StorageReference mImageStorage;

    DatabaseReference messageDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatAddButton = (ImageButton) findViewById(R.id.chatAddButton);
        mChatSendButton = (ImageButton) findViewById(R.id.chatSendButton);
        mMessageView = (EditText) findViewById(R.id.chatMessageView);

        //-----GETING FROM INTENT----
        mChatUser = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");

        //---SETTING ONLINE------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

     /*   //----ADDING ACTION BAR-----
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //---INFLATING APP BAR LAYOUT INTO ACTION BAR----
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.appbar_layout, null);
        actionBar.setCustomView(actionBarView);

        //---ADDING DATA ON ACTION BAR----
        mUserName = (TextView) actionBarView.findViewById(R.id.textView3);
        mUserLastSeen = (TextView) actionBarView.findViewById(R.id.textView5);
        mUserImage = (CircleImageView) actionBarView.findViewById(R.id.circleImageView);
        mUserName.setText(userName);*/



        //Add custom toolbar
        Toolbar toolbar=findViewById(R.id.tvchatbar);
        setSupportActionBar(toolbar);


        //adding back button
        if(getSupportActionBar()!=null){

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //---ADDING DATA ON ACTION BAR----
        mUserName = (TextView) findViewById(R.id.textView3);
        mUserLastSeen = (TextView) findViewById(R.id.textView5);
        mUserImage = (CircleImageView)findViewById(R.id.circleImageView);
        mUserName.setText(userName);


        //Change the status bar background color
        Window w= ChatActivity.this.getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setStatusBarColor(ContextCompat.getColor(ChatActivity.this,R.color.white));

       /* TextView tooltv=findViewById(R.id.toolbarTextview);
        ImageView toolimg=findViewById(R.id.toolbarimageview);

        tooltv.setText("Search Another Way");*/

        //To change the status bar Text and icon color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);




        //Main view----------------------------------->>>Activity
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mMessageAdapter = new MessageAdapter(messagesList);

        mMessagesList = (RecyclerView) findViewById(R.id.recycleViewMessageList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);

        // mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayoutManager);
        mMessagesList.setAdapter(mMessageAdapter);

        loadMessages();

        //----ADDING LAST SEEN-----
        mRootReference.child("users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String onlineValue = snapshot.child("online").getValue().toString();
                //String imageValue = snapshot.child("thumb_image").getValue().toString();

                // Picasso.with(ChatActivity.this).load(imageValue).placeholder(R.drawable.user_img).into(mUserImage);
                if (onlineValue.equals("true")) {
                    mUserLastSeen.setText("online");
                } else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(onlineValue);
                    String lastSeen = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
                    mUserLastSeen.setText(lastSeen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //----ADDING SEEN OF MESSAGES----
        mRootReference.child("chats").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(mChatUser)) {
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/" + mChatUser + "/" + mCurrentUserId, chatAddMap);
                    chatUserMap.put("chats/" + mCurrentUserId + "/" + mChatUser, chatAddMap);

                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                          /*  if(error !=null){
                                Log.d("ChatLog",error.getMessage());

                            }*/

                            if (error == null) {
                                Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();


                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();

            }
        });

        //----SEND MESSAGE--BUTTON----
        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageView.getText().toString();
                if (!TextUtils.isEmpty(message)) {

                    String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                    String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message", message);
                    messageMap.put("seen", false);
                    messageMap.put("type", mChatUser);
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    messageMap.put("from", mCurrentUserId);

                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            if (error != null) {
                                Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                            } else {
                                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                mMessageView.setText("");
                            }
                        }
                    });
                }
            }
        });


           /*
        //----THE WRAP CONTENT OF IMAGE VIEW IS GIVING ERROR--- SO REMOVING THIS FUNCTIONALITY-------
       mChatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
            }
        });
        */

        //----LOADING 10 MESSAGES ON SWIPE REFRESH----
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                mCurrentPage++;
                loadMoreMessages();

            }
        });

    }



    //---FIRST 10 MESSAGES WILL LOAD ON START----
    private void loadMessages() {

        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                //check null fast
                DatabaseReference messageRef = mRootReference.child("messages").child(user.getUid()).child(mChatUser);
                Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);

                messageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Messages messages = snapshot.getValue(Messages.class);
                        itemPos++;

                        if (itemPos == 1) {
                            String mMessageKey = snapshot.getKey();

                            mLastKey = mMessageKey;
                            mPrevKey = mMessageKey;
                        }

                        messagesList.add(messages);
                        mMessageAdapter.notifyDataSetChanged();

                        mMessagesList.scrollToPosition(messagesList.size() - 1);

                        mSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    //---ON REFRESHING 10 MORE MESSAGES WILL LOAD----
    private void loadMoreMessages() {
        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Messages message = snapshot.getValue(Messages.class);
                String messageKey = snapshot.getKey();

                if (!mPrevKey.equals(messageKey)) {
                    messagesList.add(itemPos++, message);

                } else {
                    mPrevKey = mLastKey;
                }

                if (itemPos == 1) {
                    String mMessageKey = snapshot.getKey();
                    mLastKey = mMessageKey;
                }


                mMessageAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);

                mLinearLayoutManager.scrollToPositionWithOffset(10, 0);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
