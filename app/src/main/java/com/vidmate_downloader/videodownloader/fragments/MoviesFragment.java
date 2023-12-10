package com.vidmate_downloader.videodownloader.fragments;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.vidmate_downloader.videodownloader.Model.DataModel;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.adapter.DownloadAdapter;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
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
    RecyclerView rv_videos;
    View view;
    LinearLayout isEmptyList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_download, container, false);
        rv_videos = view.findViewById(R.id.rv_videos);
        isEmptyList = view.findViewById(R.id.isEmptyList);

        loadMedia();
        return view;
    }
    File file;
    ArrayList<DataModel> downloadImageList = new ArrayList<>();
    ArrayList<DataModel> downloadVideoList = new ArrayList<>();
    public void loadMedia() {

        file = Utils.MoviesDir;

        downloadImageList.clear();
        downloadVideoList.clear();
        if (!file.isDirectory()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                displayfiles(file, rv_videos);
            }
        } else {
            displayfiles(file, rv_videos);
        }
    }
    DownloadAdapter mAdapter;
    void displayfiles(File file, final RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file);
        if (listfilemedia.length != 0) {
              isEmptyList.setVisibility(View.GONE);
        } else {
              isEmptyList.setVisibility(View.VISIBLE);
        }
        int i = 0;
        while (i < listfilemedia.length) {
            if (listfilemedia[i].isDirectory()){
                displayfiles(listfilemedia[i],mRecyclerView);
            }else {
                downloadImageList.add(new DataModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName()));
            }
            i++;
        }

        if (downloadImageList.size() > 0) {
              isEmptyList.setVisibility(View.GONE);
        } else {
              isEmptyList.setVisibility(View.VISIBLE);
        }
        mAdapter = new DownloadAdapter(getActivity(),downloadImageList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return null;
        }
        File[] sortedByDate = folder.listFiles();
        if (sortedByDate == null || sortedByDate.length <= 1) {
            return sortedByDate;
        }
        Arrays.sort(sortedByDate, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        return sortedByDate;
    }
}