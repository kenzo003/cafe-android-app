package com.example.cafe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cafe.R;
import com.example.cafe.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private NavController navController;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFields();
        initFunc();
    }

    private void initFields() {
        mToolbar = mBinding.toolbar;
        mBottomNavigation = mBinding.bottomNavigationView;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    }

    private void initFunc() {
        setSupportActionBar(mToolbar);
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.btn_account){
                    navController.popBackStack(R.id.accountFragment, true);
                    setTitle(item.getTitle());
                }
                if (item.getItemId() == R.id.btn_catalog){
                    navController.popBackStack(R.id.catalogFragment, true);
                    setTitle(item.getTitle());
                }
                if (item.getItemId() == R.id.btn_basket){
                    navController.popBackStack(R.id.basketFragment, true);
                    setTitle(item.getTitle());
                }
                if (item.getItemId() == R.id.btn_news){
                    navController.popBackStack(R.id.newsFragment,true);
                    setTitle(item.getTitle());
                }
                if (item.getItemId() == R.id.btn_promo){
//                    navController.navigate(R.id.catalogFragment);
                    setTitle(item.getTitle());
                }
//                item.setChecked(true);
                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();

                return true;
            }
        });

    }


}
