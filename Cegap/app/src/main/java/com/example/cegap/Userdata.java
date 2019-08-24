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

public class Userdata extends AppCompatActivity {

    Button btn_backtolist;
    TextView fn,ln,email_id,cn,occ,qual,dob,user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);

        btn_backtolist=findViewById(R.id.backtolist);

        fn=findViewById(R.id.textView_fn);
        ln=findViewById(R.id.textView_ln);
        dob=findViewById(R.id.textView_dob);
        occ=findViewById(R.id.textView_occ);
        qual=findViewById(R.id.textView_qual);
        user_id=findViewById(R.id.textView_userId);
        cn=findViewById(R.id.textView_cn);
        email_id=findViewById(R.id.textView_email);
        new MyTask().execute();
        btn_backtolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Presentstaff.class);
                startActivity(intent);
            }
        });

    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        public String getStn_status() {
            return stn_status;
        }

        public void setStn_status(String stn_status) {
            this.stn_status = stn_status;
        }

        String stn_fn,stn_ln,stn_occ,stn_email,stn_qual,stn_cn,stn_dob,stn_status;
        int userId,user_Id;
        @Override

        protected Void doInBackground(Void... params) {


            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            user_Id=bundle.getInt("userid");
            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/userlist&"+ user_Id);

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

                stn_fn=obj.getString("FIRSTNAME");
                stn_ln=obj.getString("LASTNAME");
                stn_email=obj.getString("EMAIL_ID");
                stn_qual=obj.getString("QUALIFICATION");
                stn_occ=obj.getString("OCCUPATION");
                stn_dob=obj.getString("DATEOFBIRTH");
                stn_cn=obj.getString("CONTACTNUMBER");
                stn_status=obj.getString("Status");
                userId=obj.getInt("USER_ID");

                if(stn_cn.isEmpty()){
                    stn_cn="0";
                }

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

            if(getStn_status().equals("OK")){
                fn.setText(stn_fn);
                ln.setText(stn_ln);
                email_id.setText(stn_email);
                dob.setText(stn_dob);
                occ.setText(stn_occ);
                qual.setText(stn_qual);
                cn.setText(stn_cn);
                user_id.setText(userId+"");


            }



        }

    }
}
