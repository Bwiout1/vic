package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;



import java.util.ArrayList;
import java.util.List;

public class FrameCate extends AppCompatActivity {
    private int [] frame_source = {
            R.drawable.pic_1,
            R.drawable.pic_2,
            R.drawable.pic_3,
            R.drawable.pic_4,
            R.drawable.pic_5,
            R.drawable.pic_6,
            R.drawable.pic_7,
            R.drawable.pic_8,
            R.drawable.pic_9,
            R.drawable.pic_10,
            R.drawable.pic_11,
            R.drawable.pic_12,
            R.drawable.pic_13,
            R.drawable.pic_14,
            R.drawable.pic_15,
            R.drawable.pic_16,
            R.drawable.pic_17,
            R.drawable.pic_18,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_cate);


        ImageView frame_back = findViewById(R.id.frame_back);
        frame_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        List<ImageSource> imagesource = new ArrayList<>();
        RecyclerView frame_recyclerView = findViewById(R.id.frame_recycle);
        frame_recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        Frame_Adpater frame_adpater = new Frame_Adpater(this);
        frame_adpater.setData(imagesource);
        frame_recyclerView.setAdapter(frame_adpater);
        for(int i =0;i<frame_source.length;i++){
            ImageSource item = new ImageSource();
            item.setId(frame_source[i]);
            imagesource.add(item);
        }
        frame_adpater.setListener(new ImageAdapter.onItemViewClickListener() {
            @Override
            public void onItemClick(int position) {
                ImageSource imageSource = imagesource.get(position);
                Intent intent = new Intent(FrameCate.this, SelectFrames.class);
                intent.setData(Uri.parse(String.valueOf(imageSource.getId())));
                startActivity(intent);
            }
        });



    }
}