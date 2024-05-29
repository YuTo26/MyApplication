package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ViewPager2 viewPager;
    private Handler slideHandler = new Handler();
    private List<Integer> images = Arrays.asList(
            R.drawable.bg_kuningan,
            R.drawable.bg_kertas,
            R.drawable.bg_botol_kaca,
            R.drawable.bg_botol_plastik
    );

    public HomeFragment() {
        // Required empty public constructor
    }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi ViewPager2
        viewPager = rootView.findViewById(R.id.viewPager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(getContext(), images);
        viewPager.setAdapter(adapter);

        // Set auto slide show
        slideHandler.postDelayed(slideRunnable, 3000); // 3 detik

        // Temukan tombol plastic_bottle dari layout fragment_home
        LinearLayout btn_plastic_bottle = rootView.findViewById(R.id.btn_plastic_bottle);
        LinearLayout btn_metal = rootView.findViewById(R.id.btn_metal);
        LinearLayout btn_glass_bottle = rootView.findViewById(R.id.btn_glass_bottle);
        LinearLayout btn_paper = rootView.findViewById(R.id.btn_paper);
        LinearLayout btn_old_books = rootView.findViewById(R.id.btn_old_books);
        LinearLayout btn_cardboard = rootView.findViewById(R.id.btn_cardboard);

        // Tambahkan OnClickListener untuk tombol plastic_bottle
        btn_plastic_bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotoitemdetails = new Intent(getActivity(), ItemDetailActivity.class);
                gotoitemdetails.putExtra("jenis_item", "Botol Plastik");
                startActivity(gotoitemdetails);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        // Tambahkan OnClickListener untuk tombol besi
        btn_metal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotoitemdetails = new Intent(getActivity(), ItemDetailActivity.class);
                gotoitemdetails.putExtra("jenis_item", "Besi");
                startActivity(gotoitemdetails);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btn_glass_bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotoitemdetails = new Intent(getActivity(), ItemDetailActivity.class);
                gotoitemdetails.putExtra("jenis_item", "Botol Kaca");
                startActivity(gotoitemdetails);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btn_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotoitemdetails = new Intent(getActivity(), ItemDetailActivity.class);
                gotoitemdetails.putExtra("jenis_item", "Kertas");
                startActivity(gotoitemdetails);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btn_old_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotoitemdetails = new Intent(getActivity(), ItemDetailActivity.class);
                gotoitemdetails.putExtra("jenis_item", "Buku Bekas");
                startActivity(gotoitemdetails);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        btn_cardboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil Intent untuk membuka ItemDetailActivity saat tombol ditekan
                Intent gotoitemdetails = new Intent(getActivity(), ItemDetailActivity.class);
                gotoitemdetails.putExtra("jenis_item", "Kardus");
                startActivity(gotoitemdetails);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });

        // Temukan elemen-elemen UI untuk menampilkan data pengguna
        TextView usernameTextView = rootView.findViewById(R.id.username);
        TextView bioTextView = rootView.findViewById(R.id.bio);
        TextView saldoTextView = rootView.findViewById(R.id.saldo_user);
        ImageView photoProfileImageView = rootView.findViewById(R.id.photo_home_user);

        // Inisialisasi Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Dapatkan UID pengguna yang saat ini login
        String uid = auth.getCurrentUser().getUid();

        // Inisialisasi DatabaseReference untuk mengambil data pengguna dari Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        // Ambil data pengguna dari Firebase Realtime Database dan dengarkan perubahan secara real-time
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Dapatkan data pengguna dari snapshot
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    Long saldo = dataSnapshot.child("balance").getValue(Long.class);
                    String photoUrl = dataSnapshot.child("profile_picture_url").getValue(String.class);

                    // Set data pengguna ke TextView
                    usernameTextView.setText(username);
                    bioTextView.setText(bio);
                    saldoTextView.setText("Rp. " + saldo);

                    // Set foto profil pengguna ke ImageView menggunakan Picasso
                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        Picasso.get().load(photoUrl).centerCrop().fit().into(photoProfileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Kembalikan rootView yang sudah diubah
        return rootView;
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int nextItem = (currentItem + 1) % images.size();
            viewPager.setCurrentItem(nextItem, true);
            slideHandler.postDelayed(this, 8000); // 3 detik
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable, 3000); // 3 detik
    }
}
