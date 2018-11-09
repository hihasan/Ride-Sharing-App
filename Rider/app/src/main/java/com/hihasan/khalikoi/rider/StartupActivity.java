package com.hihasan.khalikoi.rider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.hihasan.khalikoi.rider.signin.SignInMain;

public class StartupActivity extends AppCompatActivity
{
    public final int APP_LOADING_TIME=3000;
    private ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        logo= findViewById (R.id.logo);
        logoAnimation();
        //Thread Handler for 5 sec.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkConnection();
            }
        }, APP_LOADING_TIME);

    }

    //Animation
    private void logoAnimation()
    {
        Animation animator= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        logo.startAnimation(animator);
    }

    //Check connection Here
    //Here I need to check Login Status as well;

    private void checkConnection()
    {
        if(isOnline())
        {
            Toast.makeText(getApplicationContext(), "You are connected to Internet", Toast.LENGTH_SHORT).show();
            final Intent mainIntent = new Intent(StartupActivity.this, SignInMain.class);
            StartupActivity.this.startActivity(mainIntent);
            StartupActivity.this.finish();

        }

        else
        {
            Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            final Intent mainIntent = new Intent(StartupActivity.this, NoInternet.class);
            StartupActivity.this.startActivity(mainIntent);
            StartupActivity.this.finish();
        }
    }

    //Internet Connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
