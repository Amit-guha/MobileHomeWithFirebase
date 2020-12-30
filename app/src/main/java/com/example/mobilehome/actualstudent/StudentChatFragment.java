package com.example.mobilehome.actualstudent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilehome.R;
import com.example.mobilehome.studentfragment.ChatActivity;
import com.example.mobilehome.studentfragment.ChatFragment;
import com.example.mobilehome.studentfragment.Messages;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentChatFragment extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter mChatAdapter;

    private String mCurrent_user_id;

    private View mMainView;


    public StudentChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView= inflater.inflate(R.layout.fragment_chat, container, false);

        //--DEFINING RECYCLERVIEW OF THIS FRAGMENT---
        mConvList = (RecyclerView)mMainView.findViewById(R.id.chatFragmetn);

        //--GETTING CURRENT USER ID---
        mAuth= FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);


        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);

        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);


        //---SETTING LAYOUT FOR RECYCLER VIEW----
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);



        mConvList.setLayoutManager(linearLayoutManager);
        // mConvList.setHasFixedSize(true);
        // mConvList.setAdapter(mChatAdapter);


        //--RETURNING THE VIEW OF FRAGMENT--
        // mConvList.setAdapter(mChatAdapter);
        try{
            fetch();
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return mMainView;
    }


    private void fetch() {
        //--ORDERING THE MESSAGE BY TIME----
        Query conversationQuery = mConvDatabase.orderByChild("time_stamp");
        FirebaseRecyclerOptions<Messages> Both = new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(mMessageDatabase, Messages.class).build();

        mChatAdapter = new FirebaseRecyclerAdapter<Messages, StudentChatViewholder>(Both) {
            @NonNull
            @Override
            public StudentChatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater=LayoutInflater.from(parent.getContext());
                View v=inflater.inflate(R.layout.recycler_request,parent,false);
                return new StudentChatViewholder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final StudentChatViewholder holder, int position, @NonNull final Messages model) {
                final String last_user_id = getRef(position).getKey();
              //  Toast.makeText(getContext(), "Checking..."+last_user_id, Toast.LENGTH_SHORT).show();
                Query lastMessageQuery = mMessageDatabase.child(last_user_id).limitToLast(1);
                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String data = (String) snapshot.child("message").getValue();
                        holder.setMessage(data, model.isSeen());
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

                Log.d("Checking..", last_user_id);
                //Toast.makeText(getContext(), "" + last_user_id, Toast.LENGTH_SHORT).show();


                mUsersDatabase.child(last_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String userName = snapshot.child("name").getValue().toString();
                        //  String userThumb = snapshot.child("thumb_image").getValue().toString();

                        if(snapshot.hasChild("online")){

                            String userOnline = snapshot.child("online").getValue().toString();
                            // snapshot.setUserOnline(userOnline);

                        }
                        holder.setName(userName);

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chatIntent = new Intent(getContext(),StudentChatActivity.class);
                                chatIntent.putExtra("user_id",last_user_id);
                                chatIntent.putExtra("user_name",userName);
                                startActivity(chatIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        };

        mConvList.setAdapter(mChatAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mChatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatAdapter.stopListening();
    }


    public static class StudentChatViewholder extends RecyclerView.ViewHolder{

        View mView;

        TextView userNameView,userStatus;
        CircleImageView cv;

        public StudentChatViewholder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            userNameView=itemView.findViewById(R.id.textViewSingleListName);
            userStatus=itemView.findViewById(R.id.textViewSingleListStatus);
            cv=itemView.findViewById(R.id.circleImageViewUserImage);
        }

        public void setMessage(String message,boolean isSeen){

            TextView userStatusView = (TextView) mView.findViewById(R.id.textViewSingleListStatus);
            userStatusView.setText(message);

            //--SETTING BOLD FOR NOT SEEN MESSAGES---
            if(isSeen){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            }
            else{
                userStatusView.setTypeface(userStatusView.getTypeface(),Typeface.NORMAL);
            }

        }


        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListName);
            userNameView.setText(name);
        }


        public void setUserOnline(String onlineStatus) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.userSingleOnlineIcon);
            if(onlineStatus.equals("true")){
                userOnlineView.setVisibility(View.VISIBLE);
            }
            else{
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }

    }
}
