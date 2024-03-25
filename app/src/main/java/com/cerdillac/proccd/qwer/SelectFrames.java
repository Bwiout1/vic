package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class SelectFrames extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_frames);

        ImageView select_back = findViewById(R.id.select_back);
        ImageView img_select = findViewById(R.id.select_frame);
        TextView select_next = findViewById(R.id.select_next);
        select_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String data = intent.getDataString();
        img_select.setImageResource(Integer.parseInt(data));
        select_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SelectFrames.this, FramesEdit.class);
                intent1.setData(Uri.parse(data));
                startActivity(intent1);
            }
        });

    }
}