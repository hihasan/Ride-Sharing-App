package com.hihasan.khalikoi.rider.user.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.hihasan.khalikoi.rider.R;
import com.hihasan.khalikoi.rider.util.Value;
import com.hihasan.khalikoi.rider.util.HttpParse;

public class ProfileMain extends AppCompatActivity
{
    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Student Data from Id Sent from previous activity.
    String HttpURL = "http://bachaw.com/api/rider_profile.php";

    // Http URL for delete Already Open Student Record.
    //String HttpUrlDeleteRecord = "http://bachaw.com/api/rider_profile.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;
    TextView NAME,PHONE_NUMBER, PASSWORD;
    String NameHolder, NumberHolder, ClassHolder;
    Button UpdateButton, DeleteButton;
    String TempItem;
    ProgressDialog progressDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        NAME = (TextView)findViewById(R.id.name);
        PHONE_NUMBER = (TextView)findViewById(R.id.phone);
        PASSWORD= (TextView) findViewById (R.id.password);
        if (Value.rider==null){
            NAME.setText("Rider Name: Bachaw");
            PHONE_NUMBER.setText("Rider Phone: "+Value.rider_phone);
            PASSWORD.setText("Rider Password: "+Value.password);
        }

        else {
            NAME.setText("Rider Name: "+Value.rider);
            PHONE_NUMBER.setText("Rider Phone: "+Value.rider_phone);
            PASSWORD.setText("Rider Password: "+Value.password);
        }


        //CLASS = (TextView)findViewById(R.id.textClass);

        //UpdateButton = (Button)findViewById(R.id.buttonUpdate);
       // DeleteButton = (Button)findViewById(R.id.buttonDelete);

        //Receiving the ListView Clicked item value send by previous activity.
//        TempItem = getIntent().getStringExtra("ListViewValue");
//        Intent intent=getIntent();
//        String str=intent.getStringExtra("name");
//        PHONE_NUMBER.setText(str);
//        //Calling method to filter Student Record and open selected record.
//        HttpWebCall(Value.name);


    }






}
