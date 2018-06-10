package com.example.exwork.bean;

/**
 * Created by Administrator on 2018/5/31.
 */

public class News {
    private String imgUrl;
    private String title;
    private String content;//作者
    private String date;
    private String themetype;//标签

    public String getThemetype() {
        return themetype;
    }

    public void setThemetype(String themetype) {
        this.themetype = themetype;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public News() {
        this.imgUrl = imgUrl;
        this.title = title;
        this.content = content;
        this.date = date;
        this.themetype = themetype;
    }
}
