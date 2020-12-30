package com.example.mobilehome.student;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.Upload;
import com.example.mobilehome.owner.UploadImage1;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Upload> mdata;
    // private ArrayList<UploadImage1> msecondData;

    private static int TYPE_NOTFOUND = 2;
    private static int TYPE_DATAFOUND = 1;

    private OnItemCLICKLISITINER lisitiner;

    public StudenAdapter(Context context, ArrayList<Upload> mdata) {
        this.context = context;
        this.mdata = mdata;
        // this.msecondData = msecondData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_DATAFOUND) {
            v = LayoutInflater.from(context).inflate(R.layout.standard_layout_location, parent, false);
            return new StudentH(v);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.datanotfound, parent, false);
            return new NotViewholder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_DATAFOUND) {
            ((StudentH) holder).setData(mdata.get(position));

        } else {
            ((NotViewholder) holder).ntv.setText("Sorry we couldn't find any matches");
            Picasso.with(context).load(R.drawable.datanotfound).into(((NotViewholder) holder).nImgview);
        }


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class StudentH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvmoney, tvrom, tvstatus, tvPlace;
        private ImageView mfirst;

        public StudentH(@NonNull View itemView) {
            super(itemView);
            tvmoney = itemView.findViewById(R.id.Moneytv);
            //tvrom=itemView.findViewById(R.id.RoomTv);
            tvstatus = itemView.findViewById(R.id.StatusTv);
            tvPlace = itemView.findViewById(R.id.PlaceTv);

            mfirst = itemView.findViewById(R.id.imgfirst);
            // mSecond=itemView.findViewById(R.id.imgsecond);

            itemView.setOnClickListener(this);
        }

        void setData(final Upload u) {
            tvmoney.setText(u.getmTaka());
            // holder.tvrom.setText(u.getmRoom());
            tvstatus.setText(u.getmGender());
            tvPlace.setText(u.getmHomeAddress());

            Picasso.with(context).load(u.getmImgeUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(mfirst, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(u.getmImgeUrl()).into(mfirst);
                    Log.d("Crash", "Adapter.....");
                }
            });
        }


        @Override
        public void onClick(View v) {

            if (lisitiner != null) {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {
                    lisitiner.onItemClick(position);
                }
            }
        }


    }

    class NotViewholder extends RecyclerView.ViewHolder {

        private TextView ntv;
        private ImageView nImgview;

        public NotViewholder(@NonNull View itemView) {
            super(itemView);

            ntv = itemView.findViewById(R.id.ntvview);
            nImgview = itemView.findViewById(R.id.nImgview);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(mdata.get(position).getmPlace().trim()) || TextUtils.isEmpty(mdata.get(position).getmTaka().trim())) {
            return TYPE_NOTFOUND;
        } else {
            return TYPE_DATAFOUND;
        }
    }

    public interface OnItemCLICKLISITINER {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemCLICKLISITINER mlisitiner) {
        lisitiner = mlisitiner;
    }
}
