package com.example.cegap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SeeSchedule extends AppCompatActivity {
    TableLayout table_layout;
    TableRow tr1, tr2,tr3,tr4;
    TextView tx_1classname, tx_1coursename, tx_1labname, tx_2classname, tx_2coursename, tx_2labname,
            temptx_1classname, temptx_1coursename, temptx_1labname, temptx_2classname, temptx_2coursename, temptx_2labname,temptx1,tx1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_schedule);

        table_layout = findViewById(R.id.tablel_ayout);
        new MyTask().execute();
    }

    public void panel(View view) {
        Intent intent=new Intent(getApplicationContext(),Panel.class);
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String stn_1classname,stn_ln,stn_1labname,stn_1coursename,stn_2coursename,stn_2classname,stn_2labname,thedays;


        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        String user_status;
        int userId;
        JSONObject arrayobj = null,jsonobj=null;
        JSONArray jsonarray=null;
        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {

            Singeltondata sd = Singeltondata.getInstance();


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/recieveschedule&" + sd.getUser_id());

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();

                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);

                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                final StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
                arrayobj = new JSONObject();
                jsonobj=new JSONObject(response.toString());



            }catch (Exception e){
            System.out.println(e.getMessage());
        }

                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {


                        try {
                            if (jsonobj.getString("Status").equals("OK")) {

                                tr3 = new TableRow(getApplicationContext());
                                tr4 = new TableRow(getApplicationContext());

                                temptx_1classname = new TextView(getApplicationContext());
                                temptx_1classname.setText(" Class");
                                temptx_1classname.setTextSize(16);
                                temptx_1classname.setAlpha(1);
                                temptx_1classname.setTypeface(null, Typeface.BOLD);
                                temptx_1classname.setTextColor(Color.BLACK);

                                tr3.addView(temptx_1classname);

                                temptx_1coursename = new TextView(getApplicationContext());
                                temptx_1coursename.setText(" Course");
                                temptx_1coursename.setTextSize(16);
                                temptx_1coursename.setAlpha(1);
                                temptx_1coursename.setTextColor(Color.BLACK);
                                temptx_1coursename.setTypeface(null, Typeface.BOLD);

                                tr3.addView(temptx_1coursename);


                                temptx_1labname = new TextView(getApplicationContext());
                                temptx_1labname.setText(" lab");
                                temptx_1labname.setTextSize(16);
                                temptx_1labname.setAlpha(1);
                                temptx_1labname.setTextColor(Color.BLACK);
                                temptx_1labname.setTypeface(null, Typeface.BOLD);
                                tr3.addView(temptx_1labname);

                                temptx_2classname = new TextView(getApplicationContext());
                                temptx_2classname.setText(" Class");
                                temptx_2classname.setTextSize(16);
                                temptx_2classname.setAlpha(1);
                                temptx_2classname.setTypeface(null, Typeface.BOLD);
                                temptx_2classname.setTextColor(Color.BLACK);

                                tr3.addView(temptx_2classname);

                                temptx_2coursename = new TextView(getApplicationContext());
                                temptx_2coursename.setText(" Course");
                                temptx_2coursename.setTextSize(16);
                                temptx_2coursename.setAlpha(1);
                                temptx_2coursename.setTextColor(Color.BLACK);
                                temptx_2coursename.setTypeface(null, Typeface.BOLD);

                                tr3.addView(temptx_2coursename);


                                temptx_2labname = new TextView(getApplicationContext());
                                temptx_2labname.setText(" Lab");
                                temptx_2labname.setTextSize(16);
                                temptx_2labname.setAlpha(1);
                                temptx_2labname.setTextColor(Color.BLACK);
                                temptx_2labname.setTypeface(null, Typeface.BOLD);

                                tr3.addView(temptx_2labname);


                                temptx1 = new TextView(getApplicationContext());
                                temptx1.setText(" DAYS");
                                temptx1.setTextSize(16);
                                temptx1.setAlpha(1);
                                temptx1.setTextColor(Color.BLACK);
                                temptx1.setTypeface(null, Typeface.BOLD);

                                tr4.addView(temptx1);

                                table_layout.addView(tr3);
                                table_layout.addView(tr4);

                                jsonarray = jsonobj.getJSONArray("DATA");

                                for (int i=0;i<jsonarray.length();i++) {

                                    tr1 = new TableRow(getApplicationContext());
                                    tr2 = new TableRow(getApplicationContext());

                                    tx1 = new TextView(getApplicationContext());

                                    tx_1classname = new TextView(getApplicationContext());
                                    tx_1classname.setTextColor(Color.BLACK);

                                    tx_1coursename = new TextView(getApplicationContext());
                                    tx_1coursename.setTextColor(Color.BLACK);

                                    tx_1labname = new TextView(getApplicationContext());
                                    tx_1labname.setTextColor(Color.BLACK);

                                    tx_2classname = new TextView(getApplicationContext());
                                    tx_2classname.setTextColor(Color.BLACK);

                                    tx_2coursename = new TextView(getApplicationContext());
                                    tx_2coursename.setTextColor(Color.BLACK);

                                    tx_2labname = new TextView(getApplicationContext());
                                    tx_2labname.setTextColor(Color.BLACK);

                                    tx1 = new TextView(getApplicationContext());
                                    tx1.setTextColor(Color.BLACK);


                                    arrayobj=jsonarray.getJSONObject(i);
                                    stn_1classname = arrayobj.getString("FIRSTCLASSNAME");
                                    stn_1coursename = arrayobj.getString("FIRSTCOURSENAME");
                                    stn_1labname = arrayobj.getString("FIRSTLABNAME");

                                    stn_2classname = arrayobj.getString("SECONDCLASSNAME");
                                    stn_2coursename = arrayobj.getString("SECONDCOURSENAME");
                                    stn_2labname = arrayobj.getString("SECONDLABNAME");
                                    thedays = arrayobj.getString("DAYS");


                                    tx_1classname.setTextSize(16);
                                    tx_1classname.setAlpha(1);
                                    tx_1classname.setText(" " + stn_1classname);

                                    tr1.addView(tx_1classname);

                                    tx_1coursename.setTextSize(16);
                                    tx_1coursename.setAlpha(1);
                                    tx_1coursename.setText(" " + stn_1coursename + " ");

                                    tr1.addView(tx_1coursename);

                                    tx_1labname.setTextSize(16);
                                    tx_1labname.setAlpha(1);
                                    tx_1labname.setText(" " + stn_1labname);

                                    tr1.addView(tx_1labname);


                                    tx_2classname.setTextSize(16);
                                    tx_2classname.setAlpha(1);
                                    tx_2classname.setText(" " + stn_2classname);

                                    tr1.addView(tx_2classname);

                                    tx_2coursename.setTextSize(16);
                                    tx_2coursename.setAlpha(1);
                                    tx_2coursename.setText(" " + stn_2coursename);

                                    tr1.addView(tx_2coursename);

                                    tx_2labname.setTextSize(16);
                                    tx_2labname.setAlpha(1);
                                    tx_2labname.setText(" " + stn_2labname);

                                    tr1.addView(tx_2labname);

                                    tx1.setTextSize(16);
                                    tx1.setAlpha(1);
                                    tx1.setText(" " + thedays);

                                    tr2.addView(tx1);


                                    tr1.setAlpha(1);
                                    tr2.setAlpha(1);
                                    tr2.setAlpha(1);


                                    table_layout.addView(tr1);
                                    table_layout.addView(tr2);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                });

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            if(getUser_status().equals("WRONG")){
//                Toast.makeText(getApplicationContext(),"THERE IS NO SCHEDULE",Toast.LENGTH_LONG).show();
//            }
        }

    }
}