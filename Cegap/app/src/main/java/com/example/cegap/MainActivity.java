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
import java.sql.Timestamp;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
EditText email,pass;
Button login,singup;
String stn_email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.editText_email_id);
        pass=findViewById(R.id.editText_password);
        login=findViewById(R.id.login);
        singup=findViewById(R.id.button_singup);

        Date date=new Date();
        Timestamp ts=new Timestamp(date.getTime());
        System.out.println(ts);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stn_email = email.getText().toString();
                password = pass.getText().toString();
                if(stn_email.equals("")||password.equals("")){stn_email="stn_email";
                    password="password";
                }
                MyTask my = new MyTask();
                my.execute();
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
            }
        });
    }



    private class MyTask extends AsyncTask<Void, Void, Void> {
        int fulluid;
        String user_status,check_email;

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public int getFulluid() {
            return fulluid;
        }

        public void setFulluid(int fulluid) {
            this.fulluid = fulluid;
        }

        public String getCheck_email() {
            return check_email;
        }

        public void setCheck_email(String check_email) {
            this.check_email = check_email;
        }

        @Override

        protected Void doInBackground(Void... params) {



            Singeltondata sd=Singeltondata.getInstance();

            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/login&" + stn_email  + "&" + password );

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

                user_status=obj.getString("Status");
                setUser_status(user_status);
                int userId = obj.getInt("User_id");
                String pass=obj.getString("Password");
                setCheck_email(obj.getString("email"));



                sd.setUser_id(userId);
                System.out.println(userId+" and password "+pass);



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


            System.out.println(""+getUser_status()+stn_email);


            if (getUser_status().equals("OK")) {
                if(getCheck_email().equals("rupi07@gmail.com")){

                    Intent bridge = new Intent(getApplicationContext(), Adminpanel.class);

                    Toast.makeText(getApplicationContext(), "You are successfully login", Toast.LENGTH_SHORT).show();
                    startActivity(bridge);
                }else{

                    Intent bridge = new Intent(getApplicationContext(), Panel.class);

                    Toast.makeText(getApplicationContext(), "You are successfully login", Toast.LENGTH_SHORT).show();
                    startActivity(bridge);
                }
            } else {

                Toast.makeText(getApplicationContext(), "PLEASE RECHECK YOUR EMAIL AND PASSWORD", Toast.LENGTH_LONG).show();
            }
        }

    }
}
