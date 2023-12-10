package com.vidmate_downloader.videodownloader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.tiktok.TwitterVideoDownloader;

public class TwitterActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout tweatBtn;

    EditText linkEdt;
    TextView downloadBtn;
    ImageView help1, help2, help3, help4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twet);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(TwitterActivity.this)) {
                    final String URL = linkEdt.getText().toString();
                    if (URL.trim().length() == 0) {
                        Toast.makeText(TwitterActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {


                        TwitterVideoDownloader downloader = new TwitterVideoDownloader(TwitterActivity.this, URL);
                        downloader.DownloadVideo();
                        linkEdt.getText().clear();
                    }
                }else {
                    Toast.makeText(TwitterActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(TwitterActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.twet_1))
                .into(help1);

        Glide.with(TwitterActivity.this)
                .load(R.drawable.twet_2)
                .into(help2);

        Glide.with(TwitterActivity.this)
                .load(R.drawable.twet_3)
                .into(help3);

        Glide.with(TwitterActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.intro04))
                .into(help4);


    }


    private void openTwitter() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.twitter.android");
            this.startActivity(i);
        } catch (Exception var4) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.twitter.android")));
        }

    }
}
