package com.example.cafe.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.cafe.R;
import com.example.cafe.databinding.ActivityMainBinding;
import com.example.cafe.screens.auth.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    public NavController navController;
    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = hostFragment.getNavController();
        mToolbar = mBinding.toolbar;
        mBottomNavigation = mBinding.bottomNavigationView;

        BottomNavigationView bottomNavigationView = mBinding.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(mToolbar, navController);
//        setSupportActionBar(mToolbar);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NotNull NavController navController, @NotNull NavDestination navDestination, @org.jetbrains.annotations.Nullable Bundle bundle) {
                switch (navDestination.getId()) {
                    case R.id.authFragment2:
                    case R.id.enterPinFragment2:
                    case R.id.loginFragment: {
                        showToolbarAndNavBar();
                        break;
                    }
                    default:
                        mToolbar.setVisibility(View.VISIBLE);
                        mBottomNavigation.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        userViewModel.getMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(mBinding.getRoot(), s, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public UserViewModel getUserViewModel() {
        return userViewModel;
    }

    private void showToolbarAndNavBar() {
        mBottomNavigation.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
    }
}
