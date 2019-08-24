package com.example.cegap;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

public class Schedule extends AppCompatActivity {

    EditText fclassname,flabname,fcoursename,sclassname,slabname,scoursename,the_email;
    String firstclassname,firstlabname,firstcoursename,secondclassname,secondlabname,secondcoursename,days,theEmail;
    Button registerButton,homebutton;
    Spinner spin;
    String  data []=new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        fclassname=findViewById(R.id.editText_firstclassname);
        flabname=findViewById(R.id.editText_firstlabname);
        fcoursename=findViewById(R.id.editText_firstcoursename);

        sclassname=findViewById(R.id.editText_secondclassname);
        slabname=findViewById(R.id.editText_secondlabname);
        scoursename=findViewById(R.id.editText_secondcoursename);
        the_email=findViewById(R.id.editText);
        spin=findViewById(R.id.spinner);
       data[0]="MONDAY";
       data[1]="TUESDAY";
       data[2]="WEDNESDAY";
       data[3]="THURSDAY";
       data[4]="FRIDAY";
       data[5]="SATURDAY";
       data[6]="SUNDAY";

       Spinneradapter adapter=new Spinneradapter(getApplicationContext(),android.R.layout.simple_list_item_1);

        adapter.addAll(data);


        adapter.add("Please Select From List");
        spin.setAdapter(adapter);
        spin.setSelection(adapter.getCount());

       spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(spin.getSelectedItem().equals("Please Select From List")){

               }else if(spin.getSelectedItem().equals("MONDAY")){
                   days="D1";
               }else if(spin.getSelectedItem().equals("TUESDAY")){
                   days="D2";
               }else if(spin.getSelectedItem().equals("WEDNESDAY")){
                   days="D3";
               }else if(spin.getSelectedItem().equals("THURSDAY")){
                   days="D4";
               }else if(spin.getSelectedItem().equals("FRIDAY")){
                   days="D5";
               }else if(spin.getSelectedItem().equals("SATURDAY")){
                   days="D6";
               }else if(spin.getSelectedItem().equals("SUNDAY")){
                   days="D7";
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

               Toast.makeText(getApplicationContext(),"Please select item from list",Toast.LENGTH_LONG).show();
           }
       });

       homebutton=findViewById(R.id.button_home);
       homebutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(getApplicationContext(),Adminpanel.class);
               startActivity(intent);
           }
       });

        registerButton=findViewById(R.id.btn_register);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstclassname=fclassname.getText().toString();
                firstcoursename=fcoursename.getText().toString();
                firstlabname=flabname.getText().toString();

                secondclassname=sclassname.getText().toString();
                secondcoursename=scoursename.getText().toString();
                secondlabname=slabname.getText().toString();
                theEmail=the_email.getText().toString();

                if(firstclassname.isEmpty()){
                    fclassname.setHint("Class Name");
                }else if(firstlabname.isEmpty()){
                    flabname.setHint("Lab Name");
                }else if(firstcoursename.isEmpty()){
                    fcoursename.setHint("COURSE NAME");
                }else if(secondcoursename.isEmpty()){
                    scoursename.setHint("COURSE NAME");
                }else if(secondlabname.isEmpty()){
                    slabname.setHint("Lab Name");
                }else if(secondcoursename.isEmpty()){
                    scoursename.setHint("COURSE NAME");
                }else {

                    new MyTask().execute();

                }
            }
        });
    }

    public void panel(View view) {Intent intent=new Intent(getApplicationContext(),Adminpanel.class);
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        private String user_status;

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

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/enterschedule&"
                        + firstclassname + "&" + firstlabname + "&" + firstcoursename + "&" + secondclassname + "&" + secondlabname +
                        "&" + secondcoursename + "&" + theEmail+"&"+days );

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
                int userID = 0;
                // user_status=obj.getString("STATUS");
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
            if (getUser_status().equals("WRONG")) {
                Toast.makeText(getApplicationContext(), "YOU ARE SUCCESSFULLY REGISTER..THANKS", Toast.LENGTH_LONG).show();
                Intent bridge = new Intent(getApplicationContext(), Panel.class);
                startActivity(bridge);
            }else {
                fclassname.setHint("FIRST HALF CLASS NAME");
                flabname.setHint("FIRST HALF LAB NAME");
                fcoursename.setHint("FIRST HALF COURSE NAME");

                sclassname.setHint("SECOND HALF CLASS NAME");
                slabname.setHint("SECOND HALF LAB NAME");
                scoursename.setHint("SECOND HALF COURSE NAME");

                Intent intent=new Intent(getApplicationContext(),Schedule.class);
                Toast.makeText(getApplicationContext(),"YOU HAVE SUCCESSFULLY ENTER THE SCHEDULE ...THANKS",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }
    }


}
