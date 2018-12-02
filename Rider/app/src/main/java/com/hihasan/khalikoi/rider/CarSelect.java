package com.hihasan.khalikoi.rider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.hihasan.khalikoi.rider.util.Value;

public class CarSelect extends AppCompatActivity
{
    CardView c1,c2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        c1=(CardView) findViewById (R.id.taxi);
        c2=(CardView) findViewById (R.id.cng);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value.car_types="Taxi";
                Intent i= new Intent(CarSelect.this, PriceList.class);
                startActivity(i);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value.car_types="CNG";
                Intent i=new Intent(CarSelect.this, PriceList.class);
                startActivity(i);
            }
        });
    }
}
