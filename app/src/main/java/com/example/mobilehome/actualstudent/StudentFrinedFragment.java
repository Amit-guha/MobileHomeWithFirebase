package com.example.mobilehome.actualstudent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.ProfileActivity;
import com.example.mobilehome.studentfragment.ChatActivity;
import com.example.mobilehome.studentfragment.Friend;
import com.example.mobilehome.studentfragment.FriendFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFrinedFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;

    private FirebaseUser mCurrent_user_id;
    private FirebaseRecyclerAdapter adapter;

    private View mMainView;

    public StudentFrinedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView= inflater.inflate(R.layout.fragment_student_frined, container, false);

        //find recyclerview
        mFriendsList=mMainView.findViewById(R.id.friendFragment);
        mAuth=FirebaseAuth.getInstance();

        //---CURRENT USER ID--
        mCurrent_user_id=mAuth.getCurrentUser();
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mAuth.getUid());
        // mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        mFriendDatabase.keepSynced(true);


        //---USERS DATA
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

       // mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        fetch();

        return mMainView;

    }

    private void fetch() {
        //---FETCHING DATABASE FROM mFriendDatabase USING Friends.class AND ADDING TO RECYCLERVIEW---
        final FirebaseRecyclerOptions<Friend> Options=new FirebaseRecyclerOptions.Builder<Friend>()
                .setQuery(mFriendDatabase,Friend.class).build();

        adapter=new FirebaseRecyclerAdapter<Friend,FriendViewHolder>(Options){

            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater=LayoutInflater.from(parent.getContext());
                View v=inflater.inflate(R.layout.recycler_request,parent,false);
                return new FriendViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FriendViewHolder holder, int position, @NonNull Friend model) {
                final String userId=getRef(position).getKey();
                // Toast.makeText(getContext(), ""+userId, Toast.LENGTH_SHORT).show();
                Log.d("StudentFriendFragment",userId);
                mUsersDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //---IT WORKS WHENEVER CHILD OF mMessageDatabase IS CHANGED---
                        final String userName= (String) snapshot.child("name").getValue();
                        String userStatus= (String) snapshot.child("status").getValue();

                        holder.setName(userName);
                        // Picasso.with(getContext()).load(R.drawable.userperson).into(holder.cv);
                        //  holder.userStatus.setText(userStatus);

                        if(snapshot.hasChild("online")){
                            String userOnline = (String) snapshot.child("online").getValue();
                            holder.setOnline(userOnline);
                        }

                        // holder.userNameView.setText(userName);
                        // holder.setName(userName);
                        // Picasso.with(getContext()).load(R.drawable.userperson).into(holder.cv);


                        //--ALERT DIALOG FOR OPEN PROFILE OR SEND MESSAGE----
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence[] options = new CharSequence[]{"Open Profile" , "Send Message"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0){
                                            Intent intent=new Intent(getContext(), ProfileActivity.class);
                                            intent.putExtra("user_id",userId);
                                            startActivity(intent);
                                        }

                                        if(which == 1){

                                            //StudentChatActivity-------------like----------ChatActivity
                                            Intent intent = new Intent(getContext(), StudentChatActivity.class);
                                            intent.putExtra("user_id",userId);
                                            intent.putExtra("user_name",userName);
                                            //  Toast.makeText(getContext(), ""+userId+"\n"+userName, Toast.LENGTH_SHORT).show();

                                            startActivity(intent);
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                holder.setDate(model.getDate());
            }
        };

        mFriendsList.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private static class FriendViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView userNameView,userStatus;
        CircleImageView cv;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            userNameView=itemView.findViewById(R.id.textViewSingleListName);
            userStatus=itemView.findViewById(R.id.textViewSingleListStatus);
            cv=itemView.findViewById(R.id.circleImageViewUserImage);
        }

        public void setDate(String date){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListStatus);
            userNameView.setText(date);

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.textViewSingleListName);
            userNameView.setText(name);
        }

      /*  public void setUserImage(String userThumbImage, Context ctx){
            CircleImageView userImageview=(CircleImageView)mView.findViewById(R.id.circleImageViewUserImage);
            Picasso.with(ctx).load(userThumbImage).placeholder(R.drawable.user_img).into(userImageview);
        }*/

        public void setOnline(String isOnline){
            ImageView online=(ImageView)mView.findViewById(R.id.userSingleOnlineIcon);
            if(isOnline.equals("true")){
                online.setVisibility(View.VISIBLE);
            }
            else{
                online.setVisibility(View.INVISIBLE);
            }
        }
    }
}
