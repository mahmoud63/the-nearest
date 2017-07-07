package com.example.mohammed.withoutname;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohammed on 03/07/2017.
 */

public class CustomPhotoList extends BaseAdapter {
    Context context;
    ArrayList<String> ImagesList;
    int i=0;

    CustomPhotoList(Context context, ArrayList<String> imagesList)
    {
        this.context=context;
        this.ImagesList=imagesList;
    }
    @Override
    public int getCount() {
        return ImagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return ImagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ImagesList.indexOf(getItem(position));
    }

    private class ViewHolder
    {
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.list_photo,parent,false);

        holder=new ViewHolder();

        holder.image=(ImageView) row.findViewById(R.id.imageShow);

        // PublicPlaces publicPlaces=placesList.get(position);

        Picasso.with(context)
                .load(ImagesList.get(position)).placeholder(R.mipmap.ic_launcher_round)
                .into(holder.image);



        return row;
    }
}
