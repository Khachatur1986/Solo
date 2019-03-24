package com.khach.feed.models;

public class PinnedModel {
    private int id;
    private String webTitle;
    private String sectionName;
    private String thumbnail;

    public PinnedModel() {
    }

    public PinnedModel(String webTitle, String sectionName, String thumbnail) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.thumbnail = thumbnail;
    }

    public PinnedModel(int id, String webTitle, String sectionName, String thumbnail) {
        this.id = id;
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "PinnedModel{" +
                "webTitle='" + webTitle + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
