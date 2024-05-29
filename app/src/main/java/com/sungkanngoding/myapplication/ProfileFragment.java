package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView bioTextView;

    private DatabaseReference userRef;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        photoImageView = rootView.findViewById(R.id.add_photo_user);
        nameTextView = rootView.findViewById(R.id.tvName);
        bioTextView = rootView.findViewById(R.id.tvBio);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String photoUrl = dataSnapshot.child("profile_picture_url").getValue(String.class);
                    String fullName = dataSnapshot.child("full_name").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);

                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        Picasso.get().load(photoUrl).centerCrop().fit().into(photoImageView);
                    }
                    nameTextView.setText(fullName);
                    bioTextView.setText(bio);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }

        LinearLayout llProfileSettings = rootView.findViewById(R.id.llProfileSettings);
        LinearLayout llPrivacySettings = rootView.findViewById(R.id.llPrivacySettings);
        LinearLayout llGeneralQuestion = rootView.findViewById(R.id.llGeneralQuestion);
        LinearLayout llPrivacyPolicy = rootView.findViewById(R.id.llPrivacyPolicy);
        Button btnSignOut = rootView.findViewById(R.id.btnSignOut);

        llProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEditProfile = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(gotoEditProfile);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });

        llPrivacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoeditprivacy = new Intent(getActivity(), PrivacySettingsActivity.class);
                startActivity(gotoeditprivacy);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        llGeneralQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotogeneralquestion = new Intent(getActivity(), GeneralQuestionActivity.class);
                startActivity(gotogeneralquestion);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprivacypolicy = new Intent(getActivity(), PrivacyPolicyActivity.class);
                startActivity(gotoprivacypolicy);

                // Terapkan animasi fade in/out
                getActivity().overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent signOutIntent = new Intent(getActivity(), SignInActivity.class);
                signOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signOutIntent);
            }
        });

        return rootView;
    }
}

