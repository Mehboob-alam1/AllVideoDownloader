package com.vidmate_downloader.videodownloader.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.tiktok.HistoryBookMarkSQLite;
import com.vidmate_downloader.videodownloader.tiktok.TouchableWebView;
import com.vidmate_downloader.videodownloader.tiktok.VideoContentSearch;
import com.vidmate_downloader.videodownloader.tiktok.VisitedPage;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    private String url;
    private TouchableWebView page;
    private SSLSocketFactory defaultSSLSF;

    private FrameLayout videoFoundTV;
   // private CustomVideoView videoFoundView;
    private FloatingActionButton videosFoundHUD;

    private View foundVideosWindow;
    private VideoList videoList;
    private ImageView foundVideosClose;

    private ProgressBar loadingPageProgress;

    private int orientation;
    private boolean loadedFirsTime;

    private List<String> blockedWebsites;
    private BottomSheetDialog dialog;

    private Activity activity;
    ImageView ivBookMark;

    String isType=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        activity=this;
        HistoryBookMarkSQLite db = new HistoryBookMarkSQLite(activity);
      //  db.clearBookmark();
     //   db.close();
        isType=getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        defaultSSLSF = HttpsURLConnection.getDefaultSSLSocketFactory();
        blockedWebsites = Arrays.asList(getResources().getStringArray(R.array.blocked_sites));
        ivBookMark = findViewById(R.id.ivBookMark);
        if (page == null) {
            page = findViewById(R.id.page);
        } else {
            View page1 = findViewById(R.id.page);
            ((ViewGroup) page.getParent()).removeView(page);
        }
        loadingPageProgress = findViewById(R.id.loadingPageProgress);
        loadingPageProgress.setVisibility(View.GONE);

        createVideosFoundHUD();
        createVideosFoundTV();
        createFoundVideosWindow();
        updateFoundVideosBar();
        page.getSettings().setJavaScriptEnabled(true);
        page.getSettings().setDomStorageEnabled(true);
        page.getSettings().setAllowUniversalAccessFromFileURLs(true);
        page.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        page.setWebViewClient(new WebViewClient() {//it seems not setting webclient, launches
            //default browser instead of opening the page in webview
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(blockedWebsites.contains(Utils.getBaseDomain(request.getUrl().toString()))){
                    Log.d("vdd", "URL : " + request.getUrl().toString());
                    new AlertDialog.Builder(activity)
                            .setMessage("Youtube is not supported according to google policy.")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(final WebView webview, final String url, Bitmap favicon) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        EditText urlBox = findViewById(R.id.et_search_bar);
                        urlBox.setText(url);
                        urlBox.setSelection(urlBox.getText().length());
                        Set_BookIcon();
                    }
                });
                findViewById(R.id.loadingProgress).setVisibility(View.GONE);
                loadingPageProgress.setVisibility(View.VISIBLE);
                super.onPageStarted(webview, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingPageProgress.setVisibility(View.GONE);
                Set_BookIcon();
            }

            @Override
            public void onLoadResource(final WebView view, final String url) {
                Log.d("fb :", "URL: " + url);
                final String viewUrl = view.getUrl();
                final String title = view.getTitle();
                /*if (url.endsWith(".jpg")||url.endsWith(".jpeg")||url.endsWith(".png")){
                    Toast.makeText(WebActivity.this, ""+url, Toast.LENGTH_SHORT).show();
                    videoList.addItem("0", "jpeg", url, "Image_"+System.currentTimeMillis(), view.getUrl(), false, "https://unsplash.com/wallpapers", false);
                    updateFoundVideosBar();
                }*/
                new VideoContentSearch(activity, url, viewUrl, title) {
                    @Override
                    public void onStartInspectingURL() {
                        Utils.disableSSLCertificateChecking();
                    }

                    @Override
                    public void onFinishedInspectingURL(boolean finishedAll) {
                        HttpsURLConnection.setDefaultSSLSocketFactory(defaultSSLSF);
                    }

                    @Override
                    public void onVideoFound(String size, String type, String link, String name, String page, boolean chunked, String website, boolean audio) {
                        videoList.addItem(size, type, link, name, page, chunked, website, audio);
                        updateFoundVideosBar();
                    }
                }.start();
            }

/*            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean shouldOverride = false;
                Toast.makeText(WebActivity.this, ""+url, Toast.LENGTH_SHORT).show();
                if (url.contains(".jpg")||url.contains(".jpeg")||url.contains(".png")){
                    videoList.addItem("0", "jpeg", url, "Image_"+System.currentTimeMillis(), view.getUrl(), false, "https://unsplash.com/wallpapers", false);
                    updateFoundVideosBar();
                    shouldOverride = true;
                }
                return shouldOverride;
            }*/
            /*            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (activity != null) {
                    Log.d("VDDebug", "Url: " + url);
                    return new WebResourceResponse(null, null, null);
                }
                return super.shouldInterceptRequest(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }*/


        });
        page.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                loadingPageProgress.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                videoList.deleteAllItems();
                updateFoundVideosBar();
                if (isType==null){
                    VisitedPage vp = new VisitedPage();
                    vp.title = title;
                    vp.link = view.getUrl();
                    HistoryBookMarkSQLite db = new HistoryBookMarkSQLite(activity);
                    db.addPageToHistory(vp);
                    db.close();
                }
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }


        });

        page.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            //Checking runtime permission for devices above Marshmallow.
            final String filename = URLUtil.guessFileName(url, contentDisposition, mimeType);
          //  Toast.makeText(this, ""+filename, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Utils.downloader(activity,url,Utils.fbDirPath,filename);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }
            } else {
                Utils.downloader(activity,url,Utils.fbDirPath,filename);

            }
        });
        page.loadUrl(url);
        loadedFirsTime = true;

        findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryBookMarkSQLite db = new HistoryBookMarkSQLite(activity);
                VisitedPage vp = new VisitedPage();
                EditText urlBox = findViewById(R.id.et_search_bar);
                vp.title = page.getTitle();
                vp.link = urlBox.getText().toString().trim();
              //  vp.arr= DbBitmapUtility.getBytes(screenshot2(page));
                if (isType!=null){
                    long l=db.addPageToTab2(vp);
                }else {
                    long l=db.addPageToTab(vp);
                }
                db.close();
                finish();
            }
        });
        ivBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryBookMarkSQLite db = new HistoryBookMarkSQLite(activity);
                VisitedPage vp = new VisitedPage();
                EditText urlBox = findViewById(R.id.et_search_bar);
                vp.title = page.getTitle();
                vp.link = urlBox.getText().toString().trim();
                List<VisitedPage> list=db.getBookmarkPagesByKeyword(vp.link);
                for (int i=0;i<list.size();i++){
                    if (list.get(i).link.toLowerCase().trim().equals(vp.link.toLowerCase().trim())){
                        db.deleteFromBookMark(list.get(i).link);
                        ivBookMark.setImageResource(R.drawable.baseline_bookmark_border_24);
                        Toast.makeText(activity, "Removed From Favorites", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                long l=db.addPageToBookMark(vp);
                if (l==-1){
                    ivBookMark.setImageResource(R.drawable.baseline_bookmark_border_24);
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }else {
                    ivBookMark.setImageResource(R.drawable.baseline_bookmark_24);
                    Toast.makeText(activity, "Saved To Favorites", Toast.LENGTH_SHORT).show();

                };
                db.close();
            }
        });
    }
    public static Bitmap screenshot2(TouchableWebView webView) {
        webView.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int iHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, iHeight, paint);
        webView.draw(canvas);
        return bitmap;
    }
    public static Bitmap screenshot(TouchableWebView webView, float scale11) {
        try {
            float scale = webView.getScale();
            int height = (int) (webView.getContentHeight() * scale + 0.5);
            Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webView.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void Set_BookIcon() {
        EditText urlBox = findViewById(R.id.et_search_bar);
        String url=urlBox.getText().toString().trim();
        HistoryBookMarkSQLite db = new HistoryBookMarkSQLite(activity);
        VisitedPage vp = new VisitedPage();
        vp.title = page.getTitle();
        vp.link = url;
        if (!url.equals("")){
            List<VisitedPage> list=db.getBookmarkPagesByKeyword(vp.link);
            boolean is=false;
            for (int i=0;i<list.size();i++){
                if (list.get(i).link.equals(vp.link)){
                    ivBookMark.setImageResource(R.drawable.baseline_bookmark_24);
                    is=true;
                }
            }
            if (is==false){
                ivBookMark.setImageResource(R.drawable.baseline_bookmark_border_24);
            }
            db.close();
        }
    }

    private void updateFoundVideosBar() {
        if (videoList.getSize() > 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videosFoundHUD.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    Animation expandIn = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.expand_in);
                    videosFoundHUD.startAnimation(expandIn);
                }
            });

        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videosFoundHUD.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                    if (foundVideosWindow.getVisibility() == View.VISIBLE)
                        foundVideosWindow.setVisibility(View.GONE);
                }
            });
        }
    }
    private void createVideosFoundTV() {
/*        videoFoundTV = findViewById(R.id.videoFoundTV);
        videoFoundView = findViewById(R.id.videoFoundView);
        CustomMediaController mediaFoundController = view.findViewById(R.id.mediaFoundController);
        mediaFoundController.setFullscreenEnabled();
        videoFoundView.setMediaController(mediaFoundController);
        videoFoundTV.setVisibility(View.GONE);*/
    }

    private void createVideosFoundHUD() {
        videosFoundHUD = findViewById(R.id.videosFoundHUD);
        videosFoundHUD.setOnClickListener(this);
        if (url.contains("unsplash.com")){
            videosFoundHUD.setVisibility(View.GONE);
        }
    }

    private void createFoundVideosWindow() {

        dialog = new BottomSheetDialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.video_qualities_dialog);

        RecyclerView qualities = dialog.findViewById(R.id.qualities_rv);
        ImageView dismiss = dialog.findViewById(R.id.dismiss);

        assert dismiss != null;
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        foundVideosWindow = findViewById(R.id.foundVideosWindow);
        if (videoList != null) {
            videoList.recreateVideoList(qualities);
        }
        else {
            videoList = new VideoList(activity, qualities) {
                @Override
                public void onItemDeleted() {
                    dialog.dismiss();

                    updateFoundVideosBar();
                }

                @Override
                void onVideoPlayed(String url) {
                    dialog.dismiss();
                  //  updateVideoPlayer(url);
                }
            };
        }
    }
    @Override
    public void onClick(View v) {
        if (v == videosFoundHUD) {
            if (videoList.getSize() > 0) {
                dialog.show();
            } else {
                showGuide();
            }
        } else if (v == foundVideosClose) {
            dialog.dismiss();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback;

    public void setOnRequestPermissionsResultListener(ActivityCompat
                                                              .OnRequestPermissionsResultCallback
                                                              onRequestPermissionsResultCallback) {
        this.onRequestPermissionsResultCallback = onRequestPermissionsResultCallback;
    }
    public String getUrl() {
        return url;
    }

    @Override
    public void onPause() {
        super.onPause();
        page.onPause();
        Log.d("debug", "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        page.onResume();
        Log.d("debug", "onResume: ");
    }

    @Override
    public void onBackPressed() {
        if (foundVideosWindow.getVisibility() == View.VISIBLE /*&& !videoFoundView.isPlaying()*/ && videoFoundTV.getVisibility() == View.GONE) {
            foundVideosWindow.setVisibility(View.GONE);
        }/* else if (videoFoundView.isPlaying() || videoFoundTV.getVisibility() == View.VISIBLE) {
            videoFoundView.closePlayer();
            videoFoundTV.setVisibility(View.GONE);
        }*/ else if (page.canGoBack()) {
            page.goBack();
            Set_BookIcon();
        } else {
            super.onBackPressed();
        }

    }
    @Override
    public void onDestroy() {
        page.stopLoading();
        page.destroy();
        super.onDestroy();
    }
    private void showGuide() {
        final Dialog guide = new Dialog(activity);
        guide.setContentView(R.layout.dialog_guide);

        Button button = guide.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guide.dismiss();
            }
        });

        guide.show();
    }
}
