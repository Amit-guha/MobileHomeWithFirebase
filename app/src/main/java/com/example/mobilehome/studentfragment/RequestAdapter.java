package com.example.mobilehome.studentfragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewholder> {
    private List<String> requestList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;

    private Context mcontext;

    public RequestAdapter(List<String> requestList) {
        this.requestList = requestList;
    }


    @NonNull
    @Override
    public RequestViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_request,parent,false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        return new RequestViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewholder holder, int position) {

        String user_id=requestList.get(position);
        mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName=snapshot.child("name").getValue().toString();
                String userStatus=snapshot.child("status").getValue().toString();

                holder.displayName.setText(userName);
                holder.displayStatus.setText(userStatus);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class RequestViewholder extends RecyclerView.ViewHolder{
        public TextView displayName;
        public TextView displayStatus;
        public CircleImageView displayImage;
        public ImageView imageView;

        public RequestViewholder(@NonNull View itemView) {
            super(itemView);
            mcontext=itemView.getContext();

            displayImage=itemView.findViewById(R.id.circleImageViewUserImage);
            displayName=itemView.findViewById(R.id.textViewSingleListName);
            displayStatus=itemView.findViewById(R.id.textViewSingleListStatus);
            imageView=itemView.findViewById(R.id.userSingleOnlineIcon);
            imageView.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion=getLayoutPosition();
                   // Log.d("positon", String.valueOf(postion));
                    Toast.makeText(mcontext, ""+requestList.get(postion), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mcontext, ProfileActivity.class);
                    intent.putExtra("user_id",requestList.get(postion));
                    mcontext.startActivity(intent);
                }
            });


        }
    }
}
