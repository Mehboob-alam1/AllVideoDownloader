package com.vidmate_downloader.videodownloader.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.activities.WebActivity;
import com.vidmate_downloader.videodownloader.adapter.NormalTabsAdapter;
import com.vidmate_downloader.videodownloader.tiktok.HistoryBookMarkSQLite;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabsFragment newInstance(String param1, String param2) {
        TabsFragment fragment = new TabsFragment();
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
    RecyclerView recyclerView;
    Activity activity;
    NormalTabsAdapter normalTabsAdapter;

    HistoryBookMarkSQLite db;
    LinearLayout LL1,LL2;
    TextView tabs,tab_private;
    String Selected="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tabs, container, false);
        activity=getActivity();
        LL1=v.findViewById(R.id.LL1);
        LL2=v.findViewById(R.id.LL2);
        tabs=v.findViewById(R.id.tabs);
        tab_private=v.findViewById(R.id.tab_private);
        recyclerView=v.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        db = new HistoryBookMarkSQLite(activity);
        normalTabsAdapter=new NormalTabsAdapter(activity,db.getAllTabsPages(),"Tab");
        recyclerView.setAdapter(normalTabsAdapter);
        Selected="Tab";
        v.findViewById(R.id.add_new_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Selected.equals("Private")){
                    startActivity(new Intent(activity, WebActivity.class)
                            .putExtra("url","https://www.google.com/")
                            .putExtra("type","private"));
                }else {
                    startActivity(new Intent(activity, WebActivity.class)
                            .putExtra("url","https://www.google.com/"));
                }
            }
        });
        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selected="Tab";
                normalTabsAdapter=new NormalTabsAdapter(activity,db.getAllTabsPages(),"Tab");
                recyclerView.setAdapter(normalTabsAdapter);
                LL1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tabs.setTextColor(getResources().getColor(R.color.colorPrimary));
                LL2.setBackgroundColor(getResources().getColor(R.color.white));
                tab_private.setTextColor(getResources().getColor(R.color.black));
            }
        });
        tab_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selected="Private";
                normalTabsAdapter=new NormalTabsAdapter(activity,db.getAllTabsPages2(),"Private");
                recyclerView.setAdapter(normalTabsAdapter);
                LL1.setBackgroundColor(getResources().getColor(R.color.white));
                tabs.setTextColor(getResources().getColor(R.color.black));
                LL2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tab_private.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Selected.equals("Private")){
            normalTabsAdapter=new NormalTabsAdapter(activity,db.getAllTabsPages2(),"Private");
            recyclerView.setAdapter(normalTabsAdapter);
        }else {
            normalTabsAdapter=new NormalTabsAdapter(activity,db.getAllTabsPages(),"Tab");
            recyclerView.setAdapter(normalTabsAdapter);
        }
    }
}