package com.hihasan.khalikoi.rider.util;

import android.content.Context;
import java.util.List;
import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hihasan.khalikoi.rider.R;

public class ListAdapterClassProfile extends BaseAdapter
{

    Context context;
    List<Value> valueList;

    public ListAdapterClassProfile(List<Value> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItemProfile viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItemProfile();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.activity_profile, null);

            viewItem.TextViewStudentName = (TextView)convertView.findViewById(R.id.name);
            viewItem.TextDriverName = (TextView)convertView.findViewById(R.id.phone);
            viewItem.TextFare = (TextView)convertView.findViewById(R.id.password);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItemProfile) convertView.getTag();
        }

        viewItem.TextViewStudentName.setText(valueList.get(position).RideListRider);
        viewItem.TextDriverName.setText(valueList.get(position).RideListDriver);
        viewItem.TextFare.setText(valueList.get(position).RideListFare);

        return convertView;
    }
}

class ViewItemProfile
{
    TextView TextViewStudentName;
    TextView TextDriverName;
    TextView TextFare;

}