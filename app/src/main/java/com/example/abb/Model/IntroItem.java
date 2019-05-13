package com.example.abb.Model;

public class IntroItem {

    private String introTitle, introDesc;
    private int image;

    public IntroItem(){

    }

    public IntroItem(String introTitle, String introDesc, int image) {
        this.introTitle = introTitle;
        this.introDesc = introDesc;
        this.image = image;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public String getIntroDesc() {
        return introDesc;
    }

    public void setIntroDesc(String introDesc) {
        this.introDesc = introDesc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
