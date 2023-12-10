package com.vidmate_downloader.videodownloader;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.vidmate_downloader.videodownloader.Model.Folder;
import com.vidmate_downloader.videodownloader.Model.Video;
import com.vidmate_downloader.videodownloader.activities.HistoryActivity;
import com.vidmate_downloader.videodownloader.activities.WebFragment;
import com.vidmate_downloader.videodownloader.fragments.DownloadFragment;
import com.vidmate_downloader.videodownloader.fragments.HomeFragment;
import com.vidmate_downloader.videodownloader.fragments.TabsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class home extends AppCompatActivity implements HomeFragment.OnDataPass {

    ImageView homeImg, tabImg, downloadImg, personImg;
    TextView homeTxt, tabTxt, downloadTxt, personTxt;
    LinearLayout home, tabs, downloads, personTo;
    CardView downloadsTo;
    Activity activity;
    Fragment selectedFragment = null;
    ChipNavigationBar bottomNav;
    public static ArrayList<Video> videoList=new ArrayList<>();
    public static ArrayList<Folder> folderList=new ArrayList<>();

    public static ArrayList<Video> audioList=new ArrayList<>();
    public static ArrayList<Folder> AudiofolderList=new ArrayList<>();

    public static ArrayList<Video> imageList=new ArrayList<>();
    public static ArrayList<Folder> ImagefolderList=new ArrayList<>();
    public static ArrayList<Video> searchList=new ArrayList<>();
    public static List<String> sortList = Arrays.asList(
            MediaStore.Video.Media.DATE_ADDED + " DESC",
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.TITLE + " DESC",
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.SIZE + " DESC"
    );
    public static Integer sortValue= 0;
    DrawerLayout draw;
    WebFragment webFragment;
    @Override
    public void onDataPass(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("url",data);
        webFragment=new WebFragment();
        webFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerV,webFragment)
                .addToBackStack("WebFragment").commit();
    }

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activity = this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        draw = findViewById(R.id.draw);
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(selectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerV,
                new HomeFragment()).commit();
        if (!checkPermissions(this, permissionsList)) {
            ActivityCompat.requestPermissions(this, permissionsList, 21);
        }
/*        if (requestRuntimePermission()) {
            new DownloadFilesTask().execute("");
        } else {
            folderList = new ArrayList();
            videoList = new ArrayList();
        }*/
        findViewById(R.id.ic_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.openDrawer((int) GravityCompat.START);
            }
        });
        findViewById(R.id.llHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(R.id.containerV,
                        new HomeFragment()).commit();
            }
        });
        findViewById(R.id.llHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
                startActivity(new Intent(activity, HistoryActivity.class)
                        .putExtra("type","h"));
            }
        });
        findViewById(R.id.llfav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
                startActivity(new Intent(activity, HistoryActivity.class)
                        .putExtra("type","b"));
            }
        });
        findViewById(R.id.llShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
                    String sAux = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, getString(R.string.app_name)));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        findViewById(R.id.llRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
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
        findViewById(R.id.llMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
                Intent inMoreapp = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=pak+developer+master"));
                startActivity(inMoreapp);
            }
        });
        findViewById(R.id.llPolicy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.closeDrawer((int) GravityCompat.START);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private final ChipNavigationBar.OnItemSelectedListener selectedListener =
            item -> {
                switch (item) {
                    case R.id.home:
                        if (webFragment!=null){
                            if (webFragment.HomeClick()){

                            }
                        }
                        ClearBack();
                        getSupportActionBar().show();
                        selectFragment(getSupportFragmentManager(), R.id.home);
                        break;
                    case R.id.tab:

                        ClearBack();
                        getSupportActionBar().show();
                        selectFragment(getSupportFragmentManager(), R.id.tab);
                        break;
                    case R.id.back:
                        if (webFragment!=null){
                            if (!webFragment.BackCheck()){
                                ClearBack();
                            }
                        }else {
                            ClearBack();
                        }
                       // ClearBack();
                        break;
                    case R.id.forward:
                        if (webFragment!=null){
                            if (!webFragment.ForwardCheck()){
                                ClearBack();
                            }
                        }else {
                            ClearBack();
                        }
                       // ClearBack();
                        break;
                    case R.id.downloads:
                        ClearBack();
                        getSupportActionBar().show();
                        selectFragment(getSupportFragmentManager(), R.id.downloads);
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerV,
                            selectedFragment).commit();
                }
//                return true;
            };
    private void ClearBack() {
        webFragment=null;
        findViewById(R.id.ivBookMark).setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public static void selectFragment(FragmentManager fragmentManager, int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.home:
                fragment = new HomeFragment();
                break;
            case R.id.tab:
                fragment = new TabsFragment();
                break;
            case R.id.downloads:
                fragment = new DownloadFragment();
                break;
        }

        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.containerV, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomNav.getSelectedItemId()==R.id.home){
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerV, new HomeFragment())
                        .commit();
                findViewById(R.id.ivBookMark).setVisibility(View.GONE);
                bottomNav.setItemSelected(R.id.home,true);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
    String[] permissionsList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 21) {
            if (!checkPermissions(this, permissionsList)) {
                ActivityCompat.requestPermissions(this, permissionsList, 21);
            }
        }
        if(requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show();
              //  new DownloadFilesTask().execute("");
            }
            else{ Snackbar.make(findViewById(R.id.root_home), "Storage Permission Needed!!", 5000).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE},13);
                }
            }).show();}
//                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
        }

        //for read external storage permission
        if(requestCode == 14) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show();
               // new DownloadFilesTask().execute("");
            }
            else {Snackbar.make(findViewById(R.id.root_home), "Storage Permission Needed!!", 5000).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE},14);
                }
            }).show();}
        }
    }
    private Boolean requestRuntimePermission(){
        //android 13 permission request
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_VIDEO}, 13);
                return false;
            }
            return true;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_AUDIO}, 13);
                return false;
            }
            return true;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 13);
                return false;
            }
            return true;
        }

        //requesting storage permission for only devices less than api 28
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if(ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},13);
                return false;
            }
        }else{
            //read external storage permission for devices higher than android 10 i.e. api 29
            if(ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE},14);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dot_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.llShare){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
                String sAux = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, getString(R.string.app_name)));
            } catch (Exception e) {
                //e.toString();
            }
        }
        if (item.getItemId()==R.id.llHistory){
            startActivity(new Intent(activity, HistoryActivity.class)
                    .putExtra("type","h"));
        }
        if (item.getItemId()==R.id.llfav){
            startActivity(new Intent(activity, HistoryActivity.class)
                    .putExtra("type","b"));
        }
        if (item.getItemId()==R.id.llRate){
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
        if (item.getItemId()==R.id.llMore){
            Intent inMoreapp = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=pak+developer+master"));
            startActivity(inMoreapp);
        }
        if (item.getItemId()==R.id.llPolicy){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")));
            }catch (Exception e){
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}