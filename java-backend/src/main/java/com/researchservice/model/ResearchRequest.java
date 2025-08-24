package com.researchservice.model;

public class ResearchRequest {
    private String query;

    public ResearchRequest() {}
    public ResearchRequest(String query) { this.query = query; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}

