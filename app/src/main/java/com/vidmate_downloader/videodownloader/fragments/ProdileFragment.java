package com.vidmate_downloader.videodownloader.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.activities.AboutUs;
import com.vidmate_downloader.videodownloader.activities.HistoryActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProdileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProdileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProdileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProdileFragment newInstance(String param1, String param2) {
        ProdileFragment fragment = new ProdileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View v;
    RelativeLayout aboutUs,rateUs,shareApp;
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_prodile, container, false);
        activity = (Activity)getContext();
        aboutUs = v.findViewById(R.id.aboutUs);
        rateUs = v.findViewById(R.id.rateUs);
        shareApp = v.findViewById(R.id.shareApp);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,AboutUs.class));
            }
        });
        rateUs.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                activity.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
            }
        });
        shareApp.setOnClickListener(v -> {
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
        });
        v.findViewById(R.id.myLibrary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        v.findViewById(R.id.myOrders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerV,new DownloadFragment())
                        .addToBackStack("DownloadFragment").commit();
            }
        });
        v.findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://snobtube.blogspot.com/2023/05/privacy-policy.htm")));
                }catch (Exception e){
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        v.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, HistoryActivity.class)
                        .putExtra("type","h"));
            }
        });
        v.findViewById(R.id.favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, HistoryActivity.class)
                        .putExtra("type","b"));
            }
        });
        return v;
    }
}