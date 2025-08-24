package com.researchservice.model;

public class Citation {
    private String title;
    private String link;
    private int year;

    public Citation() {}

    public Citation(String title, String link, int year) {
        this.title = title;
        this.link = link;
        this.year = year;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
