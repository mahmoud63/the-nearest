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

        String [] arr=PublicParamaters.PlaceList.get(0).Image.split(",");

        ArrayList<String> arrayList=new ArrayList <>();
        arrayList.add(arr[0]);
        arrayList.add(arr[1]);
        arrayList.add(arr[2]);




        ListView listView=(ListView) vieW.findViewById(R.id.listView);

        listView.setAdapter(new CustomPhotoList(context,arrayList));

        
        return vieW;
    }

}
