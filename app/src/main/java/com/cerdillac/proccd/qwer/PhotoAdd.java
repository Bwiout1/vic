package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class PhotoAdd extends AppCompatActivity {

    private int [] frame_source = {
            R.drawable.pic_1,
            R.drawable.pic_2,
            R.drawable.pic_3,
            R.drawable.pic_4,
            R.drawable.pic_5,
            R.drawable.pic_6,
            R.drawable.pic_7,
            R.drawable.pic_8,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add);

        ImageView add_back = findViewById(R.id.add_back);
        add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView txt_g = findViewById(R.id.open_gallery);
        txt_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoAdd.this, MyGallery.class);
                startActivity(intent);
            }
        });

        RecyclerView re_addph = findViewById(R.id.addph_recyc);
        List<ImageSource> imagesource = new ArrayList<>();
        re_addph.setLayoutManager(new GridLayoutManager(this,4));
        AddAdapter frame_adpater = new AddAdapter(PhotoAdd.this);
        frame_adpater.setData(imagesource);
        re_addph.setAdapter(frame_adpater);
        for(int i =0;i<frame_source.length;i++){
            ImageSource item = new ImageSource();
            item.setId(frame_source[i]);
            imagesource.add(item);
        }
        frame_adpater.setListener(new ImageAdapter.onItemViewClickListener() {
            @Override
            public void onItemClick(int position) {
                ImageSource imageSource = imagesource.get(position);
                Intent intent = new Intent(PhotoAdd.this, SelectFrames.class);
                intent.setData(Uri.parse(String.valueOf(imageSource.getId())));
                startActivity(intent);
            }
        });



    }
}