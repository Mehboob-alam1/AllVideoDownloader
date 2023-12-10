package com.vidmate_downloader.videodownloader.folders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.vidmate_downloader.videodownloader.R;

public class FolderMain extends AppCompatActivity {

    Activity activity;
    LinearLayout myVideos,dowmloads,songs,movies,mySongs,myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_main);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        myVideos = findViewById(R.id.myVideos);
        dowmloads = findViewById(R.id.dowmloads);
        songs = findViewById(R.id.songs);
        movies = findViewById(R.id.movies);
        mySongs = findViewById(R.id.mySongs);
        myMovies = findViewById(R.id.myMovies);

        myVideos.setOnClickListener(v -> startActivity(new Intent(activity,AudioFold.class)
                .putExtra("title","My Videos")));
        dowmloads.setOnClickListener(v -> startActivity(new Intent(activity,AudioFold.class)
                .putExtra("title","Downloads")));
        songs.setOnClickListener(v -> startActivity(new Intent(activity,AudioFold.class)
                .putExtra("title","Songs")));
        movies.setOnClickListener(v -> startActivity(new Intent(activity,AudioFold.class)
                .putExtra("title","Movies")));
        mySongs.setOnClickListener(v -> startActivity(new Intent(activity,AudioFold.class)
                .putExtra("title","Songs")));
        myMovies.setOnClickListener(v -> startActivity(new Intent(activity,AudioFold.class)
                .putExtra("title","Movies")));
    }
}