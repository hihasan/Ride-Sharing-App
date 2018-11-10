package com.hihasan.khalikoi.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.hihasan.khalikoi.driver.help.Help;

public class MenuActivity extends AppCompatActivity
{
    CardView card_view1,card_view2,card_view3,card_view4,card_view5,card_view6;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_activity);

        card_view1=(CardView) findViewById (R.id.card_view1);
        card_view2=(CardView) findViewById (R.id.card_view2);
        card_view3=(CardView) findViewById (R.id.card_view3);
        card_view4=(CardView) findViewById (R.id.card_view4);
        card_view5=(CardView) findViewById (R.id.card_view5);
        card_view6=(CardView) findViewById (R.id.card_view6);

        action();
    }

    private void action() {
        card_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        card_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(MenuActivity.this, MainActivity.class);
//                startActivity(i);
                Toast.makeText(getApplicationContext(),"This Activity will open The driver completed rides",Toast.LENGTH_LONG).show();
            }
        });

        card_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(MenuActivity.this, MainActivity.class);
//                startActivity(i);
                Toast.makeText(getApplicationContext(),"This Activity will show delivered parcel list and on going percel dlivery",Toast.LENGTH_SHORT).show();
            }
        });

        card_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(MenuActivity.this, MainActivity.class);
//                startActivity(i);
                Toast.makeText(getApplicationContext(),"This will open payment due to system",Toast.LENGTH_SHORT).show();
            }
        });

        card_view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Profile",Toast.LENGTH_SHORT).show();
            }
        });

        card_view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this, Help.class);
                startActivity(i);
            }
        });
    }
}
