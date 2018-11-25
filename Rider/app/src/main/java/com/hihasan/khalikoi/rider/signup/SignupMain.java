package com.hihasan.khalikoi.rider.signup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hihasan.khalikoi.rider.R;
import com.hihasan.khalikoi.rider.signin.SignInMain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignupMain extends AppCompatActivity
{
    String ServerURL = "http://bachaw.com/api_update/rider_signup_api.php" ;
    EditText username, email,password ;
    TextView login;
    AppCompatButton button;
    String TempName, TempEmail,TempPassword ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText)findViewById(R.id.input_name);
        email = (EditText)findViewById(R.id.input_phone);
        password=(EditText)findViewById(R.id.input_password);

        login=(TextView) findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignupMain.this, SignInMain.class);
                startActivity(i);
            }
        });

        button = (AppCompatButton) findViewById(R.id.btn_signup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();

                InsertData(TempName, TempEmail,TempPassword);

                Intent i=new Intent(getApplicationContext(),SignInMain.class);
                startActivity(i);

            }
        });
    }

    public void GetData(){

        TempName = username.getText().toString();

        TempEmail = email.getText().toString();
        TempPassword = password.getText().toString();

    }

    public void InsertData(final String username, final String email,final String password){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = username ;
                String EmailHolder = email ;
                String PasswordHolder=password;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("username", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("password", PasswordHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(username, email,password);
    }
}
