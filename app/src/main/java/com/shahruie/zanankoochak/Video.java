package com.shahruie.zanankoochak;

/**
 * Created by samenta on 1/16/2017.
 */

public class Video {

    private  String name;
    private  String description;
    private String date;
    private int id ;
    private int phone ;
    private String email;
    private  String category ;
    private  String adv_img;

    public Video()
    {

    }

    public Video(String name , String description , String date , int id , int phone , String email , String category, String  adv_img)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.adv_img = adv_img;
    }

    public String getName()
    {
        return  name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return  description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getDate()
    {
        return  date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public int getId()
    {
        return  id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getPhone()
    {
        return  phone;
    }
    public void setPhone(int phone)
    {
        this.phone = phone;
    }
    public String getEmail()
    {
        return  email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getCategory()
    {
        return  category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getAdv_img(){
        return adv_img;
    }
    public void setAdv_img(String adv_img)
    {
        this.adv_img = adv_img;

    }
}
