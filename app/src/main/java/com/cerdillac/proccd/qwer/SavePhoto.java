package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;



public class SavePhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_photo);

        Intent intent = getIntent();
        ImageView saved = findViewById(R.id.saved_ph);
        Uri uri = intent.getData();
        saved.setImageURI(uri);

        ImageView back = findViewById(R.id.saved_back);
        ImageView home = findViewById(R.id.saved_home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SavePhoto.this,HomeActivity.class);
                startActivity(intent1);
            }
        });

    }
}