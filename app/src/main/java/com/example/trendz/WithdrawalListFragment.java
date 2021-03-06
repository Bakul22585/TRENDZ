package com.example.trendz;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WithdrawalListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WithdrawalListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AdView mAdView, mAdView2;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    WithdrawFundAdapter adapter;
    List<Object> WithdrawEntry = new ArrayList<>();
    Boolean IsScrolled = true;
    int CurrentItems, TotalItem, ScrollOutItem, PageIndex = 0;
    ProgressBar progressBar;
    String LoginUserId;
    SwipeRefreshLayout swipeRefreshLayout;

    public WithdrawalListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WithdrawalListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WithdrawalListFragment newInstance(String param1, String param2) {
        WithdrawalListFragment fragment = new WithdrawalListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_withdrawal_list, container, false);

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = root.findViewById(R.id.adViewWithdrawList1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = root.findViewById(R.id.adViewWithdrawList2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        recyclerView = root.findViewById(R.id.withdrawFundList);
        swipeRefreshLayout = root.findViewById(R.id.withdrawalFundListSwipeRefresh);
        manager = new LinearLayoutManager(getActivity());

        SessionManagement sessionManagement = new SessionManagement(getActivity());
        LoginUserId = sessionManagement.getSession("id");

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.DialogTheme);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageIndex = 0;
                WithdrawEntry.clear();
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getwithdrawentrylist?id=" + LoginUserId + "&index=" + PageIndex;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject UserData = data.getJSONObject(i);

                                WithdrawFundEntry withdrawFundEntry = new WithdrawFundEntry();
                                withdrawFundEntry.setNumber(UserData.getString("index"));
                                withdrawFundEntry.setType(UserData.getString("type"));
                                withdrawFundEntry.setAmount(UserData.getString("amount"));
                                withdrawFundEntry.setDate(UserData.getString("created"));
                                WithdrawEntry.add(withdrawFundEntry);
                            }
                            adapter = new WithdrawFundAdapter(WithdrawEntry, getActivity());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(manager);

                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && IsScrolled) {
                                        IsScrolled = true;
                                    }
                                }

                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (dy > 0) {
                                        CurrentItems = manager.getChildCount();
                                        TotalItem = manager.getItemCount();
                                        ScrollOutItem = manager.findFirstVisibleItemPosition();

                                        int Total = CurrentItems + ScrollOutItem;
                                        if (IsScrolled && (Total == TotalItem)) {
//                                        progressBar.setVisibility(View.VISIBLE);
                                            PageIndex++;
                                            IsScrolled = false;
                                            fetchData();
                                        }
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }
        );

        requestQueue.add(stringRequest);

        return root;
    }

    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://restrictionsolution.com/ci/trendz_world/user/getwithdrawentrylist?id=" + LoginUserId + "&index=" + PageIndex;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        JSONArray data = res.getJSONArray("data");

                        if (data.length() == 0) {
                            IsScrolled = false;
                        }

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject UserData = data.getJSONObject(i);

                            WithdrawFundEntry withdrawFundEntry = new WithdrawFundEntry();
                            withdrawFundEntry.setNumber(UserData.getString("index"));
                            withdrawFundEntry.setType(UserData.getString("type"));
                            withdrawFundEntry.setAmount(UserData.getString("amount"));
                            withdrawFundEntry.setDate(UserData.getString("created"));
                            WithdrawEntry.add(withdrawFundEntry);
                        }
                        adapter.notifyDataSetChanged();
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
    }
}