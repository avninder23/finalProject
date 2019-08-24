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

public class RecieverAppointment extends AppCompatActivity {
    TableLayout table_layout;
    TableRow tr1, tr2;
    TextView tx_fn,tx_email,tx_occ,tx1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever_appointment);

        table_layout=findViewById(R.id.tblayout1);
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String stn_fn;
        String stn_ln;
        String stn_occ;
        String stn_email;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        String user_status;
        int userId,appointment_id;

        @SuppressLint("WrongThread")
        @Override

        protected Void doInBackground(Void... params) {

            Singeltondata sd=Singeltondata.getInstance();


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/recieverappointment&"+sd.getUser_id());

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


                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {


                        try {
                            if( obj.getString("STATUS").equals("OK")){
                                final JSONArray postarray = obj.getJSONArray("DATA");
                            tr1 = new TableRow(getApplicationContext());

                            tr2 = new TableRow(getApplicationContext());
                            tx_fn = new TextView(getApplicationContext());
                            tx_fn.setText(" NAME");
                            tx_fn.setTextSize(20);
                            tx_fn.setAlpha(1);
                            tx_fn.setTypeface(null, Typeface.BOLD);
                            tx_fn.setTextColor(Color.BLACK);

                            tr1.addView(tx_fn);

                            tx_email = new TextView(getApplicationContext());
                            tx_email.setText(" EMAIL_ID");
                            tx_email.setTextSize(20);
                            tx_email.setAlpha(1);
                            tx_email.setTextColor(Color.BLACK);
                            tx_email.setTypeface(null, Typeface.BOLD);

                            tr1.addView(tx_email);


                            tx_occ = new TextView(getApplicationContext());
                            tx_occ.setText(" Occupation");
                            tx_occ.setTextSize(20);
                            tx_occ.setAlpha(1);
                            tx_occ.setTextColor(Color.BLACK);
                            tx_occ.setTypeface(null, Typeface.BOLD);
                            tr1.addView(tx_occ);

                            tx1 = new TextView(getApplicationContext());

                            tr2.addView(tx1);

                            table_layout.addView(tr1);
                            table_layout.addView(tr2);


                            JSONObject arrayobj = null;


                            for (int i = 0; i < postarray.length(); i++) {
                                tr1 = new TableRow(getApplicationContext());
                                tr2 = new TableRow(getApplicationContext());

                                tx1 = new TextView(getApplicationContext());

                                tx_fn = new TextView(getApplicationContext());
                                tx_fn.setTextColor(Color.BLACK);

                                tx_email = new TextView(getApplicationContext());
                                tx_email.setTextColor(Color.BLACK);

                                tx_occ = new TextView(getApplicationContext());
                                tx_occ.setTextColor(Color.BLACK);
                                try {
                                    arrayobj = postarray.getJSONObject(i);

                                    stn_fn=arrayobj.getString("FIRSTNAME");
                                    stn_ln=arrayobj.getString("LASTNAME");
                                    stn_email=arrayobj.getString("EMAIL_ID");
                                    stn_occ=arrayobj.getString("OCCUPATION");
                                    userId=arrayobj.getInt("SENDERUSER_ID");
                                    appointment_id=arrayobj.getInt("APPOINTMENTID");



                                    tx_fn.setTextSize(18);
                                    tx_fn.setAlpha(1);
                                    tx_fn.setText(stn_fn+" "+stn_ln+" ");
                                    tr1.addView(tx_fn);

                                    tx_email.setTextSize(18);
                                    tx_email.setAlpha(1);
                                    tx_email.setText(stn_email+" ");
                                    tr1.addView(tx_email);

                                    tx_occ.setTextSize(18);
                                    tx_occ.setAlpha(1);
                                    tx_occ.setText(stn_occ);
                                    tr1.addView(tx_occ);

                                    tr2.addView(tx1);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                tr1.setAlpha(1);
                                tr2.setAlpha(1);

                                table_layout.addView(tr1);
                                //table_layout.addView(tr2);


                                tr1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent bridge = new Intent(getApplicationContext(), AppointmentInformation.class);
                                        bridge.putExtra("userid", userId);

                                        bridge.putExtra("APPOINTMENTID",appointment_id);

                                        startActivity(bridge);
                                    }
                                });


                            }
                            }else{
                                Toast.makeText(getApplicationContext(),"THERE IS NO APPOINTMENTS FOR YOU",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),Appointment.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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