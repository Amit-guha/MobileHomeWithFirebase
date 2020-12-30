package com.example.mobilehome.studentfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private Context context;


    //-----GETTING LIST OF ALL MESSAGES FROM CHAT ACTIVITY ----
    public MessageAdapter(List<Messages> mMessagesList) {
        this.mMessagesList = mMessagesList;
    }


    //---CREATING SINGLE HOLDER AND RETURNING ITS VIEW---
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagelayout2, parent, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        return new MessageViewHolder(view);

    }

    //----SETTING EACH HOLDER WITH DATA----
    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        // String current_user_id = mAuth.getCurrentUser().getUid();
        Messages mes = mMessagesList.get(position);
        final String from_user_id = mes.getFrom().toString();
        String message_type = mes.getType();


        //----CHANGING TIMESTAMP TO TIME-----
        long timeStamp = mes.getTime();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String cal[] = calendar.getTime().toString().split(" ");
        String time_of_message = cal[1] + "," + cal[2] + "  " + cal[3].substring(0, 5);
        Log.e("TIME IS : ", calendar.getTime().toString());


        holder.displayTime.setText(time_of_message);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(message_type);
        //---ADDING NAME THUMB_IMAGE TO THE HOLDER----
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                holder.displayName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.messageText.setText(mes.getMessage());


    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }


    //----RETURNING VIEW OF SINGLE HOLDER----
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView displayName;
        public TextView displayTime;
        public CircleImageView profileImage;
        public ImageView messageImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text_layout);
            displayName = itemView.findViewById(R.id.name_text_layout);
            displayTime = itemView.findViewById(R.id.time_text_layout);
            // profileImage=itemView.findViewById(R.id.message_profile_layout);

            context = itemView.getContext();
/*
            //---DELETE FUNCTION---
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence options[] = new CharSequence[]{ "Delete","Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete this message");
                    builder.setItems(options, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0){
                                *//*
                                        ....CODE FOR DELETING THE MESSAGE IS YET TO BE WRITTEN HERE...
                                 *//*
                                long mesPos = getLayoutPosition();
                                String mesId = mMessagesList.get((int)mesPos).toString();
                                Log.e("Message Id is ", mesId);
                                Log.e("Message is : ",mMessagesList.get((int)mesPos).getMessage());

                            }
                            if(which == 1){

                            }

                        }
                    });


                    builder.show();
                    return true;
                }
            });*/
        }
    }


}
