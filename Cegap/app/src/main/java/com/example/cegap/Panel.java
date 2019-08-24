package com.example.cegap;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Panel extends AppCompatActivity {

    Button btn_mio,btn_appointment,btn_schedule,btn_presentstaff,btn_attendance,btn_singout,btn_myprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        btn_mio=findViewById(R.id.mio);
        btn_appointment=findViewById(R.id.appintment);
        btn_attendance=findViewById(R.id.attendance);
        btn_schedule=findViewById(R.id.schedule);
        btn_presentstaff=findViewById(R.id.presentstaff);
        btn_myprofile=findViewById(R.id.myprofile2);
        btn_singout=findViewById(R.id.signout_btn);

        btn_mio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Mio.class);
                startActivity(intent);
            }
        });

        btn_presentstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Presentstaff.class);
                startActivity(intent);
            }
        });
        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Attendence.class);
                startActivity(intent);
            }
        });
        btn_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Appointment.class);
                startActivity(intent);
            }
        });

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),SeeSchedule.class);
                startActivity(intent);
            }
        });

        btn_myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Myprofile.class);
                startActivity(intent);
            }
        });
        btn_singout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
