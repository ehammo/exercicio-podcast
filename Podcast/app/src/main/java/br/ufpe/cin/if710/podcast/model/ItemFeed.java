package br.ufpe.cin.if710.podcast.model;

/**
 * Created by pedro on 13/12/17.
 */

public interface ItemFeed {
    String getTitle();
    String getLink();
    String getPubDate();
    String getDescription();
    String getDownloadLink();
    String getUri();
    int getCurrentTime();

    void setCurrentTime(int currentTime);
}
