package com.example.mobilehome.multifileupload;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileShowAdapter extends RecyclerView.Adapter<FileShowAdapter.viwholder> {
    Context context;
    ArrayList<HashMap<String, String>> Datalist;
    ArrayList<HashMap<String, String>> Imagelist;

    public FileShowAdapter(Context context, ArrayList<HashMap<String, String>> datalist, ArrayList<HashMap<String, String>> Imagelist) {
        this.context = context;
        Datalist = datalist;
        this.Imagelist = Imagelist;
    }


    @NonNull
    @Override
    public viwholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.standard_layout_location, parent, false);
        return new viwholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final viwholder holder, int position) {
        // if (position == 0) {
        //holder.tvfile.setText(Datalist.get(position).get("text"));
        holder.tvStatus.setText(Datalist.get(position).get("text"));

        int i=0;
        for (i = 0; i < Imagelist.size(); i++) {
            Log.d("size", String.valueOf(Imagelist.size()));

            Log.d("countchecki", String.valueOf(i));
            Picasso.with(context).load(Imagelist.get(i).get("link" + String.valueOf(i))).into(holder.mFirstImg);
        }// Log.d("postion", Imagelist.get(position).get("link" + String.valueOf(position)));

    }

    @Override
    public int getItemCount() {
        return Datalist.size();
    }

    public class viwholder extends RecyclerView.ViewHolder {

        /*private ImageView img;
        private TextView tvfile;*/

        private TextView tvRoom, tvMoney, tvStatus, tvPlace;
        private ImageView mFirstImg, mSecondImg;

        public viwholder(@NonNull View itemView) {
            super(itemView);

           /* img = itemView.findViewById(R.id.iamgselect);
            tvfile = itemView.findViewById(R.id.filetv);*/

            // tvMoney=itemView.findViewById(R.id.Moneytv);
            tvStatus = itemView.findViewById(R.id.StatusTv);
            // tvPlace=itemView.findViewById(R.id.PlaceTv);

            mFirstImg = itemView.findViewById(R.id.imgfirst);
        }
    }
}
