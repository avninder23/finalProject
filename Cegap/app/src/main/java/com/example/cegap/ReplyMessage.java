package com.example.cegap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ReplyMessage extends AppCompatActivity {

    TextView email;
    EditText mess;
    Button reply;
    String message;
    String emailID;
    Singeltondata sd=Singeltondata.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_message);
        email=findViewById(R.id.textView_email);
        mess=findViewById(R.id.editText_mess);
        reply=findViewById(R.id.btn_rply);

        emailID=sd.getEmail();
        email.setText(emailID);

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               message= mess.getText().toString();
               new MyTask().execute();
            }
        });

    }

    public void panel(View view) {
        Intent intent=new Intent(getApplicationContext(),Panel.class);
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String user_status;






        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }


        Singeltondata sd=Singeltondata.getInstance();


        @Override

        protected Void doInBackground(Void... params) {






            URL url = null;

            try {

                url = new URL("http://192.168.2.30:19950/Cegapp/mobile/application/reply&" +emailID+"&"+message+"&"+sd.getUser_id());

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


                Intent intent=new Intent(
                        getApplicationContext(),RecieveMessage.class
                );
                Toast.makeText(getApplicationContext(),"YOUR REPLAY IS SUCCESSFULLY SEND",Toast.LENGTH_LONG).show();
                startActivity(intent);


            }



        }}
}
