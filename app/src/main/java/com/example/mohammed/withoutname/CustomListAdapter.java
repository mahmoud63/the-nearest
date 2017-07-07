package com.example.mohammed.withoutname;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohammed on 14/06/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    Context context;
    ArrayList<PublicPlaces> placesList;
    int i=0;

    CustomListAdapter(Context context, ArrayList<PublicPlaces> placesList)
    {
        this.context=context;
        this.placesList=placesList;
    }
    @Override
    public int getCount() {
        return placesList.size();
    }

    @Override
    public Object getItem(int position) {
        return placesList.get(position).Name;
    }

    @Override
    public long getItemId(int position) {
        return placesList.indexOf(getItem(position));
    }

    private class ViewHolder
    {
        ImageView logo_pic;
        TextView place_name,place_location,place_distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.list_item,parent,false);

            holder=new ViewHolder();


            holder.logo_pic=(ImageView) row.findViewById(R.id.Logo_Pic);
        holder.place_name=(TextView) row.findViewById(R.id.Name_Text);
        holder.place_location=(TextView) row.findViewById(R.id.Location_Text);
        holder.place_distance=(TextView) row.findViewById(R.id.Distance_Text);

          // PublicPlaces publicPlaces=placesList.get(position);

            Picasso.with(context)
                    .load(placesList.get(position).Logo).placeholder(R.mipmap.ic_launcher_round).resize(120,120)
                    .into(holder.logo_pic);
        holder.place_name.setText(placesList.get(position).Name);
        holder.place_location.setText(placesList.get(position).Location);
        holder.place_distance.setText(""+placesList.get(position).Distance+" Km");


        return row;
    }
}
