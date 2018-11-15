package com.example.android.finalnewsapp;

public class News {


    //Title of article
    private String aSectionId;

    //Section article belongs to
    private String aTitle;

    //Date published
    private String aDate;

    //Url
    private String aUrl;


    public News(String Title, String SectionID,  String Date, String url){
        aTitle = Title;
        aSectionId = SectionID;
        aDate = Date;
        aUrl = url;
    }



    public String getTitle(){
        return aTitle;
    }

    public String getSectionID(){
        return aSectionId;
    }

    public String getDate() {
        return aDate;
    }

    public String getUrl() {
        return aUrl;
    }
}


