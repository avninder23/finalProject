package com.example.cegap;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
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

public class Signup extends AppCompatActivity {

    EditText fn,ln,dob,email_id,occ,qual,pass,cpass,cnumber;
    Button singup;
    String firstname,lastname,dateofbirth,email,occupation,qualification,password,contacctnumber,confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fn=findViewById(R.id.editText_fn);
        ln=findViewById(R.id.editText_ln);
        dob=findViewById(R.id.editText_dob);
        email_id=findViewById(R.id.editText_email_id);
        occ=findViewById(R.id.editText_occupation);
        qual=findViewById(R.id.editText_qualification);
        cnumber=findViewById(R.id.editText_contactnumber);
        pass=findViewById(R.id.editText_password);
        cpass=findViewById(R.id.editText_confirmpassword);
        singup=findViewById(R.id.signup);

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname=fn.getText().toString();
                lastname=ln.getText().toString();
                dateofbirth=dob.getText().toString();
                email=email_id.getText().toString();
                occupation=occ.getText().toString();
                qualification=qual.getText().toString();
                contacctnumber=cnumber.getText().toString();
                password=pass.getText().toString();
                confirmpassword=cpass.getText().toString();
                if (password.equalsIgnoreCase((confirmpassword))) {

                    new MyTask().execute();

                } else {
                    pass.setText("");

                    cpass.setText("");

                    Toast.makeText(getApplicationContext(), "Please Match your Password  ", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void main(View view) {Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {




        @Override

        protected Void doInBackground(Void... params) {


            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/singup&"
                        + firstname + "&" + lastname + "&" + dateofbirth + "&" + email + "&" + occupation +
                        "&" + qualification + "&" + contacctnumber + "&" + password);

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
                if (obj.getString("STATUS").equals("WRONG")) {
                    Toast.makeText(getApplicationContext(), "EMAIL is Already Register", Toast.LENGTH_LONG).show();
                    Intent bridge = new Intent(getApplicationContext(), Signup.class);
                    startActivity(bridge);
                }


                System.out.println(firstname);
//                DataInfo datainfo=DataInfo.getInstance();
//                datainfo.setUser_id(userID);


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
            Intent bridge = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(bridge);
            Toast.makeText(getApplicationContext(), "YOU ARE SUCCESSFULLY REGISTERED..THANKS", Toast.LENGTH_LONG).show();
        }
    }
    }