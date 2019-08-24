package com.example.cegap;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Mio extends AppCompatActivity {

    EditText to, edit_message;
    Button send;
    String message,email_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mio);

        to = findViewById(R.id.to);
        send = findViewById(R.id.send);
        edit_message = findViewById(R.id.mess);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message=edit_message.getText().toString();
                email_id=to.getText().toString();

                new MyTask().execute();
            }
        });

    }

    public void messagepanel(View view) {
        Intent intent=new Intent(getApplicationContext(),Panel.class);
        startActivity(intent);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        String user_status=null;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        @Override

        protected Void doInBackground(Void... params) {


            Singeltondata sd=Singeltondata.getInstance();
            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/message&" + message+ "&"+sd.getUser_id()+
                        "&"+email_id);

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

            if(getUser_status().equals("OK")){
                Intent intent=new Intent(getApplicationContext(),Panel.class);
                Toast.makeText(getApplicationContext(), "MIO HAS BEEN SENT", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

            else{

                to.setText("");
                edit_message.setText("");
                Toast.makeText(getApplicationContext(), "Please put a valid Email_id", Toast.LENGTH_SHORT).show();
            }
        }

    }
}