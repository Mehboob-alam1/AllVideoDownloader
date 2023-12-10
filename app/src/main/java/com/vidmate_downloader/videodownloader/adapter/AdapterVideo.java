package com.vidmate_downloader.videodownloader.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vidmate_downloader.videodownloader.Model.DataModel;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.activities.FullImageActivity;
import com.vidmate_downloader.videodownloader.activities.VideoPreviewActivity;

import java.io.File;
import java.util.ArrayList;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.MyHolder> {


    ArrayList<DataModel> mData = new ArrayList<>();
    Context context;
    private File file;
    public AdapterVideo(ArrayList<DataModel> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.item_video, parent, false));
    }

    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        final DataModel jpast = (DataModel) this.mData.get(position);
        this.file = new File(jpast.getFilePath());
        if (!this.file.isDirectory()) {
            if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    Glide.with(this.context).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(1))).into(holder.image_view_post_item_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.playBtn.setVisibility(View.VISIBLE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
                holder.playBtn.setVisibility(View.GONE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                holder.playBtn.setVisibility(View.GONE);
                Glide.with(this.context).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(1))).into(holder.image_view_post_item_image);
            }
            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Utils.isImageFile(mData.get(holder.getAdapterPosition()).getFilePath())){
                        Intent intent = new Intent(context, VideoPreviewActivity.class);
                        intent.putExtra("videoPath", mData.get(holder.getAdapterPosition()).getFilePath());
                        context.startActivity(intent);
                    }else {
                        context.startActivity(new Intent(context, FullImageActivity.class)
                                .putExtra("link",mData.get(holder.getAdapterPosition()).getFilePath()));
                    }
                }
            });
        }
    }




    public int getItemCount() {
        return this.mData.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        final ImageView image_view_post_item_image;
        final ImageView playBtn;
        //final TextView text_view_item_post_title;

        public MyHolder(View itemView) {
            super(itemView);
            this.image_view_post_item_image = (ImageView) itemView.findViewById(R.id.image_view_post_item_image);
           // this.text_view_item_post_title = (TextView) itemView.findViewById(R.id.text_view_item_post_title);
            this.playBtn = (ImageView) itemView.findViewById(R.id.playNow);
        }
    }
}
