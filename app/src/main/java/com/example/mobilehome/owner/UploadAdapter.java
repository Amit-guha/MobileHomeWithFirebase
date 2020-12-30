package com.example.mobilehome.owner;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.UpViewholder> {
    Context context;
    ArrayList<Upload>mUploads;
  // ArrayList<UploadImage1>Second;
   private OnItemClickListener mListener;

    public UploadAdapter(Context context, ArrayList<Upload> mUploads) {
        this.context = context;
        this.mUploads = mUploads;
       // this.Second=Second;
    }

    @NonNull
    @Override
    public UpViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.standard_layout_location,parent,false);
        return new UpViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpViewholder holder, int position) {

        //First iamge and others data
        final Upload upload=mUploads.get(position);
       // holder.tvRoom.setText(upload.getmRoom());
        holder.tvMoney.setText(upload.getmTaka());
        holder.tvStatus.setText(upload.getmGender());
        holder.tvPlace.setText(upload.getmHomeAddress());

        Picasso.with(context).load(upload.getmImgeUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.mFirstImg, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(upload.getmImgeUrl()).into(holder.mFirstImg);
                Log.d("Crash","Adapter.....");
            }
        });

        //Second image and others data
     /* final  UploadImage1 image1=Second.get(position);
        Picasso.with(context).load(image1.getImageUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.mSecondImg, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(image1.getImageUrl()).into(holder.mSecondImg);
                Log.d("Crash","Adapter.....");
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    class UpViewholder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
            ,MenuItem.OnMenuItemClickListener {

        private TextView tvRoom,tvMoney,tvStatus,tvPlace;
        private ImageView mFirstImg,mSecondImg;
        private Button mBtn;


        public UpViewholder(@NonNull View itemView) {
            super(itemView);
           // tvRoom=itemView.findViewById(R.id.RoomTv);
            tvMoney=itemView.findViewById(R.id.Moneytv);
            tvStatus=itemView.findViewById(R.id.StatusTv);
            tvPlace=itemView.findViewById(R.id.PlaceTv);

            mFirstImg=itemView.findViewById(R.id.imgfirst);
           // mSecondImg=itemView.findViewById(R.id.imgsecond);

           itemView.setOnCreateContextMenuListener(this);
           itemView.setOnClickListener(this);


        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;
                       /* case 2:
                            mListener.onDeleteClick(position);
                            return true;*/
                    }
                }
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

          //  menu.setHeaderTitle("Select Action");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
           // MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
           // doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
       // void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
