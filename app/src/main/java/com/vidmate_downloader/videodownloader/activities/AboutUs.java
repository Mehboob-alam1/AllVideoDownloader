package com.vidmate_downloader.videodownloader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vidmate_downloader.videodownloader.R;

public class AboutUs extends AppCompatActivity {

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        activity=this;
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    activity.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                }
            }
        });
        findViewById(R.id.card_insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/lovevideodownlader")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.card_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/LoveDownloader?t=uqD9SlWlo1pAYPsy-df2nQ&s=09")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.card_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100093490945639&mibextid=ZbWKwL")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.card_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.card_youtube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@LoveDownloader?si=IksoUoz65ytY8iLr")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}