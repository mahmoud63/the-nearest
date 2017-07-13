package com.example.mohammed.withoutname;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mohammed on 23/06/2017.
 */

public class PublicParamaters {
    public static String UserRootId,CategoryName,PlaceRootId;
    public static List<User>UserInfo=new ArrayList<>();
    public static ArrayList<PublicPlaces> PlaceList=new ArrayList <>();

    public static double lat,lon;

    public  static String Read(String file,Context context)
    {
        String Text="";
        try {
            FileInputStream fileInputStream = context.openFileInput(file);
            int size=fileInputStream.available();
            byte []buffer=new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            Text=new String(buffer);

        }
        catch (Exception ex)
        {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }
        return Text;
    }
    public static void Write(String file,String text,Context context)
    {
        try {
            FileOutputStream  fileOutputStream = context.openFileOutput (file,  Context.MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
            fileOutputStream.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }
    }
}
