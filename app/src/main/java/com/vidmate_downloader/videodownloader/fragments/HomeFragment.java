package com.vidmate_downloader.videodownloader.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vidmate_downloader.videodownloader.R;
import com.vidmate_downloader.videodownloader.Utils;
import com.vidmate_downloader.videodownloader.activities.InstagramActivity;
import com.vidmate_downloader.videodownloader.activities.StatusActivity;
import com.vidmate_downloader.videodownloader.activities.TwitterActivity;
import com.vidmate_downloader.videodownloader.activities.WebActivity;
import com.vidmate_downloader.videodownloader.activities.WebFragment;
import com.vidmate_downloader.videodownloader.tiktok.TiktokActivity;
import com.vidmate_downloader.videodownloader.database.MyDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    RelativeLayout vLayout;
    Activity activity;
    ImageView instagram, titkok, snapchat, fb;
    MyDatabase database;
    EditText searchTxt;
    WebFragment webFragment;

    public interface OnDataPass {
        public void onDataPass(String data);
    }

    OnDataPass dataPasser;
    CardView instagramCard;
    CardView
            facebookCard;
    CardView
            twitterCard;
    CardView
            tiktokCard;

    CardView
            pinterestCard;
    CardView
            whatsappCard;
    CardView
            wallpaperCard;
    CardView
            familyCard;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        activity = (Activity) getContext();
        database = new MyDatabase(activity);
        searchTxt = v.findViewById(R.id.searchTxt);
        vLayout = v.findViewById(R.id.vLayout);
        instagram = v.findViewById(R.id.instagram);
        titkok = v.findViewById(R.id.titkok);
        snapchat = v.findViewById(R.id.snapchat);
        fb = v.findViewById(R.id.facebook);

        instagramCard = v.findViewById(R.id.cardInstagram);
        facebookCard = v.findViewById(R.id.cardFacebook);
        twitterCard = v.findViewById(R.id.cardTwitter);
        tiktokCard = v.findViewById(R.id.cardTikTok);
        pinterestCard = v.findViewById(R.id.cardPinterest);
        whatsappCard = v.findViewById(R.id.cardWhatsapp);
        wallpaperCard = v.findViewById(R.id.cardWallpaper);
        familyCard = v.findViewById(R.id.cardFamilyApps);

        instagramCard.setOnClickListener(v -> selectCard(instagramCard));
        facebookCard.setOnClickListener(v -> selectCard(facebookCard));
        twitterCard.setOnClickListener(v -> selectCard(twitterCard));
        tiktokCard.setOnClickListener(v -> selectCard(tiktokCard));
        pinterestCard.setOnClickListener(v -> selectCard(pinterestCard));
        whatsappCard.setOnClickListener(v -> selectCard(whatsappCard));
        wallpaperCard.setOnClickListener(v -> selectCard(wallpaperCard));
        familyCard.setOnClickListener(v -> selectCard(facebookCard));

        instagram.setOnClickListener(v -> startActivity(new Intent(activity, InstagramActivity.class)));
        titkok.setOnClickListener(v -> startActivity(new Intent(activity, TiktokActivity.class)));
        snapchat.setOnClickListener(v -> startActivity(new Intent(activity, TwitterActivity.class)));
        fb.setOnClickListener(v ->
                startActivity(new Intent(activity, WebActivity.class)
                        .putExtra("url", "https://www.facebook.com/")));

        v.findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, StatusActivity.class));
            }
        });
        v.findViewById(R.id.wallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, WebActivity.class)
                        .putExtra("url", "https://unsplash.com/wallpapers"));
            }
        });
        v.findViewById(R.id.tv_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuide();
            }
        });
        searchTxt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
        ShimmerTextView shimmerTextView = v.findViewById(R.id.largeBannerShimmer);
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        Utils.loadNativeAdAdMob(v.findViewById(R.id.largeBannerShimmer), getString(R.string.admob_Native), R.layout.google_native_large, activity, v.findViewById(R.id.framelayout)
                , shimmerTextView);
        // Utils.BannerAd(v.findViewById(R.id.framelayout),getActivity());
        return v;
    }

    private void selectCard(CardView card) {
        // Reset the background for all cards
        resetCardBackground();

        // Set the background for the selected card
        card.setCardBackgroundColor(Color.parseColor("#FF5733")); // Change to your desired color
    }

    private void resetCardBackground() {
        // Reset the background for all cards


        facebookCard.setCardBackgroundColor(Color.WHITE);

        twitterCard.setCardBackgroundColor(Color.WHITE);

        instagramCard.setCardBackgroundColor(Color.WHITE);
                facebookCard.setCardBackgroundColor(Color.WHITE);
        twitterCard.setCardBackgroundColor(Color.WHITE);
                tiktokCard.setCardBackgroundColor(Color.WHITE);
        pinterestCard.setCardBackgroundColor(Color.WHITE);
                whatsappCard.setCardBackgroundColor(Color.WHITE);
        wallpaperCard.setCardBackgroundColor(Color.WHITE);
                familyCard.setCardBackgroundColor(Color.WHITE);

        // ... Repeat the process for other cards
    }

    private void performSearch() {
        searchTxt.clearFocus();
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchTxt.getWindowToken(), 0);
        //...perform search
        String s = searchTxt.getText().toString();
        s = s.trim();

        boolean matches = Patterns.WEB_URL.matcher(s).matches();
        String url;
        if (matches)
            url = s;
        else {
            //this url well open in web view as google search
            url = "https://www.google.com/search?q=" + s.replace(" ", "%20");
        }
        //  startActivity(new Intent(activity, WebActivity.class).putExtra("url",url));
/*        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        webFragment=new WebFragment();
        webFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerV,webFragment)
                .addToBackStack("WebFragment").commit();*/
        passData(url);
    }

    private void showGuide() {
        final Dialog guide = new Dialog(activity);
        guide.setContentView(R.layout.dialog_guide2);

        Button button = guide.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guide.dismiss();
            }
        });

        guide.show();
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }
}