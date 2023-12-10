package com.vidmate_downloader.videodownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

public class DownlaodActivity extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downlaod);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }
}