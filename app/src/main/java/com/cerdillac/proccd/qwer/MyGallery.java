package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

public class MyGallery extends AppCompatActivity {
    private List<String> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        List<ImageSource> imagesource = new ArrayList<>();
        ImageView img_back = findViewById(R.id.mg_back);
        RecyclerView recyclerView = findViewById(R.id.mg_recycle);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Localphoto localphoto = new Localphoto();
        photoList = localphoto.getLocalAlbumPhotos(MyGallery.this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ImageAdapter imageAdapter = new ImageAdapter(MyGallery.this);
        imageAdapter.setData(imagesource);
        recyclerView.setAdapter(imageAdapter);
        for(int i = 0;i<photoList.size();i++){
            ImageSource item = new ImageSource();
            item.setPath(photoList.get(i));
            imagesource.add(item);
        }
        imageAdapter.setListener(new ImageAdapter.onItemViewClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });

    }
}