package com.example.cegap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DeleteSchedule extends AppCompatActivity {

    Button btn_del;
     EditText edt_email;
     String stn_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_schedule);

        btn_del=findViewById(R.id.button_delete);
        edt_email=findViewById(R.id.editText_emaildel);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stn_email=edt_email.getText().toString();
                new MyTask().execute();
            }
        });

    }

    public void deletetoadmin(View view) {  Intent intent=new Intent(getApplicationContext(),Adminpanel.class);
        startActivity(intent);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {


        String delStatus;

        public String getDelStatus() {
            return delStatus;
        }

        public void setDelStatus(String delStatus) {
            this.delStatus = delStatus;
        }

        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/deleteschedule&"+ stn_email);

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

                setDelStatus(obj.getString("Status"));




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

            if(getDelStatus().equals("OK")){

                Intent intent=new Intent(getApplicationContext(),Adminpanel.class);
                Toast.makeText(getApplicationContext(),"SCHEDULE IS SUCCESFULLY REMOVED",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else{
            edt_email.setText("");
                Intent intent=new Intent(getApplicationContext(),Adminpanel.class);
                Toast.makeText(getApplicationContext(),"This email doesnot have any data",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }



        }

    }
