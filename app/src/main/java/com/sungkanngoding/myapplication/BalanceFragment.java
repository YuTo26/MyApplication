package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BalanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BalanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnTukarBlender;

    public BalanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BalanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BalanceFragment newInstance(String param1, String param2) {
        BalanceFragment fragment = new BalanceFragment();
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
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        // Initialize button
        Button btnTukarBlender = view.findViewById(R.id.btnTukarBlender);
        Button btnTukarStrika = view.findViewById(R.id.btnTukarStrika);
        Button btnTukarMagicom = view.findViewById(R.id.btnTukarMagicom);
        Button btnTukarSpeaker = view.findViewById(R.id.btnTukarSpeaker);
        Button btnTukarAirbuds = view.findViewById(R.id.btnTukarAirbuds);
        Button btnTukarPowerbank = view.findViewById(R.id.btnTukarPowerbank);
        Button btnTukarMinyak = view.findViewById(R.id.btnTukarMinyak);
        Button btnTukarBeras = view.findViewById(R.id.btnTukarBeras);
        Button btnTukarMie = view.findViewById(R.id.btnTukarMie);

        // Set onClick listener
        btnTukarBlender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Blender Miyako");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarStrika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Strika Philips");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarMagicom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Magicom Cosmos");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Speaker JBL");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarAirbuds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Airbuds Samsung");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarPowerbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Powerbank Robot");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarMinyak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Minyak Goreng 1L");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarBeras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Beras 5Kg");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btnTukarMie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ExchangeConfirmationActivity saat tombol ditekan
                Intent gotoExchangeConfirmation = new Intent(getActivity(), ExchangeConfirmationActivity.class);

                // Menambahkan data tambahan jika diperlukan
                // Misalnya, jenis barang yang akan ditukar
                gotoExchangeConfirmation.putExtra("jenis_barang", "Mie Rebus");

                // Mulai activity baru
                startActivity(gotoExchangeConfirmation);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });




        return view;
    }
}
