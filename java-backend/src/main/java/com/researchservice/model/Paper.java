package com.researchservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Paper {
    private String title;
    private String link;
    private String published;

    @JsonProperty("abstract")
    private String abstractText;  // map JSON "abstract" -> this field

    private String source;
    private double score; // computed by RankingService

    public Paper() {}

    public Paper(String title, String link, String published, String abstractText, String source) {
        this.title = title;
        this.link = link;
        this.published = published;
        this.abstractText = abstractText;
        this.source = source;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPublished() { return published; }
    public void setPublished(String published) { this.published = published; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
