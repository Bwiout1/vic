package com.cerdillac.proccd.qwer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ContactHolder> {//
    private Context context;

    public List<ImageSource> data;



    private onItemViewClickListener listener;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ImageSource> data) {
        this.data = data;
    }


    public onItemViewClickListener getListener() {
        return listener;
    }

    public void setListener(onItemViewClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(context).inflate(R.layout.image_adpter_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {

        Glide.with(context)
                .load(data.get(position).getPath())
                .into(holder.Image);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {//绑定列表项点击事件
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);//点击跳转
            }
        });

    }

    @Override
    public int getItemCount() {
        return null != data ? data.size() : 0;//不为空返回数量，为空返回0
    }
    //Recyclerview适配器

    public class ContactHolder extends RecyclerView.ViewHolder {

        ImageView Image;//Recyclerview列表项图片
        ConstraintLayout constraintLayout;//Recyclerview列表项布局

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.image_adapter_view);
            constraintLayout = itemView.findViewById(R.id.image_adapter_layout);

        }
    }
    public interface onItemViewClickListener {
        void onItemClick(int position);
    }
}
