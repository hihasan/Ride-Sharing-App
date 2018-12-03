package com.hihasan.khalikoi.driver.ride;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hihasan.khalikoi.driver.MainActivity;
import com.hihasan.khalikoi.driver.R;
import com.hihasan.khalikoi.driver.StartupActivity;
import com.hihasan.khalikoi.driver.util.HttpParse;
import com.hihasan.khalikoi.driver.util.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ShowSingleRecordActivity extends AppCompatActivity
{
    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Student Data from Id Sent from previous activity.
    String HttpURL = "http://bachaw.com/api/FilterStudentData.php";

    String HttpURLUpdate = "http://bachaw.com/api/update.php";
    ProgressDialog progressDialog;
    String IdHolder, StudentNameHolder, StudentPhoneHolder, StudentClassHolder;


    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();

    String FinalJSonObject ;
    TextView NAME,PHONE_NUMBER,CLASS;
    String NameHolder, NumberHolder, ClassHolder;
    AppCompatButton UpdateButton, DeleteButton;
    String TempItem;
    ProgressDialog progressDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_record);

        NAME = (TextView)findViewById(R.id.textName);
        PHONE_NUMBER = (TextView)findViewById(R.id.textPhone);
        CLASS = (TextView)findViewById(R.id.textClass);

//        IdHolder = getIntent().getStringExtra("Id");
//        StudentNameHolder = getIntent().getStringExtra("driver_name");
//        StudentPhoneHolder = getIntent().getStringExtra("driver_phone");
//        StudentName.setText(StudentNameHolder);
//        StudentPhoneNumber.setText(StudentPhoneHolder);

        UpdateButton = (AppCompatButton)findViewById(R.id.buttonUpdate);
        DeleteButton = (AppCompatButton)findViewById(R.id.buttonDelete);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");

        //Calling method to filter Student Record and open selected record.
        HttpWebCall(TempItem);


        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting data from EditText after button click.
                Intent intent=new Intent(ShowSingleRecordActivity.this, MainActivity.class);
//                intent.putExtra("Id", TempItem);
                GetDataFromEditText();

                // Sending Student Name, Phone Number, Class to method to update on server.
                StudentRecordUpdate(IdHolder,StudentNameHolder,StudentPhoneHolder);
                startActivity(intent);
            }

                // Finishing current activity after opening next activity.
                //finish();

        });

        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling Student delete method to delete current record using Student ID.
                Toast.makeText(getApplicationContext(),"Cancel Request",Toast.LENGTH_LONG).show();
                Intent i=new Intent(ShowSingleRecordActivity.this, AllRide.class);
                startActivity(i);

            }
        });

    }


//    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowSingleRecordActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleRecordActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("StudentID",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

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
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Student Name, Phone Number, Class into Variables.
                            NameHolder = jsonObject.getString("rider").toString() ;
                            NumberHolder = jsonObject.getString("rider_phone").toString() ;
                            ClassHolder = jsonObject.getString("fare").toString() ;
                            Value.rider_phone=NumberHolder;

                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            NAME.setText(NameHolder);
            PHONE_NUMBER.setText(NumberHolder);
            CLASS.setText(ClassHolder);

        }
    }

    //update functions
    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        IdHolder=TempItem;
        if (Value.name==null)
        {
            StudentNameHolder = "Bachaw";
        }
        else {
            StudentNameHolder = Value.name;
        }

        StudentPhoneHolder = Value.phone;
        System.out.println("Phone Number"+Value.phone);


    }

    // Method to Update Student Record.

    public void StudentRecordUpdate(final String ID, final String S_Name, final String S_Phone){

        class StudentRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ShowSingleRecordActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(ShowSingleRecordActivity.this,TempItem+" : "+StudentPhoneHolder+" : "+httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("rides_id",params[0]);

                hashMap.put("driver_name",params[1]);

                hashMap.put("driver_phone",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURLUpdate);

                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(ID,S_Name,S_Phone);
    }


}
