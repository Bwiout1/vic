package com.cerdillac.proccd.qwer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ContactHolder> {

    private Context context;

    public List<ImageSource> data;



    private ImageAdapter.onItemViewClickListener listener;

    public AddAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ImageSource> data) {
        this.data = data;
    }


    public ImageAdapter.onItemViewClickListener getListener() {
        return listener;
    }

    public void setListener(ImageAdapter.onItemViewClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public AddAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddAdapter.ContactHolder(LayoutInflater.from(context).inflate(R.layout.photo_add_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AddAdapter.ContactHolder holder, int position) {
        holder.Image.setImageResource(data.get(position).getId());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {//绑定列表项点击事件
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);//点击跳转

            }
        });

    }
    public class ContactHolder extends RecyclerView.ViewHolder {

        ImageView Image;//Recyclerview列表项图片
        ConstraintLayout constraintLayout;//Recyclerview列表项布局


        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.add_img);
            constraintLayout = itemView.findViewById(R.id.add_cons);
        }
    }

    @Override
    public int getItemCount() {
        return null != data ? data.size() : 0;//不为空返回数量，为空返回0
    }
    public interface onItemViewClickListener {
        void onItemClick(int position);
    }
}
