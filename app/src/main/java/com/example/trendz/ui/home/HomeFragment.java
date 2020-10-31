package com.example.trendz.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trendz.AutoPoolIncomeActivity;
import com.example.trendz.R;
import com.example.trendz.RegisterActivity;
import com.example.trendz.SessionManagement;
import com.example.trendz.TeamIncomeActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    TextView LoginFullname, Loginusername, TeamIncome, AutoPoolIncome, BalanceIncome;
    Button AutoPool, Team;
    private AdView mAdView, mAdView2, mAdView3, mAdView4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        LoginFullname = root.findViewById(R.id.HomeLoginuserfullname);
        Loginusername = root.findViewById(R.id.HomeLoginusername);
        AutoPool = root.findViewById(R.id.Btnautopool);
        Team = root.findViewById(R.id.Btnteam);
        TeamIncome = root.findViewById(R.id.TxtHomeScreenTeamAmount);
        AutoPoolIncome = root.findViewById(R.id.TxtHomeScreenAutoPoolAmount);
        BalanceIncome = root.findViewById(R.id.Txthomescreenamount);
        final NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));

        SessionManagement sessionManagement = new SessionManagement(getActivity());

        String LoginUsername = sessionManagement.getSession("FullName");
        String LoginUserEmail = sessionManagement.getSession("username");
        String LoginUserId = sessionManagement.getSession("id");

        LoginFullname.setText(LoginUsername);
        Loginusername.setText(LoginUserEmail);

        AutoPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AutoPoolIncomeActivity.class);
                startActivity(intent);
            }
        });

        Team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TeamIncomeActivity.class);
                startActivity(intent);
            }
        });

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = root.findViewById(R.id.homeAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getincome?id=" + LoginUserId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        TeamIncome.setText(format.format(Double.parseDouble(res.getString("referral_incomme"))));
                        AutoPoolIncome.setText(format.format(Double.parseDouble(res.getString("autopool_income"))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
        );
        requestQueue.add(stringRequest);

        return root;
    }
}