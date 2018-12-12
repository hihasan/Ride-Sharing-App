package com.hihasan.khalikoi.driver.user.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hihasan.khalikoi.driver.R;
import com.hihasan.khalikoi.driver.util.HttpServicesClass;
import com.hihasan.khalikoi.driver.util.ListAdapterClassProfile;
import com.hihasan.khalikoi.driver.util.Value;

public class ProfileMain extends AppCompatActivity
{
    ListView StudentListView;
    ProgressBar progressBar;
    String HttpUrl = "http://bachaw.com/api/profile_driver.php";
    List<String> IdList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        StudentListView = (ListView)findViewById(R.id.listview1);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        new GetHttpResponse(ProfileMain.this).execute();

        //Adding ListView Item click Listener.
        StudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

//                Intent intent = new Intent(TripList.this,ShowSingleRecordActivity.class);
//
//                // Sending ListView clicked value using intent.
//                intent.putExtra("ListViewValue", IdList.get(position).toString());
//
//                startActivity(intent);

                //Finishing current activity after open next activity.
//                finish();
                Toast.makeText(getApplicationContext(),"Your profile",Toast.LENGTH_SHORT).show();

            }
        });
    }

    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String JSonResult;

        List<Value> studentList;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpUrl);
            try
            {
                httpServicesClass.ExecutePostRequest();

                if(httpServicesClass.getResponseCode() == 200)
                {
                    JSonResult = httpServicesClass.getResponse();

                    if(JSonResult != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            Value ride;

                            studentList = new ArrayList<Value>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                ride = new Value();

                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding Student Id TO IdList Array.
                                IdList.add(jsonObject.getString("id").toString());
                                System.out.println("Rider Phone: "+Value.rider_phone);
                                if (jsonObject.getString("driver_phone").toString().equals(Value.rider_phone)){
                                    //Adding Student Name.
                                    ride.RideListRider =("User Name: "+jsonObject.getString("driver_name").toString());
                                    System.out.println();
                                    ride.RideListDriver =("User Phone:"+jsonObject.getString("driver_phone").toString()) ;
                                    ride.RideListFare = ("Password: "+jsonObject.getString("password").toString());

                                    System.out.println("user "+ride.RideListRider);
                                    System.out.println("user "+ride.RideListDriver);
                                    System.out.println("user "+ride.RideListFare);
                                }

//                                else {
//                                    Toast.makeText(getApplicationContext(),"No Rides FOund",Toast.LENGTH_SHORT).show();
//                                }



                                studentList.add(ride);

                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.GONE);

            StudentListView.setVisibility(View.VISIBLE);

            ListAdapterClassProfile adapter = new ListAdapterClassProfile(studentList, context);

            StudentListView.setAdapter(adapter);

        }
    }
}
