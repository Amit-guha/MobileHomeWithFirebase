package com.example.mobilehome.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilehome.R;
import com.example.mobilehome.owner.Upload;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimilarDataAdapter extends RecyclerView.Adapter<SimilarDataAdapter.Edu> {

    private Context context1;
    private ArrayList<Upload>mSimilarData;

    private ONShopiFyClikLisitiner lisitiner;

    public SimilarDataAdapter(Context context1, ArrayList<Upload> mSimilarData) {
        this.context1 = context1;
        this.mSimilarData = mSimilarData;
    }

    @NonNull
    @Override
    public Edu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context1).inflate(R.layout.standard_layout_location,parent,false);
        return new Edu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Edu holder, int position) {

        final Upload upload=mSimilarData.get(position);
        holder.tvTotalPlace.setText(upload.getmHomeAddress());
        holder.tvTotalAmount.setText(upload.getmTaka());
        holder.tvGender.setText(upload.getmGender());

        Picasso.with(context1).load(upload.getmImgeUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.mimageview, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                Picasso.with(context1).load(upload.getmImgeUrl()).into(holder.mimageview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSimilarData.size();
    }

      class Edu extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mimageview;
        private TextView tvTotalAmount,tvTotalPlace,tvGender;

        public Edu(@NonNull View itemView) {
            super(itemView);

            mimageview=itemView.findViewById(R.id.imgfirst);

            tvTotalAmount=itemView.findViewById(R.id.Moneytv);
            tvTotalPlace=itemView.findViewById(R.id.PlaceTv);
            tvGender=itemView.findViewById(R.id.StatusTv);

            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {
             if (lisitiner != null) {
                 int position = getLayoutPosition();
                 if (position != RecyclerView.NO_POSITION) {
                     lisitiner.OnITEMCLICK(position);
                 }
             }
         }


     }

    public interface ONShopiFyClikLisitiner{
        void OnITEMCLICK(int position);
    }

    public void setOnItemClickListener(ONShopiFyClikLisitiner mlisitiner){
        lisitiner=mlisitiner;
    }
}
