package com.arcbees.plugin.template.create.place;

public class NameToken {
    private boolean crawlable;
    private String token;
    
    public NameToken() {
    }

    public boolean getCrawlable() {
        return crawlable;
    }

    public void setCrawlable(boolean crawlable) {
        this.crawlable = crawlable;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
