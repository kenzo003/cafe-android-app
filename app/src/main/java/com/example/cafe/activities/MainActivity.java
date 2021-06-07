package com.example.cafe.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cafe.R;
import com.example.cafe.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import static com.example.cafe.utilits.constants.APP_ACTIVITY;
import static com.example.cafe.utilits.constants.AUTH;
import static com.example.cafe.utilits.constants.initFireBase;

;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    public NavController navController;
    BottomNavigationView mBottomNavigation;
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
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

        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = hostFragment.getNavController();
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        APP_ACTIVITY = this;
        initFireBase();
    }

    @SuppressLint("RestrictedApi")
    private void initFunc() {
        setSupportActionBar(mToolbar);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.navigation_graph);

//        Не авторизован
        if (AUTH.getCurrentUser() == null) {
            navGraph.setStartDestination(R.id.authFragment);
            //Отображаем окно Авторизации
        } else {
//            constants.AUTH.signOut();
            AUTH.getCurrentUser().getEmail().toString();
            setTitle(AUTH.getCurrentUser().getEmail().toString());
            mBottomNavigation.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);

            navGraph.setStartDestination(R.id.newsFragment);
            //Обработка клика по элементу меню
            mBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                    return MainActivity.this.onNavigationItemSelected(item);
                }
            });
        }
        navController.setGraph(navGraph);
    }

    public void showToolbarAndNavBar() {
        mBottomNavigation.setVisibility(View.VISIBLE);
        mToolbar.setVisibility(View.VISIBLE);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        //Извлекаем все Fragment из backstack
        navController.popBackStack();

        switch (item.getItemId()) {
            case R.id.btn_news: {
                navController.navigate(R.id.newsFragment);
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_catalog: {
                navController.navigate(R.id.catalogFragment);
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_promo: {
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_basket: {
                navController.navigate(R.id.basketFragment);
                setTitle(item.getTitle());
                break;
            }
            case R.id.btn_account: {
                navController.navigate(R.id.accountFragment);
                setTitle(item.getTitle());
                AUTH.signOut();
                finish();
                break;
            }
            default:
                return false;
        }
        return true;
    }
}
