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

public class Presentstaff extends AppCompatActivity {

    TableLayout tablelayout;
    TableRow tr1, tr2;
    TextView user_id,tx1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentstaff);
        tablelayout=findViewById(R.id.tablelayout);
        new MyTask().execute();
    }

    public void panel(View view) {
        Intent intent=new Intent(getApplicationContext(),Panel.class);
        startActivity(intent);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String firstname;

        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/presentstaff");

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
                final JSONObject obj = new JSONObject(response.toString());

                final JSONArray postarray = obj.getJSONArray("DATA");
                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {


                        tr1 = new TableRow(getApplicationContext());

                        tr2 = new TableRow(getApplicationContext());
                        user_id = new TextView(getApplicationContext());
                        user_id.setText(" USER_ID");
                        user_id.setTextSize(20);
                        user_id.setAlpha(1);
                        user_id.setTextColor(Color.BLACK);
                        //email.setTextColor(R.color.colorPrimaryDark);
                        tr1.addView(user_id);


                        tx1 = new TextView(getApplicationContext());

                        tr2.addView(tx1);

                        tablelayout.addView(tr1);
                        tablelayout.addView(tr2);

                        int user_Id=0;
                        JSONObject arrayobj = null;


                        for (int i = 0; i < postarray.length(); i++) {
                            tr1 = new TableRow(getApplicationContext());
                            tr2 = new TableRow(getApplicationContext());

                            tx1 = new TextView(getApplicationContext());

                            user_id = new TextView(getApplicationContext());
                            user_id.setTextColor(Color.BLACK);
                            try {
                                arrayobj = postarray.getJSONObject(i);



                                user_Id=arrayobj.getInt("USER_ID");
                                user_id.setTextSize(20);
                                user_id.setAlpha(1);

                                user_id.setText(user_Id + "  ");
                                tr1.addView(user_id);

                                tr2.addView(tx1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            tr1.setAlpha(1);
                            tr2.setAlpha(1);

                            tablelayout.addView(tr1);
                            tablelayout.addView(tr2);
                            //tablelayout.addView(tr2);

                            final int tempuser_id = user_Id;
                            tr1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent bridge = new Intent(getApplicationContext(), Userdata.class);
                                    bridge.putExtra("userid", tempuser_id);
                                    startActivity(bridge);
                                }
                            });


                        }


                    }
                });
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

}
