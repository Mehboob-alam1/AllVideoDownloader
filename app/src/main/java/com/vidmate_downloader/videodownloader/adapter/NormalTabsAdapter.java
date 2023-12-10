package com.vidmate_downloader.videodownloader.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.activities.WebFragment;
import com.vidmate_downloader.videodownloader.home;
import com.vidmate_downloader.videodownloader.tiktok.HistoryBookMarkSQLite;
import com.vidmate_downloader.videodownloader.tiktok.VisitedPage;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.List;

public class NormalTabsAdapter extends RecyclerView.Adapter<NormalTabsAdapter.ViewHolder> {
    private Activity activity;
    private File file;
    List<VisitedPage> mData;
    String Type="";

    public NormalTabsAdapter(Activity paramActivity, List<VisitedPage> paramArrayList,String type) {
        this.mData = paramArrayList;
        this.activity = paramActivity;
        this.Type=type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.browser_tab_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
     //   holder.setIsRecyclable(false);
        if (mData.size()>0){
            VisitedPage jpast = (VisitedPage) this.mData.get(position);
          //  holder.ic_thumb.setImageBitmap(DbBitmapUtility.getImage(jpast.arr));
            holder.web.loadUrl(jpast.link);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ic_close;
        ShapeableImageView ic_thumb;
        WebView web;
        RelativeLayout rv_click;
        public ViewHolder(View itemView) {
            super(itemView);
            this.ic_close = itemView.findViewById(R.id.ic_close);
            this.ic_thumb = itemView.findViewById(R.id.ic_thumb);
            this.web = itemView.findViewById(R.id.web);
            this.rv_click = itemView.findViewById(R.id.rv_click);
            web.getSettings().setJavaScriptEnabled(true);
            web.setFocusableInTouchMode(false);
            web.setFocusable(false);
            web.setClickable(true);
            web.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            this.ic_close.setOnClickListener(this);
            this.web.setOnClickListener(this);
            this.itemView.setOnClickListener(this);
            this.rv_click.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.ic_close){
                HistoryBookMarkSQLite db = new HistoryBookMarkSQLite(activity);
                if (Type.equals("Tab")){
                    db.deleteFromTabPage(mData.get(getAdapterPosition()).link);
                }else {
                    db.deleteFromTabPage2(mData.get(getAdapterPosition()).link);
                }
                mData.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            }else {
                if (Type.equals("Tab")){
                  //  activity.startActivity(new Intent(activity, WebActivity.class).putExtra("url",mData.get(getAdapterPosition()).link));
                    Bundle bundle = new Bundle();
                    bundle.putString("url",mData.get(getAdapterPosition()).link);
                    WebFragment webFragment=new WebFragment();
                    webFragment.setArguments(bundle);
                    ((home)activity).getSupportFragmentManager().beginTransaction().replace(R.id.containerV,webFragment)
                            .addToBackStack("WebFragment").commit();
                }else {
                  //  activity.startActivity(new Intent(activity, WebActivity.class).putExtra("url",mData.get(getAdapterPosition()).link).putExtra("type","private"));
                    Bundle bundle = new Bundle();
                    bundle.putString("url",mData.get(getAdapterPosition()).link);
                    bundle.putString("type","private");
                    WebFragment webFragment=new WebFragment();
                    webFragment.setArguments(bundle);
                    ((home)activity).getSupportFragmentManager().beginTransaction().replace(R.id.containerV,webFragment)
                            .addToBackStack("WebFragment").commit();
                }
            }
        }
    }
}
