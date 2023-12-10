/*
 * Copyright (c) 2023.  Hurricane Development Studios
 */

package com.vidmate_downloader.videodownloader.activities;

import static android.os.Build.VERSION.SDK_INT;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.fragments.StatusSaverMainFragOld;
import com.vidmate_downloader.videodownloader.fragments.StatusSaverMainFragment;


public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        if (SDK_INT >= 29) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new StatusSaverMainFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new StatusSaverMainFragOld()).commit();
        }
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}