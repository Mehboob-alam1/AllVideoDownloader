package com.vidmate_downloader.videodownloader.activities;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.vidmate_downloader.videodownloader.R;

public class MainActivity extends AppCompatActivity {

    EditText videourl;
    Button searchurl, downloadvideo;
    VideoView vv;
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        videourl = findViewById(R.id.url);
        searchurl = findViewById(R.id.searchurl);
        webView = findViewById(R.id.web);
        downloadvideo = findViewById(R.id.download);
        webView.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(this, "mJava");
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //webView.setWebViewClient(new myWebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:"+
                                "var e=0;\n" +
                                "window.onscroll=function()\n" +
                                "{\n" +
                                "\tvar ij=document.querySelectorAll(\"video\");\n" +
                                "\t\tfor(var f=0;f<ij.length;f++)\n" +
                                "\t\t{\n" +
                                "\t\t\tif((ij[f].parentNode.querySelectorAll(\"img\")).length==0)\n" +
                                "\t\t\t{\n" +
                                "\t\t\t\tvar nextimageWidth=ij[f].nextSibling.style.width;\n" +
                                "\t\t\t\tvar nextImageHeight=ij[f].nextSibling.style.height;\n" +
                                "\t\t\t\tvar Nxtimgwd=parseInt(nextimageWidth, 10);\n" +
                                "\t\t\t\tvar Nxtimghght=parseInt(nextImageHeight, 10); \n" +
                                "\t\t\t\tvar DOM_img = document.createElement(\"img\");\n" +
                                "\t\t\t\t\tDOM_img.height=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.width=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.style.top=(Nxtimghght/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.left=(Nxtimgwd/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.position=\"absolute\";\n" +
                                "\t\t\t\t\tDOM_img.src = \"https://image.ibb.co/kobwsk/one.png\"; \n" +
                                "\t\t\t\t\tij[f].parentNode.appendChild(DOM_img);\n" +
                                "\t\t\t}\t\t\n" +
                                "\t\t\tij[f].remove();\n" +
                                "\t\t} \n" +
                                "\t\t\te++;\n" +
                                "};"+
                                "var a = document.querySelectorAll(\"a[href *= 'video_redirect']\");\n" +
                                "for (var i = 0; i < a.length; i++) {\n" +
                                "    var mainUrl = a[i].getAttribute(\"href\");\n" +
                                "  a[i].removeAttribute(\"href\");\n"+
                                "\tmainUrl=mainUrl.split(\"/video_redirect/?src=\")[1];\n" +
                                "\tmainUrl=mainUrl.split(\"&source\")[0];\n" +
                                "    var threeparent = a[i].parentNode.parentNode.parentNode;\n" +
                                "    threeparent.setAttribute(\"src\", mainUrl);\n" +
                                "    threeparent.onclick = function() {\n" +
                                "        var mainUrl1 = this.getAttribute(\"src\");\n" +
                                "         mJava.getData(mainUrl1);\n" +
                                "    };\n" +
                                "}"+
                                "var k = document.querySelectorAll(\"div[data-store]\");\n" +
                                "for (var j = 0; j < k.length; j++) {\n" +
                                "    var h = k[j].getAttribute(\"data-store\");\n" +
                                "    var g = JSON.parse(h);\nvar jp=k[j].getAttribute(\"data-sigil\");\n"+
                                "    if (g.type === \"video\") {\n" +
                                "if(jp==\"inlineVideo\")" +
                                "{" +
                                "   k[j].removeAttribute(\"data-sigil\");" +
                                "}\n" +
                                "        var url = g.src;\n" +
                                "        k[j].setAttribute(\"src\", g.src);\n" +
                                "        k[j].onclick = function() {\n" +
                                "            var mainUrl = this.getAttribute(\"src\");\n" +
                                "               mJava.getData(mainUrl);\n" +
                                "        };\n" +
                                "    }\n" +
                                "\n" +
                                "}");
                    }
                }, 3000);
            }

            public void onLoadResource(WebView view, String url) {
                final Handler handler = new Handler();
                if (url.endsWith(".mp4")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Download")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    downloadvideo(url);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();

                }
                if (url.endsWith(".png")||url.endsWith(".jpg")||url.endsWith(".jpeg")){
                    View view1=getLayoutInflater().inflate(R.layout.download_btn_view,null);
                    view1.findViewById(R.id.downloadBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadimage(url);
                            Toast.makeText(MainActivity.this, ""+url, Toast.LENGTH_SHORT).show();
                        }
                    });
                    view.addView(view1);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:"+
                                "var e=document.querySelectorAll(\"span\"); " +
                                "if(e[0]!=undefined)" +
                                "{"+
                                "var fbforandroid=e[0].innerText;" +
                                "if(fbforandroid.indexOf(\"Facebook\")!=-1)" +
                                "{ " +
                                "var h =e[0].parentNode.parentNode.parentNode.style.display=\"none\";" +
                                "} " +
                                "}" +
                                "var installfb=document.querySelectorAll(\"a\");\n" +
                                "for (var hardwares = 0; hardwares < installfb.length; hardwares++) \n" +
                                "{\n" +
                                "\tif(installfb[hardwares].text.indexOf(\"Install\")!=-1)\n" +
                                "\t{\n" +
                                "\t\tvar soft=installfb[hardwares].parentNode.style.display=\"none\";\n" +
                                "\n" +
                                "\t}\n" +
                                "}\n");
                        webView.loadUrl("javascript:"+
                                "var e=0;\n" +
                                "window.onscroll=function()\n" +
                                "{\n" +
                                "\tvar ij=document.querySelectorAll(\"video\");\n" +
                                "\t\tfor(var f=0;f<ij.length;f++)\n" +
                                "\t\t{\n" +
                                "\t\t\tif((ij[f].parentNode.querySelectorAll(\"img\")).length==0)\n" +
                                "\t\t\t{\n" +
                                "\t\t\t\tvar nextimageWidth=ij[f].nextSibling.style.width;\n" +
                                "\t\t\t\tvar nextImageHeight=ij[f].nextSibling.style.height;\n" +
                                "\t\t\t\tvar Nxtimgwd=parseInt(nextimageWidth, 10);\n" +
                                "\t\t\t\tvar Nxtimghght=parseInt(nextImageHeight, 10); \n" +
                                "\t\t\t\tvar DOM_img = document.createElement(\"img\");\n" +
                                "\t\t\t\t\tDOM_img.height=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.width=\"68\";\n" +
                                "\t\t\t\t\tDOM_img.style.top=(Nxtimghght/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.left=(Nxtimgwd/2-20)+\"px\";\n" +
                                "\t\t\t\t\tDOM_img.style.position=\"absolute\";\n" +
                                "\t\t\t\t\tDOM_img.src = \"https://image.ibb.co/kobwsk/one.png\"; \n" +
                                "\t\t\t\t\tij[f].parentNode.appendChild(DOM_img);\n" +
                                "\t\t\t}\t\t\n" +
                                "\t\t\tij[f].remove();\n" +
                                "\t\t} \n" +
                                "\t\t\te++;\n" +
                                "};"+
                                "var a = document.querySelectorAll(\"a[href *= 'video_redirect']\");\n" +
                                "for (var i = 0; i < a.length; i++) {\n" +
                                "    var mainUrl = a[i].getAttribute(\"href\");\n" +
                                "  a[i].removeAttribute(\"href\");\n"+
                                "\tmainUrl=mainUrl.split(\"/video_redirect/?src=\")[1];\n" +
                                "\tmainUrl=mainUrl.split(\"&source\")[0];\n"+
                                "    var threeparent = a[i].parentNode.parentNode.parentNode;\n" +
                                "    threeparent.setAttribute(\"src\", mainUrl);\n" +
                                "    threeparent.onclick = function() {\n" +
                                "        var mainUrl1 = this.getAttribute(\"src\");\n" +
                                "         mJava.getData(mainUrl1);\n" +
                                "    };\n" +
                                "}"+
                                "var k = document.querySelectorAll(\"div[data-store]\");\n" +
                                "for (var j = 0; j < k.length; j++) {\n" +
                                "    var h = k[j].getAttribute(\"data-store\");\n" +
                                "    var g = JSON.parse(h);var jp=k[j].getAttribute(\"data-sigil\");\n"+
                                "    if (g.type === \"video\") {\n" +
                                "if(jp==\"inlineVideo\")" +
                                "{" +
                                "   k[j].removeAttribute(\"data-sigil\");" +
                                "}\n" +
                                "        var url = g.src;\n" +
                                "        k[j].setAttribute(\"src\", g.src);\n" +
                                "        k[j].onclick = function() {\n" +
                                "            var mainUrl = this.getAttribute(\"src\");\n" +
                                "               mJava.getData(mainUrl);\n" +
                                "        };\n" +
                                "    }\n" +
                                "\n" +
                                "}");
                    }
                }, 3000);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("URL",request.getUrl().toString());
                if (request.getUrl().toString().endsWith(".mp4")){
                    downloadvideo(request.getUrl().toString());
                    return true;
                }
            //    return super.shouldOverrideUrlLoading(view, request);
                    return false;
            }
        });
        webView.loadUrl("https://www.facebook.com");
        searchurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = videourl.getText().toString();
/*                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);*/
                webView.loadUrl(url);
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });

    downloadvideo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Toast.makeText(MainActivity.this, "Downlaod Video", Toast.LENGTH_SHORT).show();
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                    String[] permissions = {WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, 1);
                }else {
                    Toast.makeText(MainActivity.this, "Enable Video Downlaod", Toast.LENGTH_SHORT).show();
                }
            }
//            webView.setDownloadListener(new DownloadListener() {
//                @Override
//                public void onDownloadStart(String url, String userAgent, String contentDiposition, String mimeType, long contentLenght) {
//
//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                    request.setMimeType(mimeType);
//                    String cookies = CookieManager.getInstance().getCookie(url);
//                    request.addRequestHeader("cookies", cookies);
//                    request.addRequestHeader("user-agent", userAgent);
//                    request.setDescription("Downloading File...");
//                    request.setTitle(URLUtil.guessFileName(url, contentDiposition, mimeType));
//                    request.allowScanningByMediaScanner();
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDiposition, mimeType));
//                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                    dm.enqueue(request);
//                    Toast.makeText(getApplicationContext(), "Downloading File....", Toast.LENGTH_SHORT).show();
//                }
//            });


            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie",cookies);
                    request.addRequestHeader("User-Agent",userAgent);
                    request.setDescription("Downloading file....");
                    request.setTitle(URLUtil.guessFileName(url,contentDisposition,mimeType));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(),"Downloading File",Toast.LENGTH_SHORT).show();


                }
            });
        }
    });
}
    @JavascriptInterface
    public void getData(final String pathvideo) {
        Log.d("scroled","jo");
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        alertDialog.setTitle("Save Video?");
        alertDialog.setMessage("Do you Really want to Save Video ?");
        alertDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String finalurl ;
                finalurl=pathvideo;
                finalurl=finalurl.replaceAll("%3A",":");
                finalurl=finalurl.replaceAll("%2F","/");
                finalurl=finalurl.replaceAll("%3F","?");
                finalurl=finalurl.replaceAll("%3D","=");
                finalurl=finalurl.replaceAll("%26","&");
                downloadvideo(finalurl);
            }


        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Play Video", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Setting Netural "Cancel" Button
        alertDialog.setNeutralButton("Copy Link", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed Cancel button. Write Logic Here
                String finalurl ;
                finalurl=pathvideo;
                finalurl=finalurl.replaceAll("%3A",":");
                finalurl=finalurl.replaceAll("%2F","/");
                finalurl=finalurl.replaceAll("%3F","?");
                finalurl=finalurl.replaceAll("%3D","=");
                finalurl=finalurl.replaceAll("%26","&");
                ClipboardManager clipboard = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("mainurlcopy",finalurl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Url Copied",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    public void downloadvideo(String pathvideo)
    {
        if(pathvideo.contains(".mp4"))
        {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + "Video-"  + ".mp4");
            DownloadManager dm = (DownloadManager)getApplicationContext().getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
            dm.enqueue(request);

        }
    }
    public void downloadimage(String pathvideo)
    {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + "Video-"  + ".jpg");
        DownloadManager dm = (DownloadManager)getApplicationContext().getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
          //  Log.e("eeeeeee",url);
            return true;
        }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Log.e("eeeeeee",view.getUrl());
    }
}



}