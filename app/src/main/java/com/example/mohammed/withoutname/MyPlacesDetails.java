package com.example.mohammed.withoutname;

/**
 * Created by Mohammed on 11/05/2017.
 */

public class MyPlacesDetails {
    public int id;
    public  String title;
    public  String description;
    public  String longitude;
    public  String latitude;


    public MyPlacesDetails(int Id, String Title, String Description, String Longitude, String Latitude)
    {
        this.id=Id;
        this.title=Title;
        this.description=Description;
        this.longitude=Longitude;
        this.latitude=Latitude;

    }
}
