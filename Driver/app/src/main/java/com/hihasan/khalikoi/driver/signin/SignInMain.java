package com.hihasan.khalikoi.driver.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hihasan.khalikoi.driver.MainActivity;
import com.hihasan.khalikoi.driver.R;

import org.w3c.dom.Text;

public class SignInMain extends AppCompatActivity
{
    private EditText phone,password;
    private AppCompatButton submit;
    private TextView link_signup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        phone= findViewById (R.id.input_phone);
        password= findViewById (R.id.input_password);

//        link_signup= findViewById (R.id.link_signup);

        submit= findViewById (R.id.btn_login);

//        link_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Correct Username & Password",Toast.LENGTH_SHORT).show();
//                Intent i=new Intent(SignInMain.this, SignupMain.class);
//                startActivity(i);
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText().toString().equalsIgnoreCase("017") &&
                        password.getText().toString().equalsIgnoreCase("1234"))
                {
                    Intent i=new Intent(SignInMain.this, MainActivity.class);
                    startActivity(i);
                }

                else {
                    Toast.makeText(getApplicationContext(),"Error User Phone Number or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
