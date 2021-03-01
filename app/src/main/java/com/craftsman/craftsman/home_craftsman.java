package com.craftsman.craftsman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.craftsman.Login;
import com.craftsman.R;
import com.craftsman.Register;

public class home_craftsman extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_craftsman);

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_craftsman.this, profile_craftsman.class);
                startActivity(i);
            }
        });

        findViewById(R.id.work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_craftsman.this, works_craftsman.class);
                startActivity(i);
            }
        });
        findViewById(R.id.request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_craftsman.this, requests_in_craftsman.class);
                startActivity(i);
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_craftsman.this, Login.class);

                startActivity(i);
            }
        });

    }
}