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

public class Singlemessage extends AppCompatActivity {

    TextView fn,date,email,cn,occ,qual,mess;
    Button list,btnreply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemessage);

        fn=findViewById(R.id.tv_fn);
        date=findViewById(R.id.tv_date);
        email=findViewById(R.id.tv_email);
        cn=findViewById(R.id.tv_contactnumber);
        occ=findViewById(R.id.tv_occupation);
        qual=findViewById(R.id.tv_qualification);
        mess=findViewById(R.id.tv_message);
        list=findViewById(R.id.back);
        btnreply=findViewById(R.id.rply);
        btnreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ReplyMessage.class);
               startActivity(intent);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        new MyTask().execute();
    }



    public void mestopro(View view) {  Intent intent=new Intent(getApplicationContext(),Panel.class);
        startActivity(intent);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        String user_status;
        String stndate;

        public String getStnemail() {
            return stnemail;
        }

        public void setStnemail(String stnemail) {
            this.stnemail = stnemail;
        }

        String stnemail;
        String stnfn;
        String stnln;
        String stncn;
        String stnmess;
        String stnqual;
        String stnocc;



        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }


        Singeltondata sd=Singeltondata.getInstance();


        @Override

        protected Void doInBackground(Void... params) {




            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
           int msgId= bundle.getInt("messageid");


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/message&" +msgId);

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
                System.out.println("dfnksdjkfbkdsjbfjbsdjk");
                setUser_status(obj.getString("Status"));

                if (getUser_status().equals("OK")){


                    stnfn=obj.getString("FIRSTNAME");
                    stnln=obj.getString("LASTNAME");
                    stnmess=obj.getString("Message");
                    stnemail=obj.getString("EMAIL_ID");
                    sd.setEmail(stnemail);
                    stndate=obj.getString("date");
                    stncn=obj.getString("CONTACTNUMBER");
                    stnqual=obj.getString("QUALIFICATION");
                    stnocc=obj.getString("OCCUPATION");


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


            if (getUser_status().equals("OK")){

                fn.setText(stnfn+" "+stnln);
                occ.setText(stnocc);
                qual.setText(stnqual);
                cn.setText(stncn);
                email.setText(stnemail);
                mess.setText(stnmess);
                date.setText(stndate);



            }


    }}
}
