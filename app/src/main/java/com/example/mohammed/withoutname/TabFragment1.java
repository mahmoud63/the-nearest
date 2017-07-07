package com.example.mohammed.withoutname;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class TabFragment1 extends Fragment {

    public Context context;


    public TabFragment1()
    {

    }

    public TabFragment1(Context contextl)
    {
        this.context=contextl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vieW=inflater.inflate(R.layout.activity_tab_fragment1, container, false);

        ArrayList<String> arrayList=new ArrayList <>();
        arrayList.add("http://www.planwallpaper.com/static/images/desktop-year-of-the-tiger-images-wallpaper.jpg");
        arrayList.add("lkl");
        arrayList.add("http://www.planwallpaper.com/static/images/Child-Girl-with-Sunflowers-Images.jpg");




        ListView listView=(ListView) vieW.findViewById(R.id.listView);

        listView.setAdapter(new CustomPhotoList(context,arrayList));



        return vieW;
    }

}
