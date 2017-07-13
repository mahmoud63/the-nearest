package com.example.mohammed.withoutname;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.util.ArrayList;

/**
 * Created by Mohammed on 12/07/2017.
 */

public class PrivateCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> placesList;
    int i = 0;

    public PrivateCustomAdapter(Context context,ArrayList<String> placesList)
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
        return placesList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return placesList.indexOf(getItem(position));
    }
    private class ViewHolder
    {
       RoundedLetterView roundedLetterView;
        TextView placeTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PrivateCustomAdapter.ViewHolder holder=null;
        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.private_listview,parent,false);

        holder=new PrivateCustomAdapter.ViewHolder();



        holder.placeTitle=(TextView) row.findViewById(R.id.TV_Title);
        holder.roundedLetterView=(RoundedLetterView)row.findViewById(R.id.rlv_name_view);

        // PublicPlaces publicPlaces=placesList.get(position);


        holder.placeTitle.setText(placesList.get(position));
       holder.roundedLetterView.setTitleText(placesList.get(position).charAt(0)+"");


        return row;
    }
}
