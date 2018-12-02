package com.hihasan.khalikoi.rider.confirm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hihasan.khalikoi.rider.R;
import com.hihasan.khalikoi.rider.signin.SignInMain;
import com.hihasan.khalikoi.rider.util.Value;

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

public class Single extends AppCompatActivity
{
    String ServerURL = "http://bachaw.com/api/ride.php" ;

    private TextView rider, rider_phone, car_types, ride_types, fare;
    String TempRider, TempPhone, TempCar, TempRide, TempFare;
    private AppCompatButton button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        rider=(TextView) findViewById (R.id.rider_name);
        rider_phone=(TextView) findViewById (R.id.rider_phone);
        car_types=(TextView) findViewById (R.id.car_types);
        ride_types=(TextView) findViewById (R.id.ride_types);
        fare=(TextView) findViewById (R.id.fare);

        rider.setText(Value.rider);
        rider_phone.setText(Value.rider_phone);
        car_types.setText(Value.car_types);
        ride_types.setText(Value.ride_types);
        fare.setText(String.valueOf(Value.fare));

        button=(AppCompatButton) findViewById (R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();

                InsertData(TempRider, TempPhone,TempCar, TempRide, TempFare);

//                Value.rider=username.getText().toString();
//                Value.rider_phone=email.getText().toString();
//                Value.password=password.getText().toString();

//                Intent i=new Intent(getApplicationContext(),SignInMain.class);
//                startActivity(i);

            }
        });
    }

    public void GetData(){

        TempRider = rider.getText().toString();
//        System.out.println(TempName);
        TempPhone = rider_phone.getText().toString();
//        System.out.println(TempEmail);
        TempCar = car_types.getText().toString();
//        System.out.println(TempPassword);
        TempRide=ride_types.getText().toString();
        TempFare=fare.getText().toString();

    }

    public void InsertData(final String rider, final String rider_phone,final String car_types,
                           final String ride_types, final String fare){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String RiderHolder = rider ;
                String PhoneHolder = rider_phone ;
                String CarHolder=car_types;
                String RideHolder=ride_types;
                String FareHolder=fare;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("rider", RiderHolder));
                nameValuePairs.add(new BasicNameValuePair("rider_phone", PhoneHolder));
                nameValuePairs.add(new BasicNameValuePair("car_types", CarHolder));
                nameValuePairs.add(new BasicNameValuePair("ride_types", RideHolder));
                nameValuePairs.add(new BasicNameValuePair("fare", FareHolder));


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

        sendPostReqAsyncTask.execute(rider,rider_phone,car_types,ride_types,fare);
    }

}
