package com.example.mobilehome.multifileupload;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;

import java.util.List;

public class UploadCleassAdapter extends RecyclerView.Adapter<UploadCleassAdapter.ViewHolder> {

    public List<String>mFilelist;
    public List<String>mDonelist;

    public UploadCleassAdapter(List<String> mFilelist, List<String> mDonelist) {
        this.mFilelist = mFilelist;
        this.mDonelist = mDonelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlefile,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String fileNameview=mFilelist.get(position);
        holder.tv.setText(fileNameview);
    }

    @Override
    public int getItemCount() {
        return mFilelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv=itemView.findViewById(R.id.filetv);
            imageView=itemView.findViewById(R.id.iamgselect);


        }
    }
}
