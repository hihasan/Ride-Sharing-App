package com.hihasan.projectmeta;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Handler;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StartupActivity extends AppCompatActivity
{
    private static final int app_load_time=3000;
    private ImageView i1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        i1=(ImageView) findViewById (R.id.startup_img);
        setAnimation();

        //Thread Handler for 5 sec.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(StartupActivity.this, MainActivity.class);
                StartupActivity.this.startActivity(mainIntent);
                StartupActivity.this.finish();
            }
        }, app_load_time);
    }

    private void setAnimation()
    {
        Animation animator= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        i1.startAnimation(animator);
    }
}
