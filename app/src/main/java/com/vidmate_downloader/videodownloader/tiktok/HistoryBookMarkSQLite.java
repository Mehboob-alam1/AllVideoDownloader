/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.vidmate_downloader.videodownloader.tiktok;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryBookMarkSQLite extends SQLiteOpenHelper {
    private static final String VISITED_PAGES = "visited_pages";
    private static final String BOOKMARKED_PAGES = "bookmark_pages";
    private static final String TABS_PAGES = "tabs_pages";
    private static final String TABS_PAGES2 = "tabs_pages2";

    private static final String TITLE = "title";
    private static final String THUMNAIL = "thumb";
    private SQLiteDatabase dB;

    public HistoryBookMarkSQLite(Context context) {
        super(context, "his_book_tabsapp.db", null, 1);
        dB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE visited_pages (title TEXT, link TEXT, time TEXT)");
        db.execSQL("CREATE TABLE bookmark_pages (title TEXT, link TEXT, time TEXT)");
        db.execSQL("CREATE TABLE tabs_pages (title TEXT, link TEXT, time TEXT, thumb BLOB)");
        db.execSQL("CREATE TABLE tabs_pages2 (title TEXT, link TEXT, time TEXT, thumb BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nada

        db.execSQL("DROP TABLE IF EXISTS "+VISITED_PAGES);
        db.execSQL("DROP TABLE IF EXISTS "+BOOKMARKED_PAGES);
        db.execSQL("DROP TABLE IF EXISTS "+TABS_PAGES);
        db.execSQL("DROP TABLE IF EXISTS "+TABS_PAGES2);
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
           // clearHistory();
        }
    }

    public void addPageToHistory(VisitedPage page) {
        ContentValues v = new ContentValues();
        v.put(TITLE, page.title);
        v.put("link", page.link);
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm ss SSS",
                Locale.getDefault());
        v.put("time", simpleDateFormat.format(time));
        if (dB.update(VISITED_PAGES, v, "link = '" + page.link + "'", null) <= 0) {
            dB.insert(VISITED_PAGES, null, v);
        }
    }

    public void deleteFromHistory(String link) {
        dB.delete(VISITED_PAGES, "link = '" + link + "'", null);
    }

    public void clearHistory() {
        dB.execSQL("DELETE FROM visited_pages");
    }


    public List<VisitedPage> getAllVisitedPages() {
        Cursor c = dB.query(VISITED_PAGES, new String[]{TITLE, "link"}, null, null, null,
                null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
            pages.add(page);
        }
        c.close();
        return pages;
    }

    public List<VisitedPage> getVisitedPagesByKeyword(String keyword) {
        Cursor c = dB.query(VISITED_PAGES, new String[]{TITLE, "link"}, "title LIKE '%" +
                keyword + "%'", null, null, null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
            pages.add(page);
        }
        c.close();
        return pages;
    }


    public long addPageToBookMark(VisitedPage page) {
        ContentValues v = new ContentValues();
        v.put(TITLE, page.title);
        v.put("link", page.link);
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm ss SSS",
                Locale.getDefault());
        v.put("time", simpleDateFormat.format(time));
        if (dB.update(BOOKMARKED_PAGES, v, "link = '" + page.link + "'", null) <= 0) {
             return dB.insert(BOOKMARKED_PAGES, null, v);
        }else {
            return -1;
        }
    }
    public void deleteFromBookMark(String link) {
        dB.delete(BOOKMARKED_PAGES, "link = '" + link + "'", null);
    }
    public List<VisitedPage> getAllBookMarkedPages() {
        Cursor c = dB.query(BOOKMARKED_PAGES, new String[]{TITLE, "link"}, null, null, null,
                null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
            pages.add(page);
        }
        c.close();
        return pages;
    }
    public List<VisitedPage> getBookmarkPagesByKeyword(String keyword) {
        Cursor c = dB.query(BOOKMARKED_PAGES, new String[]{TITLE, "link"}, "link LIKE '%" +
                keyword + "%'", null, null, null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
            pages.add(page);
        }
        c.close();
        return pages;
    }
    public void clearBookmark() {
        dB.execSQL("DELETE FROM bookmark_pages");
    }

    public long addPageToTab(VisitedPage page) {
        ContentValues v = new ContentValues();
        v.put(TITLE, page.title);
        v.put("link", page.link);
      //  v.put("thumb", page.arr);
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm ss SSS",
                Locale.getDefault());
        v.put("time", simpleDateFormat.format(time));
        if (dB.update(TABS_PAGES, v, "link = '" + page.link + "'", null) <= 0) {
            return dB.insert(TABS_PAGES, null, v);
        }else {
            return -1;
        }
    }
    public List<VisitedPage> gettabPagesByKeyword(String keyword) {
        Cursor c = dB.query(TABS_PAGES, new String[]{TITLE, "link","thumb"}, "link LIKE '%" +
                keyword + "%'", null, null, null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
          //  page.arr = c.getBlob(c.getColumnIndex("thumb"));
            pages.add(page);
        }
        c.close();
        return pages;
    }
    public List<VisitedPage> getAllTabsPages() {
        Cursor c = dB.query(TABS_PAGES, new String[]{TITLE, "link","thumb"}, null, null, null,
                null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
          //  page.arr = c.getBlob(c.getColumnIndex("thumb"));
            pages.add(page);
        }
        c.close();
        return pages;
    }
    public void deleteFromTabPage(String link) {
        dB.delete(TABS_PAGES, "link = '" + link + "'", null);
    }
    public void clearTabs() {
        dB.execSQL("DELETE FROM tabs_pages");
    }


    public long addPageToTab2(VisitedPage page) {
        ContentValues v = new ContentValues();
        v.put(TITLE, page.title);
        v.put("link", page.link);
      //  v.put("thumb", page.arr);
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm ss SSS",
                Locale.getDefault());
        v.put("time", simpleDateFormat.format(time));
        if (dB.update(TABS_PAGES2, v, "link = '" + page.link + "'", null) <= 0) {
            return dB.insert(TABS_PAGES2, null, v);
        }else {
            return -1;
        }
    }
    public List<VisitedPage> gettabPagesByKeyword2(String keyword) {
        Cursor c = dB.query(TABS_PAGES2, new String[]{TITLE, "link","thumb"}, "link LIKE '%" +
                keyword + "%'", null, null, null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
          //  page.arr = c.getBlob(c.getColumnIndex("thumb"));
            pages.add(page);
        }
        c.close();
        return pages;
    }
    public List<VisitedPage> getAllTabsPages2() {
        Cursor c = dB.query(TABS_PAGES2, new String[]{TITLE, "link","thumb"}, null, null, null,
                null, "time DESC");
        List<VisitedPage> pages = new ArrayList<>();
        while (c.moveToNext()) {
            VisitedPage page = new VisitedPage();
            page.title = c.getString(c.getColumnIndex(TITLE));
            page.link = c.getString(c.getColumnIndex("link"));
           // page.arr = c.getBlob(c.getColumnIndex("thumb"));
            pages.add(page);
        }
        c.close();
        return pages;
    }
    public void deleteFromTabPage2(String link) {
        dB.delete(TABS_PAGES2, "link = '" + link + "'", null);
    }
    public void clearTabs2() {
        dB.execSQL("DELETE FROM tabs_pages2");
    }

}
