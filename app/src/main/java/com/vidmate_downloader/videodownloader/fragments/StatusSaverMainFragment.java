

package com.vidmate_downloader.videodownloader.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.STORAGE_SERVICE;

import static com.vidmate_downloader.videodownloader.Utils.appInstalledOrNot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vidmate_downloader.videodownloader.Model.WAStoryModel;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.adapter.WhatsappStoryAdapter;
import com.vidmate_downloader.videodownloader.tiktok.SharedPrefsMainApp;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;



public class StatusSaverMainFragment extends Fragment {

    //TODO fix it for whatsapp business only too like it works for whatsapp,
    // so users with only whatsappbusiness can use it too

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private static final int OPEN_DOCOMENT_TREE_REQUEST_CODE = 1012;
    private static final int OPEN_DOCOMENT_TREE_REQUEST_CODE_BUSINESS = 1013;


    ArrayList<Object> filesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout recyclerLayout;
    private AppCompatButton grantpermissionand11;

    private String namedataprefs;
    private String namedataprefs_business;
    private FragmentActivity myselectedActivity = null;
    private boolean isWhatsAppBusinessAvaliable = false;
    static DocumentFile[] documentFilesWhatsapp;
    static DocumentFile[] documentFilesWhatsappBusiness;

    private LinearLayout noresultfound;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remember add this line
        setRetainInstance(true);
    }
    public static boolean isMyPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_saver_main, container, false);
        setActivityAfterAttached();
        noresultfound = view.findViewById(R.id.grantlayout);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerLayout = view.findViewById(R.id.swipeRecyclerViewlayout);
        grantpermissionand11 = view.findViewById(R.id.grantpermissionand11);

        isWhatsAppBusinessAvaliable = isMyPackageInstalled("com.whatsapp.w4b", myselectedActivity.getPackageManager());

        namedataprefs = new SharedPrefsMainApp(requireActivity()).getPREFERENCE_whatsapp();
        namedataprefs_business = new SharedPrefsMainApp(requireActivity()).getPREFERENCE_whatsappbusiness();

        if (isAdded()) {
            grantpermissionand11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (namedataprefs.equals("")) {
                        grantAndroid11StorageAccessPermission();
                    }else {
                        Open();
                    }

                }
            });
            boolean result = checkPermission();
            if (result) {
                if (namedataprefs.equals("")) {
                    //                if (namedataprefs_business.equals("")){
                    //                    grantpermissionand11business.setVisibility(View.VISIBLE);
                    //                }
                } else {
                    if (getDataFromWhatsAppFolder() != null) {
                        runMyTask();
                    } else {

                    }

                }
            }

            recyclerLayout.setOnRefreshListener(() -> {
                try {


                    if (result) {
                        documentFilesWhatsapp = null;
                        documentFilesWhatsappBusiness = null;
                        if (!namedataprefs.equals("") && getDataFromWhatsAppFolder() != null) {

                            recyclerLayout.setRefreshing(true);
                            runMyTask();


                            (new Handler()).postDelayed(() -> {
                                try {
                                    recyclerLayout.setRefreshing(false);
                                    Utils.ShowToast(requireActivity(), requireActivity().getString(R.string.refre));


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, 2000);


                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(myselectedActivity, "Error in swipe refresh " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        return view;
    }


    @SuppressLint("WrongConstant")
    public void grantAndroid11StorageAccessPermission() {
        System.out.println("mydhfhsdkfsd 00");

        if (isMyPackageInstalled("com.whatsapp", myselectedActivity.getPackageManager())) {


            Intent intent;
            String whatsappfolderdir;
            StorageManager storageManager = (StorageManager) myselectedActivity.getSystemService(STORAGE_SERVICE);

            if (new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses").isDirectory()) {
                whatsappfolderdir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
            } else {
                whatsappfolderdir = "WhatsApp%2FMedia%2F.Statuses";
            }

            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String scheme = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI"))
                        .toString()
                        .replace("/root/", "/document/");
                String stringBuilder = scheme + "%3A" + whatsappfolderdir;
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(stringBuilder));

                System.out.println("mydhfhsdkfsd " + stringBuilder);
            }
            else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(whatsappfolderdir));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            startActivityForResult(intent, OPEN_DOCOMENT_TREE_REQUEST_CODE);
            return;
        } else {
            Toast.makeText(myselectedActivity, "App Not Install", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("WrongConstant")
    public void grantAndroid11StorageAccessPermissionBusiness() {
        System.out.println("mydhfhsdkfsd 00");

        if (isMyPackageInstalled("com.whatsapp.w4b", myselectedActivity.getPackageManager())) {


            Intent intent;
            String whatsappfolderdir;
            StorageManager storageManager = (StorageManager) myselectedActivity.getSystemService(STORAGE_SERVICE);

            if (new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses").isDirectory()) {

                whatsappfolderdir = "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses";
            } else if (new File(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/.Statuses").isDirectory()) {
                whatsappfolderdir = "WhatsApp%20Business%2FMedia%2F.Statuses";
            } else {
                return;
            }

            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String scheme = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI"))
                        .toString()
                        .replace("/root/", "/document/");
                String stringBuilder = scheme + "%3A" + whatsappfolderdir;
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(stringBuilder));

                System.out.println("mydhfhsdkfsd " + stringBuilder);
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(whatsappfolderdir));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            startActivityForResult(intent, OPEN_DOCOMENT_TREE_REQUEST_CODE_BUSINESS);
            return;
        } else {
            Toast.makeText(myselectedActivity, "App Not Install", Toast.LENGTH_SHORT).show();
        }
    }


    private DocumentFile[] getDataFromWhatsAppFolder() {
        try {
            if (documentFilesWhatsapp != null) {
                Log.e("StatusSaverMainFragment", "data not empty");

                return documentFilesWhatsapp;
            } else {
                Log.e("StatusSaverMainFragment", "empty");

                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(myselectedActivity.getApplicationContext(), Uri.parse(namedataprefs));
                if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
                    documentFilesWhatsapp = fromTreeUri.listFiles();
                    return documentFilesWhatsapp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private DocumentFile[] getDataFromWhatsAppBusinessFolder() {
        try {
            if (documentFilesWhatsappBusiness != null) {
                Log.e("StatusSaverMainFragment", "business data not empty");
                return documentFilesWhatsappBusiness;
            } else {
                Log.e("StatusSaverMainFragment", "business empty");
                DocumentFile fromTreeUri = DocumentFile.fromTreeUri(myselectedActivity.getApplicationContext(), Uri.parse(namedataprefs_business));
                if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
                    documentFilesWhatsappBusiness = fromTreeUri.listFiles();
                    return documentFilesWhatsappBusiness;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (isAdded()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(myselectedActivity);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.pernecessory);
                    alertBuilder.setMessage(R.string.write_neesory);
                    alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
            } else {
                ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (isAdded()) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(myselectedActivity);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.pernecessory);
                alertBuilder.setMessage(R.string.write_neesory);
                alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE));
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        } else {
            ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runMyTask();
            } else {
                checkAgain();
            }
        }
    }


    private void runMyTask() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (isAdded()) {
            new MyDataLoadTask(requireActivity()).execute();
        }
//            }
//        }, 100);
    }


    private class MyDataLoadTask extends AsyncTask<Void, Void, Void> {

        private Activity activity;

        public MyDataLoadTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                WhatsappStoryAdapter recyclerViewAdapter = new WhatsappStoryAdapter(this.activity, getData());
                this.activity.runOnUiThread(() -> {
                    recyclerView.setLayoutManager(new GridLayoutManager(this.activity, 2));
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();

                });

            } catch (Throwable e) {
                e.printStackTrace();
                this.activity.runOnUiThread(() -> {
                    Toast.makeText(activity, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            return null;
        }
    }
    public static String FOLDER_NAME = "/WhatsApp/";
    public static String FOLDER_NAME_Whatsappbusiness = "/WhatsApp Business/";
    public static String FOLDER_NAME_Whatsapp_and11 = "/Android/media/com.whatsapp/WhatsApp/";
    public static String FOLDER_NAME_Whatsapp_and11_B = "/Android/media/com.whatsapp.w4b/WhatsApp Business/";

    private ArrayList<Object> getData() {


        if (Build.VERSION.SDK_INT >= 30) {
            if (filesList != null) {
                filesList = new ArrayList<>();
            }
            WAStoryModel f;
            String targetPath = "";
            DocumentFile[] allFiles;
            try {
                DocumentFile[] allFilesWhatsapp = (documentFilesWhatsapp != null) ? documentFilesWhatsapp : getDataFromWhatsAppFolder();
                if (isWhatsAppBusinessAvaliable) {
                    DocumentFile[] allFilesBusiness = (documentFilesWhatsappBusiness != null) ? documentFilesWhatsappBusiness : getDataFromWhatsAppBusinessFolder();
                    allFiles = ArrayUtils.addAll(allFilesBusiness, allFilesWhatsapp);

                } else {
                    allFiles = allFilesWhatsapp;

                }

                for (DocumentFile allFile : Objects.requireNonNull(allFiles)) {
                    Uri file = allFile.getUri();
                    f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(file);
                    f.setPath(allFile.getUri().toString());
                    f.setFilename(allFile.getUri().getLastPathSegment());


                    if (!allFile.getUri().toString().contains(".nomedia") && !allFile.getUri().toString().equals("")) {
                        filesList.add(f);
                    }
                    requireActivity().runOnUiThread(() -> {
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else {
            if (filesList != null) {
                filesList = new ArrayList<>();
            }
            WAStoryModel f;
            String targetPath = "";

            SharedPreferences prefs = myselectedActivity.getSharedPreferences("whatsapp_pref", MODE_PRIVATE);
            String name = prefs.getString("whatsapp", "main");//"No name defined" is the default value.


            String mainWP = Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER_NAME + "Media/.Statuses";

            String mainWP_11 = Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER_NAME_Whatsapp_and11 + "Media/.Statuses";

            String mainWP_B = Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER_NAME_Whatsappbusiness + "Media/.Statuses";

            String mainWP_B_11 = Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER_NAME_Whatsapp_and11_B + "Media/.Statuses";

            String mainFM = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FMWhatsApp/Media/.Statuses";

            String mainGB = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBWhatsApp/Media/.Statuses";


            File targetDirector1 = new File(mainWP);
            File targetDirector2 = new File(mainWP_11);

            File targetDirector3 = new File(mainWP_B);

            File targetDirector4 = new File(mainWP_B_11);

            File targetDirector5 = new File(mainFM);

            File targetDirector6 = new File(mainGB);


            ArrayList<File> aList = new ArrayList<>(Arrays.asList(targetDirector1.listFiles() != null ? Objects.requireNonNull(targetDirector1.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector2.listFiles() != null ? Objects.requireNonNull(targetDirector2.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector3.listFiles() != null ? Objects.requireNonNull(targetDirector3.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector4.listFiles() != null ? Objects.requireNonNull(targetDirector4.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector5.listFiles() != null ? Objects.requireNonNull(targetDirector5.listFiles()) : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector6.listFiles() != null ? Objects.requireNonNull(targetDirector6.listFiles()) : new File[]{new File("")}));


            File[] n = new File[aList.size()];
            File[] files;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Arrays.setAll(n, aList::get);
                files = n;

            } else {

                files = targetDirector1.listFiles();
            }


            try {
                Arrays.sort(Objects.requireNonNull(files), (Comparator) (o1, o2) -> Long.compare(((File) o2).lastModified(), ((File) o1).lastModified()));

                for (File file : files) {
                    f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(Uri.fromFile(file));
                    f.setPath(file.getAbsolutePath());
                    f.setFilename(file.getName());

                    if (!file.getName().equals(".nomedia") && !file.getPath().equals("")) {
                        filesList.add(f);
                    }
                }

                requireActivity().runOnUiThread(() -> {

                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (filesList!=null){
            if (filesList.size()>0){
                requireActivity().runOnUiThread(() -> noresultfound.setVisibility(View.GONE));
            }else {
                requireActivity().runOnUiThread(() -> noresultfound.setVisibility(View.VISIBLE));
            }
        }else {
            requireActivity().runOnUiThread(() -> noresultfound.setVisibility(View.VISIBLE));
        }
        return filesList;

    }

    void setActivityAfterAttached() {

        try {

            if (getActivity() != null && isAdded()) {
                myselectedActivity = getActivity();
            }
        } catch (Exception e) {
            myselectedActivity = requireActivity();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1239) {
            runMyTask();

        }
        else if (requestCode == OPEN_DOCOMENT_TREE_REQUEST_CODE && resultCode == -1) {

            Uri uri = Objects.requireNonNull(data).getData();


            try {
                myselectedActivity.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            namedataprefs = uri.toString();

            new SharedPrefsMainApp(requireActivity()).setPREFERENCE_whatsapp(uri.toString());

            if (isWhatsAppBusinessAvaliable && namedataprefs_business.equals("")) {
            } else {
                Open();
                new Handler(Looper.getMainLooper()).postDelayed(() -> new MyDataLoadTask(requireActivity()).execute(), 2000);
            }


        }
        else if (requestCode == OPEN_DOCOMENT_TREE_REQUEST_CODE_BUSINESS && resultCode == -1) {

            Uri uri = Objects.requireNonNull(data).getData();


            try {
                myselectedActivity.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            namedataprefs_business = uri.toString();
            new SharedPrefsMainApp(requireActivity()).setPREFERENCE_whatsappbusiness(uri.toString());

            new Handler(Looper.getMainLooper()).postDelayed(() -> new MyDataLoadTask(requireActivity()).execute(), 2000);

        }

    }

    private void Open() {
        Intent localIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        boolean installed = appInstalledOrNot(getContext(),"com.whatsapp");
        if (installed) {
            try {
                startActivity(localIntent);
            } catch (ActivityNotFoundException localActivityNotFoundException) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
            }
        } else {
            Toast.makeText(getContext(), "Whatsapp not install in your device!", Toast.LENGTH_SHORT).show();
        }
    }
}