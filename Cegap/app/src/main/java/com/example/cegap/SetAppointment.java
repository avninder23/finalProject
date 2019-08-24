package com.example.cegap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;

public class SetAppointment extends AppCompatActivity {

    Button btn_backtolist;
    TextView fn,ln,email_id,cn,occ,qual,dob,user_id;
    EditText edt_date,edt_time;
    String stn_date,stn_time,stn_datetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);

        fn=findViewById(R.id.textView_fn);
        ln=findViewById(R.id.textView_ln);
        dob=findViewById(R.id.textView_dob);
        occ=findViewById(R.id.textView_occ);
        qual=findViewById(R.id.textView_qual);
        user_id=findViewById(R.id.textView_userId);
        cn=findViewById(R.id.textView_cn);
        email_id=findViewById(R.id.textView_email);
        new MyTask().execute();

        SimpleDateFormat dateFormat=new SimpleDateFormat();

        edt_date=findViewById(R.id.editText_date);
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int  mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(SetAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                edt_date.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth   );

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        edt_time=findViewById(R.id.editText_time);
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int  mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(SetAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                edt_date.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth   );

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        btn_backtolist=findViewById(R.id.backtolist);
        btn_backtolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stn_date=edt_date.getText().toString();
                stn_time=edt_time.getText().toString();
                stn_datetime=stn_date+" "+stn_time+":00:000";

                if(stn_datetime.matches("(.*)-(.*)-(.*) (.*):(.*):(.*):(.*)")) {
                    new MyTask2().execute();
                    Intent intent = new Intent(getApplicationContext(), Panel.class);
                    startActivity(intent);

                }else{
                    System.out.println(stn_datetime);
                    edt_date.setHint("yyyy-mm-dd");
                    edt_time.setHint("HH:MM");
                    Toast.makeText(getApplicationContext(),"PLEASE DATE AND TIME AS MENTIONED....THANKS",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void searchapp(View view) {Intent intent=new Intent(getApplicationContext(),Appointment.class);
        startActivity(intent);
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


            Singeltondata sd=Singeltondata.getInstance();
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            user_Id=bundle.getInt("userid");
            //user_Id=sd.getUser_id();
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

                sd.setReciever_id(userId);
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


    private class MyTask2 extends AsyncTask<Void, Void, Void> {


        Singeltondata sd=Singeltondata.getInstance();

        String user_status;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/setappointment&"+stn_datetime+"&"+sd.getUser_id()+"&"+sd.getReciever_id());

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(getUser_status().equals("OK")){

                Toast.makeText(getApplicationContext(),"YOUR APPOINTMENT IS SUCCESSFULLY SET",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),Panel.class);
                startActivity(intent);
            }else if(getUser_status().equals("wrong")){

                Toast.makeText(getApplicationContext(),"PLEASE SELECT THE PROPER TIME",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),Appointment.class);
                startActivity(intent);
            }
        }
    }
}

