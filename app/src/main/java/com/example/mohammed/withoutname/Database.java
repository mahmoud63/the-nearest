package com.example.mohammed.withoutname;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohammed on 10/05/2017.
 */

public class Database {
    static SQLiteDatabase myDB;
    static String Data = "";
    public static void CreateDatabase(Context context) {
        try {
            myDB = context.openOrCreateDatabase("Local", MODE_PRIVATE, null);
        } catch (Exception ex) {
            Toast.makeText(context, "Can not Create database", Toast.LENGTH_SHORT).show();
        }
    }

    public static void CreateTableSaves(String TableName) {
        try {


            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (ID INTEGER PRIMARY KEY   AUTOINCREMENT,Title VARCHAR, Description VARCHAR, longitude VARCHAR, latitude VARCHAR);");
            Log.d("Create Table Success","Create Table Success");
        } catch (Exception exc) {
            Log.d("Create Fail","Create Fail");
        }

    }

    public static void InsertIntoSaves(String TableName, String Title, String Description, String longitude,String latitude, Context context) {
        try {
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (Title, Description, longitude, latitude)"
                    + " VALUES ('" + Title + "', '" + Description + "','" + longitude + "','" + latitude + "');");
            Toast.makeText(context, "Seccesful Insert", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {

            Toast.makeText(context, "Fail Insert"+"\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    public static ArrayList<MyPlacesDetails> RetrivePlaces(Context context) {
        ArrayList<MyPlacesDetails>arrayList=new ArrayList<>();
        try {
            myDB = context.openOrCreateDatabase("Local", MODE_PRIVATE, null);
            String query = "SELECT * FROM 'Saves'";
            Cursor cursor = myDB.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id=Integer.parseInt(cursor.getString(0));
                    String title=cursor.getString(1);
                    String description=cursor.getString(2);
                    String longitude=cursor.getString(3);
                    String latitude=cursor.getString(4);
                    arrayList.add(new MyPlacesDetails(id,title,description,longitude,latitude));
                } while (cursor.moveToNext());
            }
            Toast.makeText(context, "Retrive Sucssufl", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(context,ex.getMessage() , Toast.LENGTH_LONG).show();
        }
        return arrayList;
    }

    public static void DeletePlace(int id,Context context)
    {
        try {
            myDB.execSQL("Delete From 'Saves' WHERE ID = '"+id+"';");
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        } catch (Exception exc) {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
