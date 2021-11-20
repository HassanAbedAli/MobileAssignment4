package com.example.mobileassignment4;

public class Article {

    private String name;
    private String url;


    @Override
    public String toString() {
        return name ;
    }

    public Article(String name, String url) {
        this.name = name;
        this.url = url;

    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
