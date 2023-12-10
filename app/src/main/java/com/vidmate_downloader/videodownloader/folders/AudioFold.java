package com.vidmate_downloader.videodownloader.folders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.vidmate_downloader.videodownloader.R;

public class AudioFold extends AppCompatActivity {

    Activity activity;
    TextView title;
    String val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_fold);
        activity = this;

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        title = findViewById(R.id.title);
        val = getIntent().getStringExtra("title");
        title.setText(""+val);
    }
}