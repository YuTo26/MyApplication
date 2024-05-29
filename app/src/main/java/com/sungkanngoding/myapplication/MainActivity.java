package com.sungkanngoding.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sungkanngoding.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private static final String MENU_HOME = "home";
    private static final String MENU_HISTORY = "history";
    private static final String MENU_BALANCE = "balance";
    private static final String MENU_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        String itemId = getResources().getResourceEntryName(item.getItemId());
        switch (itemId) {
            case MENU_HOME:
                fragment = new HomeFragment();
                break;
            case MENU_HISTORY:
                fragment = new SalesFragment();
                break;
            case MENU_BALANCE:
                fragment = new BalanceFragment();
                break;
            case MENU_PROFILE:
                fragment = new ProfileFragment();
                break;
            default:
                return false;
        }
        replaceFragment(fragment);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Mengganti container dari FrameLayout ke FragmentContainerView
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }
}
