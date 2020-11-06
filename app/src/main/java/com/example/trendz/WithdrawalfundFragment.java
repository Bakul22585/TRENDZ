package com.example.trendz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WithdrawalfundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WithdrawalfundFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AdView mAdView;

    EditText BankAccountNumber, BankIFSCCode, Amount;
    Spinner WithdrawOption;
    TextView BankAccountNumberError, BankIFSCCodeError, WithdrawOptionError, AmountError;
    Button Withdraw;
    int maxAmount;

    public WithdrawalfundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WithdrawalfundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WithdrawalfundFragment newInstance(String param1, String param2) {
        WithdrawalfundFragment fragment = new WithdrawalfundFragment();
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
        View root = inflater.inflate(R.layout.fragment_withdrawalfund, container, false);

        mAdView = root.findViewById(R.id.adViewWithdrawaRequest);
        BankAccountNumber = root.findViewById(R.id.editTextWithdrawalBankAccountNumber);
        BankAccountNumberError = root.findViewById(R.id.BankAccountNumberError);
        BankIFSCCode = root.findViewById(R.id.editTextWithdrawalifscCode);
        BankIFSCCodeError = root.findViewById(R.id.withdrawalIfscCodeError);
        WithdrawOption = root.findViewById(R.id.selectWithdrawOption);
        WithdrawOptionError = root.findViewById(R.id.selectWithdrawOptionError);
        Amount = root.findViewById(R.id.editTextWithdrawAmount);
        AmountError = root.findViewById(R.id.withdrawAmountError);
        Withdraw = root.findViewById(R.id.btnAddWithdrawalRequest);

        final SessionManagement sessionManagement = new SessionManagement(getActivity());

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_unit));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        List<String> option = new ArrayList<>();
        option.add(0, "Choose Withdraw Option");
        option.add("Auto Pool Income");    option.add("Team Income");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, option);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        WithdrawOption.setAdapter(dataAdapter);

        WithdrawOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals("Choose Withdraw Option")) {
                    String item = parent.getItemAtPosition(position).toString();
                    if (item == "Auto Pool Income") {
                        maxAmount = Integer.parseInt(sessionManagement.getSession("autoPoolIncome"));
                    }

                    if (item == "Team Income") {
                        maxAmount = Integer.parseInt(sessionManagement.getSession("teamIncome"));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Withdraw.setEnabled(false);
                Boolean successBoolean = true;
                int amount = 0;

                if (BankAccountNumber.getText().toString().equals("")) {
                    successBoolean = false;
                    BankAccountNumberError.setVisibility(View.VISIBLE);
                }
                if (!BankAccountNumber.getText().toString().equals("")) {
                    BankAccountNumberError.setVisibility(View.GONE);
                }

                if (BankIFSCCode.getText().toString().equals("")) {
                    successBoolean = false;
                    BankIFSCCodeError.setVisibility(View.VISIBLE);
                }
                if (!BankIFSCCode.getText().toString().equals("")) {
                    BankIFSCCodeError.setVisibility(View.GONE);
                }

                if (WithdrawOption.getSelectedItem().toString().equals("Choose Withdraw Option")) {
                    successBoolean = false;
                    WithdrawOptionError.setVisibility(View.VISIBLE);
                }
                if (!WithdrawOption.getSelectedItem().toString().equals("Choose Withdraw Option")) {
                    WithdrawOptionError.setVisibility(View.GONE);
                }

                if (Amount.getText().toString().equals("")) {
                    successBoolean = false;
                    AmountError.setVisibility(View.VISIBLE);
                }

                if (!Amount.getText().toString().equals("")) {
                    AmountError.setVisibility(View.GONE);
                    amount = Integer.parseInt(Amount.getText().toString());
                }

                if (amount < 100 && !Amount.getText().toString().equals("")) {
                    successBoolean = false;
                    AmountError.setText("Please enter an amount greater than 100");
                    AmountError.setVisibility(View.VISIBLE);
                }

                if (amount > maxAmount && !Amount.getText().toString().equals("")) {
                    successBoolean = false;
                    AmountError.setText("Insufficient funds, Please try again after some time");
                    AmountError.setVisibility(View.VISIBLE);
                }

                if (successBoolean) {
                    Toast.makeText(getActivity(), "Withdraw amount api in-progress.", Toast.LENGTH_SHORT).show();
                } else {
                    Withdraw.setEnabled(true);
                }
            }
        });

        return root;
    }
}