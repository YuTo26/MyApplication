package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SalesFragment extends Fragment {

    public SalesFragment() {
        // Required empty public constructor
    }

    public static SalesFragment newInstance() {
        return new SalesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sales, container, false);

        LinearLayout llHistoryOne = rootView.findViewById(R.id.llHistoryOne);

        // Tambahkan OnClickListener untuk tombol Details Transaction
        llHistoryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotodetailtransaction = new Intent(getActivity(), TransactionDetailsActivity.class);
                startActivity(gotodetailtransaction);
            }
        });

        return rootView;
    }
}
