package com.example.mohammed.withoutname;

/**
 * Created by Mohammed on 13/06/2017.
 */

public class PublicPlaces {

    public String Name;
    public double Lang;
    public double Lat;
    public String Location;
    public String Description;
    public String Category;
    public String WebsiteUrl;
    public String Phone;
    public String Tag;
    public String Image;
    public String Logo;
    public String City;
    public String OwnId;
    public String WorkHour;
    public float Distance;
    public String RootId;



    public PublicPlaces(String logo,String name,String rootId)
    {
        this.Logo=logo;
        this.Name=name;
        this.RootId=rootId;
    }
    public PublicPlaces(String logo,String name,String location,double lang,double lat,float distance,String tag
    ,String description,String image,String phone,String websiteUrl,String category)
    {
        this.Logo=logo;
        this.Name=name;
        this.Location=location;
        this.Lang=lang;
        this.Lat=lat;
        this.Distance=distance;
        this.Tag=tag;
        this.Description=description;
        this.Image=image;
        this.Phone=phone;
        this.WebsiteUrl=websiteUrl;
        this.Category=category;
    }

    //Search Cons
    public PublicPlaces(float distance, String logo,String name
            ,String description,String location,String websiteUrl,String workHour,
                        String tag,String imageUrl,String phoneArray)
    {
        this.Distance=distance;
        this.Logo=logo;
        this.Name=name;
        this.Description=description;this.Location=location;this.WebsiteUrl=websiteUrl;this.WorkHour=workHour;
        this.Tag=tag;this.Image=imageUrl;this.Phone=phoneArray;
    }
}
