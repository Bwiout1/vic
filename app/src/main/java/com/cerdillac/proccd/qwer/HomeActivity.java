package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView home_pe = findViewById(R.id.home_pe);
        TextView home_mg = findViewById(R.id.home_mg);
        home_pe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,FrameCate.class);
                startActivity(intent);
            }
        });
        home_mg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,PhotoAdd.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        startActivity(new Intent(HomeActivity.this, ExitActivity.class));
    }
}