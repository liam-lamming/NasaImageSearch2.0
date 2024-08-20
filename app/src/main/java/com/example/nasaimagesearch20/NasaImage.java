package com.example.nasaimagesearch20;

public class NasaImage {
    private String title;
    private String url;
    private String hdurl;
    private String date;
    private String explanation;
    private String copyright;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getHdurl() {
        return hdurl;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getCopyright() {
        return copyright;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
