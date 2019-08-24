package com.example.cegap;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Attendence extends AppCompatActivity {

    TextView textview_date,textview_time;
    Button btn_clock;
    String currentdate,currenttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        btn_clock=findViewById(R.id.clockin);
        textview_date=findViewById(R.id.date);
        textview_time=findViewById(R.id.time);

        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        currentdate=simpleDateFormat.format(date).toString();
        textview_date.setText(currentdate);
        System.out.println(simpleDateFormat.format(date));

        SimpleDateFormat simpletimeformat=new SimpleDateFormat("HH:mm:SS:SSS");
        currenttime=simpletimeformat.format(date);
        textview_time.setText(currenttime);

        btn_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MyTask().execute();
            }
        });

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String user_status=null;

        public String getUser_status() {
            return user_status;
        }

        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        @Override

        protected Void doInBackground(Void... params) {


            Singeltondata sd=Singeltondata.getInstance();
            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/attendence&"+sd.getUser_id()
                        );

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();

                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);

                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj = new JSONObject(response.toString());
                setUser_status(obj.getString("STATUS"));
                setMessage(obj.getString("Message"));


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (getUser_status().equals("OK")) {
                Toast.makeText(getApplicationContext(), getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Panel.class);
                startActivity(intent);
            } else {


                Toast.makeText(getApplicationContext(), "Please put a valid Email_id", Toast.LENGTH_SHORT).show();
            }
        }}

    }

