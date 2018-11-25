package com.hihasan.khalikoi.rider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PriceList extends AppCompatActivity
{
    private CardView c1,c2,c3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        TextView single_price=(TextView) findViewById (R.id.single_price);
        TextView shared_price=(TextView) findViewById (R.id.shared_price);
        TextView parcel=(TextView) findViewById (R.id.parcel_price);

        c1=(CardView) findViewById (R.id.single);
        c2=(CardView) findViewById (R.id.shared);
        c3=(CardView) findViewById(R.id.parcel);

        actionBtn();

        int  num=getRandomNumberInRange(4,25);

       int priceSingle= num*12;
        int priceShared=num*4;
        int priceParcel=num*4;
//
       String priceSingle_string=String.valueOf(priceSingle);
       String priceShared_string=String.valueOf(priceShared);
      String priceParcel_string=String.valueOf(priceParcel);
//
//    //    Toast.makeText(getApplicationContext(),priceParcel,Toast.LENGTH_LONG).show();
//
        single_price.setText(priceSingle_string);
//        System.out.println("Single"+priceSingle_string);
       shared_price.setText(priceShared_string);
//        System.out.println("Single2"+priceShared_string);
       parcel.setText(priceParcel_string);
//        System.out.println("Single3"+priceParcel_string);


    }

    private void actionBtn() {
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"No Driver Found :(", Toast.LENGTH_LONG).show();
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"No Driver Found :(", Toast.LENGTH_LONG).show();
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"No Driver Found :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
