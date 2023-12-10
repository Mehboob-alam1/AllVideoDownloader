package com.vidmate_downloader.videodownloader.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vidmate_downloader.videodownloader.Model.DataModel;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.activities.FullImageActivity;
import com.vidmate_downloader.videodownloader.activities.VideoPreviewActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Activity activity;
    private File file;
    ArrayList<DataModel> mData;

    public DownloadAdapter(Activity paramActivity, ArrayList<DataModel> paramArrayList) {
        this.mData = paramArrayList;
        this.activity = paramActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final DataModel jpast = (DataModel) this.mData.get(position);
        this.file = new File(jpast.getFilePath());

        if (!this.file.isDirectory()) {

            Log.e("eeee",file.toString());
            holder.videoName.setText(mData.get(position).getFileName());
            holder.folderName.setText(Utils.formatFileSize(this.file.length())+" | "+Utils.VideoType(this.file.getPath())+" | "+Utils.getVideoDate(this.file.lastModified()));
            if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    Glide.with(activity)
                            .asBitmap()
                            .load(this.file)
                            .apply(new RequestOptions().placeholder(R.drawable.test).centerCrop())
                            .into(holder.videoImg);
                   // Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(1))).into(holder.videoImg);
                } catch (Exception e) {
                    e.printStackTrace();
                  //  Log.e("eeee",e.toString());
                }
              //  holder.imagePlayer.setVisibility(View.VISIBLE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
              //  holder.imagePlayer.setVisibility(View.GONE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                holder.duration.setVisibility(View.GONE);
              //  holder.imagePlayer.setVisibility(View.GONE);
                Glide.with(activity)
                        .asBitmap()
                        .load(this.file)
                        .apply(new RequestOptions().placeholder(R.drawable.test).centerCrop())
                        .into(holder.videoImg);
            }

/*            holder.deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(holder.getAdapterPosition(), activity);
                }
            });

            holder.shareIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    share(jpast.getFilePath(), activity);
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView videoName,folderName,duration;
        ShapeableImageView videoImg;

        public ViewHolder(View itemView) {
            super(itemView);

            this.videoName = itemView.findViewById(R.id.videoName);
            this.folderName = itemView.findViewById(R.id.folderName);
            this.duration = itemView.findViewById(R.id.duration);
            this.videoImg = itemView.findViewById(R.id.videoImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!Utils.isImageFile(mData.get(getAdapterPosition()).getFilePath())){
                Intent intent = new Intent(activity, VideoPreviewActivity.class);
                intent.putExtra("videoPath", mData.get(getAdapterPosition()).getFilePath());
                activity.startActivity(intent);
            }else {
                activity.startActivity(new Intent(activity, FullImageActivity.class)
                        .putExtra("link",mData.get(getAdapterPosition()).getFilePath()));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mData.get(getAdapterPosition()).getFilePath()), "image/*");
             //   ContextCompat.startActivity(activity, intent, null);
            }
        }
    }

    void share(String path, Activity activity) {
        Utils.mShare(path, activity);
    }

    void delete(final int position, Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are You Sure to Delete this File?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                File file = new File(mData.get(position).getFilePath());
                if (file.exists()) {
                    file.delete();
                    mData.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
