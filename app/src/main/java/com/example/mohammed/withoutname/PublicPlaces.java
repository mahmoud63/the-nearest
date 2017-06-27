package com.example.mohammed.withoutname;

/**
 * Created by Mohammed on 13/06/2017.
 */

public class PublicPlaces {
    public double Lang;
    public double Lat;
    public String Name;
    public String Location;
    public String Description;
    public String Category;
    public String WebsiteUrl;
    public String Phone ;
    public String TagArray [];
    public String ImageUrl[];
    public String MenuUrl[];
    public String Logo;

    public PublicPlaces(String logo,String name)
    {
        this.Logo=logo;
        this.Name=name;
    }
    public PublicPlaces(String logo,String name,String location,double lang,double lat)
    {
        this.Logo=logo;
        this.Name=name;
        this.Location=location;
        this.Lang=lang;
        this.Lat=lat;
    }

    public PublicPlaces(double lang,double lat,String name,String location,String description,String category
    ,String websiteUrl,String phone,String tags[],String images[],String menu[])
    {
        this.Lang=lang;
        this.Lat=lat;
        this.Name=name;
        this.Location=location;
        this.Description=description;
        this.Category=category;
        this.WebsiteUrl=websiteUrl;this.Phone=phone;this.TagArray=tags;this.ImageUrl=images;this.MenuUrl=menu;
    }
}
