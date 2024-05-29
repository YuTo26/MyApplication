package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.sungkanngoding.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;

    private static final String MENU_HOME = "home";
    private static final String MENU_HISTORY = "history";
    private static final String MENU_BALANCE = "balance";
    private static final String MENU_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Periksa apakah pengguna sudah login
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Jika belum login, arahkan ke SignInActivity
            Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signInIntent);
            finish();
            return;
        }

        // Periksa peran pengguna dan arahkan ke halaman yang sesuai
        String userRole = sharedPreferences.getString("userRole", "");
        if ("admin".equals(userRole)) {
            Intent adminIntent = new Intent(MainActivity.this, AdminPanelActivity.class);
            startActivity(adminIntent);
            finish();
            return;
        } else if ("user".equals(userRole)) {
            replaceFragment(new HomeFragment());
        }

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
        // Animasi fade in/out
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_page, R.anim.fade_out_page);
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }
}
