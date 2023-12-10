package com.vidmate_downloader.videodownloader.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.database.MyDatabase;
import com.vidmate_downloader.videodownloader.fragments.HomeFragment;

import java.util.ArrayList;

public class BrowserHistory extends AppCompatActivity {

    MyDatabase database = new MyDatabase(this);
    RecyclerView recyclerView;
    HomeFragment homeFragment;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        activity = this;

        homeFragment = new HomeFragment();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyDateAdapter adapter = new MyDateAdapter(database.getAllData());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        findViewById(R.id.deleteAllHistoryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(BrowserHistory.this);
                alertdialog.setTitle("Confirmation...!");
                alertdialog.setMessage("Do you want to delete all the history ???");
                alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.removeAllData();
                    }
                });

                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = alertdialog.create();
                alert.show();
            }
        });

    }

    private class MyDateAdapter extends RecyclerView.Adapter<MyDateAdapter.MyDateViews> {
        ArrayList<String> urls;

        MyDateAdapter(ArrayList<String> urls) {
            this.urls = urls;
        }

        @Override
        public MyDateViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();

            View loadedRow = inflater.inflate(R.layout.history_row, parent, false);
            return new MyDateViews(loadedRow);
        }

        @Override
        public void onBindViewHolder(MyDateViews holder, int position) {
            String url = urls.get(position);
            holder.UrlTv.setText(url);

            holder.openLinkBtn.setOnClickListener(v -> {
               // homeFragment.openLink(url);
                finish();
            });

            holder.deleteBtn.setOnClickListener(v -> database.removeData(url));

        }

        @Override
        public int getItemCount() {
            return urls.size();
        }

        private class MyDateViews extends RecyclerView.ViewHolder {
            TextView UrlTv;
            ImageButton openLinkBtn, deleteBtn;

            public MyDateViews(View itemView) {
                super(itemView);

                UrlTv = itemView.findViewById(R.id.UrlTv);
                openLinkBtn = itemView.findViewById(R.id.openUrlBtn);
                deleteBtn = itemView.findViewById(R.id.deleteUrlBtn);

            }
        }
    }
}