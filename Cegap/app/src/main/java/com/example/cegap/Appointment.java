package com.example.cegap;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Appointment extends AppCompatActivity {

    Button search,viewapt,recieveapt;
    EditText edt_search;
    String stn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        search=findViewById(R.id.search_btn);
        viewapt=findViewById(R.id.view_appointment);
        edt_search=findViewById(R.id.editT_email);

        recieveapt=findViewById(R.id.recieveappointment);

        recieveapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),RecieverAppointment.class);
                startActivity(intent);

            }
        });

        viewapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),SenderAppointment.class);
                startActivity(intent);

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stn_search=edt_search.getText().toString();
                if(stn_search.isEmpty()) {
                    edt_search.setText("");
                    Toast.makeText(getApplicationContext(),"PLEASE ENTER THE KEYWORD FOR SEARCH",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), SearchAppointment.class);
                    intent.putExtra("KEYWORD", stn_search);
                    startActivity(intent);
                }
            }
        });
    }

    public void apppanel(View view) {
        Intent intent=new Intent(getApplicationContext(),Panel.class);
        startActivity(intent);
    }
}
