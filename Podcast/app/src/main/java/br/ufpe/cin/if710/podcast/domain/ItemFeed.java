package br.ufpe.cin.if710.podcast.domain;

public class ItemFeed {
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;
    private final String uri;
    private int currentTime;

    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink, String item_uri) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = item_uri;
        this.currentTime=0;
    }

    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink, String item_uri, int currentTime) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = item_uri;
        this.currentTime=currentTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = "NONE";
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
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