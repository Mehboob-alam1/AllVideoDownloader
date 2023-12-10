/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.vidmate_downloader.videodownloader.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.tiktok.HistoryBookMarkSQLite;
import com.vidmate_downloader.videodownloader.tiktok.VisitedPage;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {


    private RecyclerView visitedPagesView;

    private List<VisitedPage> visitedPages;
    private HistoryBookMarkSQLite historySQLite;

    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        visitedPagesView = findViewById(R.id.rvHistoryList);
        ImageView clearHistory = findViewById(R.id.btn_delete_history);
        historySQLite = new HistoryBookMarkSQLite(this);

        type=getIntent().getStringExtra("type");
        if (type.equals("h")){
            ((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.history));
            visitedPages = historySQLite.getAllVisitedPages();
        }else {
            ((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.Favourites));
            visitedPages = historySQLite.getAllBookMarkedPages();
        }
        visitedPagesView.setAdapter(new VisitedPagesAdapter());
        visitedPagesView.setLayoutManager(new LinearLayoutManager(this));

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("h")){
                    ((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.history));
                    historySQLite.clearHistory();
                }else {
                    ((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.Favourites));
                    historySQLite.clearBookmark();
                }

                visitedPages.clear();
                visitedPagesView.getAdapter().notifyDataSetChanged();
                isHistoryEmpty();
            }
        });

        isHistoryEmpty();

    }

    private class VisitedPagesAdapter extends RecyclerView.Adapter<VisitedPagesAdapter.VisitedPageItem> {
        @Override
        public VisitedPageItem onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VisitedPageItem(LayoutInflater.from(getApplicationContext()).inflate(R.layout.history_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VisitedPageItem holder, int position) {
            holder.bind(visitedPages.get(position));
        }

        @Override
        public int getItemCount() {
            return visitedPages.size();
        }

        class VisitedPageItem extends RecyclerView.ViewHolder {
            private TextView title;
            private TextView subtitle;

            VisitedPageItem(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.row_history_title);
                subtitle = itemView.findViewById(R.id.row_history_subtitle);

                itemView.findViewById(R.id.row_history_menu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final PopupMenu popup = new PopupMenu(HistoryActivity.this, view);
                        popup.getMenuInflater().inflate(R.menu.history_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.history_delete:
                                        if (type.equals("h")){
                                            historySQLite.deleteFromHistory(visitedPages.get(getAdapterPosition()).link);
                                        }else {
                                            historySQLite.deleteFromBookMark(visitedPages.get(getAdapterPosition()).link);
                                        }
                                        visitedPages.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        isHistoryEmpty();
                                        break;
                                    case R.id.history_copy:
                                        Log.d("TAG", "onMenuItemClick: " + visitedPages.get(getAdapterPosition()).link);
                                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                        clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied URL", visitedPages.get(getAdapterPosition()).link));
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                });
            }

            void bind(VisitedPage page) {
                title.setText(page.title);
                subtitle.setText(page.link);
            }
        }
    }

    private void isHistoryEmpty(){
        if(visitedPages.isEmpty()){
            if (type.equals("h")){
                ((TextView)findViewById(R.id.tv_no)).setText(getString(R.string.no_history));
            }else {
                ((TextView)findViewById(R.id.tv_no)).setText(getString(R.string.no_bookmark));
            }
            findViewById(R.id.llNoHistory).setVisibility(View.VISIBLE);
            findViewById(R.id.llShowHistory).setVisibility(View.INVISIBLE);
        }else{
            findViewById(R.id.llNoHistory).setVisibility(View.INVISIBLE);
            findViewById(R.id.llShowHistory).setVisibility(View.VISIBLE);
        }
    }
}