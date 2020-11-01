package com.example.trendz;

import android.content.Context;
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

public class AutopoolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> data;
    private Context context;

    private static final int ENTRY_ITEM = 0;
    private static final int ADS_ITEM = 1;

    public AutopoolAdapter(List<Object> data, Context context){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ENTRY_ITEM:
                View EntryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_autopool_list, parent, false);
                return new AutopoolViewHolder(EntryView);
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
                AutopoolViewHolder autopoolViewHolder = (AutopoolViewHolder) holder;
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
                AutopoolEntry autopoolEntry = (AutopoolEntry) data.get(position);
                autopoolViewHolder.number.setText(autopoolEntry.getNumber());
                autopoolViewHolder.level.setText(autopoolEntry.getLevel());
                autopoolViewHolder.amount.setText(format.format(Double.parseDouble(autopoolEntry.getAmount())));
                autopoolViewHolder.date.setText(autopoolEntry.getDate());
            case  ADS_ITEM:
            default:
//                BannerAdViewHolder adViewHolder = (BannerAdViewHolder) holder;
//                AdView adView = (AdView) data.get(position);
//                ViewGroup adCardView = (ViewGroup) adViewHolder.itemView;
//
//                if (adCardView.getChildCount() > 0) {
//                    adCardView.removeAllViews();
//                }
//
//                if (adView.getParent() != null) {
//                    ((ViewGroup) adView.getParent()).removeView(adView);
//                }
//
//                adCardView.addView(adView);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position%AutoPoolIncomeActivity.ITEM_PER_AD == 0) {
            return ADS_ITEM;
        } else {
            return ENTRY_ITEM;
        }
    }

    public class AutopoolViewHolder extends RecyclerView.ViewHolder {
        TextView number, level, amount, date;
        public AutopoolViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.autopoollistnumberTv);
            level = itemView.findViewById(R.id.autopoollistlevelTV);
            amount = itemView.findViewById(R.id.autopoollistamountTV);
            date = itemView.findViewById(R.id.autopoollistdateTV);
        }
    }

    public class BannerAdViewHolder extends RecyclerView.ViewHolder {

        public BannerAdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
