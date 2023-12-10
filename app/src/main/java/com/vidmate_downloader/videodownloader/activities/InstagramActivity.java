package com.vidmate_downloader.videodownloader.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.tiktok.InstaDownload;

public class InstagramActivity extends AppCompatActivity {
    ImageView backBtn;
    LinearLayout instaBtn;

    EditText linkEdt;
    TextView downloadBtn;
    ImageView help1, help2, help3, help4;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);
        activity = this;

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(activity)
                .load(ContextCompat.getDrawable(this,R.drawable.intro01))
                .into(help1);

        Glide.with(activity)
                .load(R.drawable.intro02)
                .into(help2);

        Glide.with(activity)
                .load(R.drawable.intro03)
                .into(help3);

        Glide.with(activity)
                .load(ContextCompat.getDrawable(this,R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(activity)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(activity, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        final String url = linkEdt.getText().toString();

                        if (!Patterns.WEB_URL.matcher(url).matches() && !url.contains("instagram")) {
                            Toast.makeText(activity, R.string.invalid, Toast.LENGTH_SHORT).show();
                        } else {
                            InstaDownload.INSTANCE.startInstaDownload(url, activity);
                            linkEdt.getText().clear();
                        }

                    }
                }else {
                    Toast.makeText(activity, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }


        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void launchInstagram() {
        String instagramApp = "com.instagram.android";
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(instagramApp);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.instagram_not_found, Toast.LENGTH_SHORT).show();
        }
    }



}
