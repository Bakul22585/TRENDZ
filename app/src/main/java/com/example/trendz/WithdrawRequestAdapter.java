package com.example.trendz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendz.R;
import com.example.trendz.WithdrawRequestEntry;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WithdrawRequestAdapter extends RecyclerView.Adapter {

    private List<Object> data;
    private Context context;
    private OnItemClickListener Listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setListener(OnItemClickListener listener) {
        Listener = listener;
    }

    public WithdrawRequestAdapter(List<Object> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_withdraw_request_list, parent, false);
        return new WithdrawRequestViewHolder(view, Listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WithdrawRequestViewHolder withdrawRequestViewHolder = (WithdrawRequestViewHolder) holder;
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        WithdrawRequestEntry withdrawRequestEntry = (WithdrawRequestEntry) data.get(position);

        withdrawRequestViewHolder.number.setText(withdrawRequestEntry.getNumber());
        withdrawRequestViewHolder.name.setText(withdrawRequestEntry.getName());
        withdrawRequestViewHolder.accountNumber.setText(withdrawRequestEntry.getAccount_number());
        withdrawRequestViewHolder.IfscCode.setText(withdrawRequestEntry.getIfsc_code());
        withdrawRequestViewHolder.amount.setText(format.format(Double.parseDouble(withdrawRequestEntry.getAmount())));
        withdrawRequestViewHolder.date.setText(withdrawRequestEntry.getDate());

        if (withdrawRequestEntry.getStatus().equals("1")){
            withdrawRequestViewHolder.Pay.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class WithdrawRequestViewHolder extends RecyclerView.ViewHolder {
        TextView number, name, accountNumber, IfscCode, amount, date;
        Button Pay;
        public WithdrawRequestViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            number = itemView.findViewById(R.id.txtWithdrawRequestListNumber);
            name = itemView.findViewById(R.id.txtWithdrawRequestListUserName);
            accountNumber = itemView.findViewById(R.id.txtWithdrawRequestListBankAccountNumber);
            IfscCode = itemView.findViewById(R.id.txtWithdrawRequestListBankIFSCCode);
            amount = itemView.findViewById(R.id.txtWithdrawRequestListAmount);
            date = itemView.findViewById(R.id.txtWithdrawRequestListDate);
            Pay = itemView.findViewById(R.id.btnWithdrawRequestListPay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(position);
                }
            });

            Pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onDeleteClick(position);
                }
            });
        }
    }
}
