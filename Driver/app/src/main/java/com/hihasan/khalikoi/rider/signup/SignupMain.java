package com.hihasan.khalikoi.rider.signup;

import android.content.Intent;
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

public class SignupMain extends AppCompatActivity
{
    private EditText input_name,input_phone, input_password;
    private AppCompatButton btn_signup;
    private TextView link_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        input_name= findViewById (R.id.input_name);
        input_password= findViewById (R.id.input_password);
        input_phone= findViewById (R.id.input_phone);

        link_login= findViewById (R.id.link_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignupMain.this, SignInMain.class);
                startActivity(i);
            }
        });

        btn_signup= findViewById (R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your Name :"+input_name.getText()
                        +"\n Your Phone:"+input_phone
                +"\n Your password: "+input_password.getText(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
