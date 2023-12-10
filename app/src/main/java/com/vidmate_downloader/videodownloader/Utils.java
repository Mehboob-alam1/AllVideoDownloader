package com.vidmate_downloader.videodownloader;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utils {

    public static File downloadWhatsAppDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");
    public static File downloadWABusiDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");
    public static String instaDirPath = "MyDownloads/";
    public static File downloadInstaDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");

    public static String twitterDirPath = "MyDownloads/";
    public static File downloadTwitterDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");

    public static String vimeoDirPath = "MyDownloads/";
    public static File downloadVimeoDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");

    public static String tiktokDirPath = "MyDownloads/";
    public static File downloadTiktokDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");

    public static File downloadLikeeDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");

    public static String fbDirPath = "MyDownloads/";
    public static File downloadFBDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads");
    public static File MoviesDir = new File(Environment.getExternalStorageDirectory() + "/Movies");

    public static String snackDirPath = "MyDownloads/";
    public static File downloadSnackDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/SnackVideo");

    public static String sChatDirPath = "MyDownloads/ShareChat/";
    public static File downloadSChatkDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/ShareChat");

    public static String roposoDirPath = "MyDownloads/Roposo/";
    public static File downloadRoposoDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/Roposo");

    public static String chinagriDirPath = "MyDownloads/Chinagri/";
    public static File downloadChinagriDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/Chinagri");

    public static String mojDirPath = "MyDownloads/Moj/";
    public static File downloadMojDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/Moj");

    public static String mxTakaTakDirPath = "MyDownloads/MxTakaTak/";
    public static File downloadMxTakaTakDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/MxTakaTak");

    public static String mitronDirPath = "MyDownloads/Mitron/";
    public static File downloadMitronDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/Mitron");

    public static String ziliDirPath = "MyDownloads/Zili/";
    public static File downloadZiliDir = new File(Environment.getExternalStorageDirectory() + "/Download/MyDownloads/Zili");

    private static boolean doesPackageExist(String targetPackage, Context context) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void mPlayer(String filepath, Activity activity) {
        File file = new File(filepath);
        Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext()
                .getPackageName() + ".provider", file);
        Intent intent;
        if (!Utils.getBack(filepath, "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(videoURI, "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (doesPackageExist("com.google.android.gallery3d", activity)) {
                    intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.android.gallery3d", activity)) {
                    intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.cooliris.media", activity)) {
                    intent.setClassName("com.cooliris.media", "com.cooliris.media.MovieView");
                }
                activity.startActivity(intent);
            } catch (Exception e) {
                try {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(videoURI, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                } catch (Exception e2) {
                    Toast.makeText(activity, "Sorry, Play video not working properly , try again!", Toast.LENGTH_LONG).show();
                }
            }
        } else if (!Utils.getBack(filepath, "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(videoURI, "audio/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);
        } else if (!Utils.getBack(filepath, "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(videoURI, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(intent);
            } catch (Exception e3) {
                Toast.makeText(activity, "Sorry. We can't Display Images. try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String getBack(String paramString1, String paramString2) {
        Matcher localMatcher = Pattern.compile(paramString2).matcher(paramString1);
        if (localMatcher.find()) {
            return localMatcher.group(1);
        }
        return "";
    }


    public static void mShare(String filepath, Activity activity) {
        File fileToShare = new File(filepath);
        if (isImageFile(filepath)) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setType("image/*");
           // Uri photoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext().getPackageName() + ".provider", fileToShare);
            share.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse(filepath));
            activity.startActivity(Intent.createChooser(share, "Share via"));

        } else if (isVideoFile(filepath)) {


         //   Uri uri=FileProvider.getUriForFile(Objects.requireNonNull(activity.getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", fileToShare);

          //  Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext().getPackageName() + ".provider", fileToShare);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath));

            activity.startActivity(videoshare);
        }

    }

    public static void shareFile(Context context, boolean isVideo, String path) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        if (isVideo)
            share.setType("Video/*");
        else
            share.setType("image/*");

        Uri uri;
        if (path.startsWith("content")) {
            uri = Uri.parse(path);
        } else {
            uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", new File(path));
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(share);
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
    public static String VideoType(String path) {
        Uri file = Uri.fromFile(new File(path));
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        return fileExt.toUpperCase();
    }

    public static String getVideoDate(long val){
        val*=1000L;
        return new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date(val));
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void ShowToast(Context context, String str) {
        try {
            Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void mediaScanner(Context context, String filePath, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + filePath + fileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/" + filePath + fileName))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlertDialog alertDialog = null;

    public static void displayLoader(Context context) {
        if (alertDialog == null) {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View view = layoutInflaterAndroid.inflate(R.layout.dialog_loading, null);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(view);

            alertDialog = alert.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }

    }

    public static void dismissLoader() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public static void downloader(Context context, String downloadURL, String path, String fileName) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "" + context.getString(R.string.dl_started), Toast.LENGTH_SHORT).show();
            }
        });

        String desc = context.getString(R.string.downloading);
//        String timeStamp = String.valueOf(System.currentTimeMillis());
//        String file = "tiktok_" + "_" + timeStamp;
//        String ext = "mp4";
//        String name = file + "." + ext;
        Uri Download_Uri = Uri.parse(downloadURL);


        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setTitle(context.getString(R.string.app_name));
        request.setVisibleInDownloadsUi(true);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, path + fileName);
        dm.enqueue(request);

        Utils.mediaScanner(context, path, fileName);
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static boolean isVideoFile(Context context, String path) {
        if (path.startsWith("content")) {
            DocumentFile fromTreeUri = DocumentFile.fromSingleUri(context, Uri.parse(path));
            String mimeType = fromTreeUri.getType();
            return mimeType != null && mimeType.startsWith("video");
        } else {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("video");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean copyFileInSavedDir(Context context, String sourceFile, boolean isWApp) {

        String finalPath = getDir(isWApp).getAbsolutePath();

        String pathWithName = finalPath + File.separator + new File(sourceFile).getName();
        Uri destUri = Uri.fromFile(new File(pathWithName));

        InputStream is = null;
        OutputStream os = null;
        try {
            Uri uri= Uri.parse(sourceFile);
            is = context.getContentResolver().openInputStream(uri);
            os = context.getContentResolver().openOutputStream(destUri, "w");

            byte[] buffer = new byte[1024];

            int length;
            while ((length = is.read(buffer)) > 0)
                os.write(buffer, 0, length);

            is.close();
            os.flush();
            os.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(destUri);
            context.sendBroadcast(intent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    static File getDir(boolean isWApp) {

        File rootFile = downloadWhatsAppDir;
        if (!isWApp){
            rootFile = downloadWABusiDir;
        }
        rootFile.mkdirs();

        return rootFile;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean download(Context context, String sourceFile, boolean isWApp) {
        if (copyFileInSavedDir(context, sourceFile, isWApp)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getHost(String url){
        if(url == null || url.length() == 0)
            return "";

        int doubleslash = url.indexOf("//");
        if(doubleslash == -1)
            doubleslash = 0;
        else
            doubleslash += 2;

        int end = url.indexOf('/', doubleslash);
        end = end >= 0 ? end : url.length();

        int port = url.indexOf(':', doubleslash);
        end = (port > 0 && port < end) ? port : end;

        return url.substring(doubleslash, end);
    }
    public static String getBaseDomain(String url) {
        String host = getHost(url);
        int startIndex = 0;
        int nextIndex = host.indexOf('.');
        int lastIndex = host.lastIndexOf('.');
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf('.', startIndex);
        }
        if (startIndex > 0) {
            return host.substring(startIndex);
        } else {
            return host;
        }
    }
    public static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    public static NativeAd nativeAd = null;

    public static void loadNativeAdAdMob(ShimmerTextView frameLayout, String key, Integer layout, Activity activity
            , FrameLayout content_layout, ShimmerTextView shimmerTextView) {
        AdLoader.Builder builder = new AdLoader.Builder(activity, key);
        // OnLoadedListener implementation.
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                if (Utils.nativeAd != null) {
                    Utils.nativeAd.destroy();
                }
                Utils.nativeAd = nativeAd;
                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);
                populateNativeAdView(nativeAd, adView);
                content_layout.removeAllViews();
                content_layout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader=builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                frameLayout.setVisibility(View.GONE);
                shimmerTextView.setVisibility(View.GONE);
               // Toast.makeText(activity, ""+loadAdError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                frameLayout.setVisibility(View.GONE);
                shimmerTextView.setVisibility(View.GONE);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }
    public static void populateNativeAdView(NativeAd nativeAd,NativeAdView adView) {
        // Set the media view.
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        adView.getMediaView().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());

        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView)adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button)adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            adView.getIconView().setVisibility(View.VISIBLE);
            ((ImageView)adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView)adView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView)adView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStarRatingView().setVisibility(View.VISIBLE);
            ((RatingBar)adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
        }
        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView)adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    public static void BannerAd(final FrameLayout Ad_Layout, Activity activity) {


        AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(activity.getString(R.string.admob_banner_id));
        AdRequest adore = new AdRequest.Builder().build();
        mAdView.loadAd(adore);
        Ad_Layout.removeAllViews();
        Ad_Layout.addView(mAdView);


        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                Ad_Layout.setVisibility(View.VISIBLE);
                super.onAdLoaded();

                Log.e("ddddd", "dddd");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Ad_Layout.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView.destroy();
                Ad_Layout.setVisibility(View.INVISIBLE);
                Log.e("ddddd2", "dddd" + loadAdError.getMessage());
                Toast.makeText(activity, ""+loadAdError.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static void setDesktopMode(WebView webView, boolean enabled) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        webView.reload();
    }
}
