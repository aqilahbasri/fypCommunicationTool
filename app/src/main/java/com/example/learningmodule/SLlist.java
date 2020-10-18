package com.example.learningmodule;


public class SLlist {
    private String imgurl;
    private String sldescription;


    public SLlist(){

    }

    public SLlist(String imgurl, String sldescription) {
        this.imgurl = imgurl;
        this.sldescription = sldescription;
    }


    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getSldescription() {
        return sldescription;
    }

    public void setSldescription(String sldescription) {
        this.sldescription = sldescription;
    }
}
