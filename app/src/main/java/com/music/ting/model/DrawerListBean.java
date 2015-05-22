package com.music.ting.model;

/**
 * Created by Jun on 2015/5/16.
 */
public class DrawerListBean {

    private String title;
    private int titleImageId;

    public DrawerListBean(String title, int titleImageId){
        this.title = title;
        this.titleImageId = titleImageId;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitleImageId(int titleImageId){
        this.titleImageId = titleImageId;
    }
    public int getTitleImageId(){
        return titleImageId;
    }
}
