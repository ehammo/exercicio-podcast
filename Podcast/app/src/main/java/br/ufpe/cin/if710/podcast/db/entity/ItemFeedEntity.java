package br.ufpe.cin.if710.podcast.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import br.ufpe.cin.if710.podcast.model.ItemFeed;

@Entity
public class ItemFeedEntity implements ItemFeed {
    private String title;
    private String link;
    private String pubDate;
    private String description;
    @PrimaryKey @NonNull
    private String downloadLink;
    private String uri;
    private int currentTime;

    public ItemFeedEntity(String title, String link, String pubDate, String description, String downloadLink, String uri, int currentTime) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = uri;
        this.currentTime = currentTime;
    }

    @Ignore
    public ItemFeedEntity(String title, String link, String pubDate, String description, String downloadLink) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = "NONE";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return title + "\n" +
                link + "\n" +
                pubDate + "\n" +
                description + "\n" +
                uri + "\n" +
                downloadLink + "\n";
    }
}