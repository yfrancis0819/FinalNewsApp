package com.example.android.finalnewsapp;

public class News {


    //Title of article
    private String aSection;

    //Section article belongs to
    private String aTitle;

    //Date published
    private String aDate;

    //Url
    private String aUrl;

    private String aAuthor;

    public News(String Title, String Section,  String Date, String url, String Author){
        aTitle = Title;
        aSection = Section;
        aDate = Date;
        aUrl = url;
        aAuthor = Author;
    }

    public String getTitle(){
        return aTitle;
    }

    public String getSection(){
        return aSection;
    }

    public String getDate() {
        return aDate;
    }

    public String getUrl() {
        return aUrl;
    }

    public String getAuthor() {return aAuthor;}
}


