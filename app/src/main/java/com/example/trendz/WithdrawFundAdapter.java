package com.example.trendz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WithdrawFundAdapter extends RecyclerView.Adapter {

    private List<Object> data;
    private Context context;

    public WithdrawFundAdapter(List<Object> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_fund_list, parent, false);
        return new WithdrawFundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WithdrawFundViewHolder withdrawFundViewHolder = (WithdrawFundViewHolder) holder;
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        WithdrawFundEntry withdrawFundEntry = (WithdrawFundEntry) data.get(position);

        withdrawFundViewHolder.number.setText(withdrawFundEntry.getNumber());
        withdrawFundViewHolder.type.setText(withdrawFundEntry.getType());
        withdrawFundViewHolder.amount.setText(format.format(Double.parseDouble(withdrawFundEntry.getAmount())));
        withdrawFundViewHolder.date.setText(withdrawFundEntry.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class WithdrawFundViewHolder extends RecyclerView.ViewHolder {
        TextView number, type, amount, date;
        public WithdrawFundViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.withdrawFundlistnumberTv);
            type = itemView.findViewById(R.id.withdrawFundlistTypeTV);
            amount = itemView.findViewById(R.id.withdrawFundlistamountTV);
            date = itemView.findViewById(R.id.withdrawFundlistdateTV);
        }
    }
}
