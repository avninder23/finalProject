package com.example.cegap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class    Adminpanel extends AppCompatActivity {
    Button btnschedule,btnenterschedule,btn_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);

        btnschedule=findViewById(R.id.btn_deletescedule);
        btnenterschedule=findViewById(R.id.btnenterschedule);

        btn_sign=findViewById(R.id.btn_singout);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnenterschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Schedule.class);
                startActivity(intent);
            }
        });

        btnschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DeleteSchedule.class);
                startActivity(intent);
            }
        });
    }
}
