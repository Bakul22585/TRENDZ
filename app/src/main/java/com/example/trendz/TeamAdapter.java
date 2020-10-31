package com.example.trendz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> data;
    private Context context;

    private static final int ENTRY_ITEM = 0;
    private static final int ADS_ITEM = 1;

    public TeamAdapter(List<Object> data, Context context){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ENTRY_ITEM:
                View EntryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_team_list, parent, false);
                return new TeamViewHolder(EntryView);
            case ADS_ITEM:
            default:
                View BannerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad, parent, false);
                return new BannerAdViewHolder(BannerView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case ENTRY_ITEM:
                TeamViewHolder teamViewHolder = (TeamViewHolder) holder;
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
                TeamEntry teamEntry = (TeamEntry) data.get(position);
                teamViewHolder.number.setText(teamEntry.getNumber());
                teamViewHolder.name.setText(teamEntry.getName());
                teamViewHolder.sponsor.setText(teamEntry.getSponsor());
                teamViewHolder.amount.setText(format.format(Double.parseDouble(teamEntry.getAmount())));
                teamViewHolder.phone.setText(teamEntry.getPhone());
                teamViewHolder.date.setText(teamEntry.getDate());
            case  ADS_ITEM:
            default:
//                BannerAdViewHolder adViewHolder = (BannerAdViewHolder) holder;
//                AdView adView = (AdView) data.get(position);
//                ViewGroup adCardView = (ViewGroup) adViewHolder.itemView;

//                if (adCardView.getChildCount() > 0) {
//                    adCardView.removeAllViews();
//                }
//
//                if (adView.getParent() != null) {
//                    ((ViewGroup) adView.getParent()).removeView(adView);
//                }

//                adCardView.addView(adView);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position%TeamIncomeActivity.ITEM_PER_AD == 0) {
            return ADS_ITEM;
        } else {
            return ENTRY_ITEM;
        }
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView number, name, sponsor, amount, phone, date;
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.teamlistnumbertextView);
            name = itemView.findViewById(R.id.teamlistnametextView);
            sponsor = itemView.findViewById(R.id.teamlistsponsortextView);
            amount = itemView.findViewById(R.id.teamlistamounttextView);
            phone = itemView.findViewById(R.id.teamlistphonetextView);
            date = itemView.findViewById(R.id.teamlistdatetextView);
        }
    }

    public class BannerAdViewHolder extends RecyclerView.ViewHolder {

        public BannerAdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
