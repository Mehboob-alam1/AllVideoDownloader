package com.vidmate_downloader.videodownloader.tiktok;

public interface VideoDownloader {

//    String createDirectory();

    String getVideoId(String link);

    void DownloadVideo();
}
