package com.example.mohammed.withoutname;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabFragment2 extends Fragment {
    TextView name,category,description,location,tags,website,phone1,phone2;
    CircleImageView imageView;
    public Context context;


    public TabFragment2()
    {

    }

    public TabFragment2(Context contextl)
    {
        this.context=contextl;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_tab_fragment2, container, false);

        name=(TextView)view.findViewById(R.id.TV_Name);
        category=(TextView)view.findViewById(R.id.TV_Category);
        description=(TextView)view.findViewById(R.id.TV_Description);
        location=(TextView)view.findViewById(R.id.TV_Location);
        tags=(TextView)view.findViewById(R.id.TV_Tags);
        website=(TextView)view.findViewById(R.id.TV_Website);
        phone1=(TextView)view.findViewById(R.id.TV_Phone);
        phone2=(TextView)view.findViewById(R.id.TV_Phone2);
        imageView=(CircleImageView) view.findViewById(R.id.IV_Logo);


        Glide.with(context)
                .load(PublicParamaters.PlaceList.get(0).Logo)
                .into(imageView);
        name.setText(PublicParamaters.PlaceList.get(0).Name);
        category.setText(PublicParamaters.PlaceList.get(0).Category);
        description.setText(PublicParamaters.PlaceList.get(0).Description);
        location.setText(PublicParamaters.PlaceList.get(0).Location);
        tags.setText(PublicParamaters.PlaceList.get(0).Tag.replace("[","#").replace(", ", ",#").replace("]",""));
        website.setText(PublicParamaters.PlaceList.get(0).WebsiteUrl);
        phone1.setText(PublicParamaters.PlaceList.get(0).Phone.split(",")[0].replace("[",""));
        phone2.setText(PublicParamaters.PlaceList.get(0).Phone.split(",")[1].replace("]",""));





        return view;
    }
}