package com.shurik.memwor_24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.shurik.memwor_24.browser.BrowserActivity;
import com.shurik.memwor_24.memwor.MemworActivity;
import com.shurik.memwor_24.pizza_planet.activities.IntroductoryActivity;
import com.shurik.memwor_24.pumpwimo.activities.PumpWiMoActivity;
import com.shurik.memwor_24.pumpwimo.activities.SplashScreenActivity;

public class DopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dop);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_memwor);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.nav_memwor:
                Intent intent = new Intent(DopActivity.this, MemworActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_browser:
                Intent intent1 = new Intent(DopActivity.this, BrowserActivity.class);
                startActivity(intent1);
                break;

            case R.id.nav_pumpwimo:
                Intent intent2 = new Intent(DopActivity.this, SplashScreenActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_pizza_planet:
                Intent intent3 = new Intent(DopActivity.this, IntroductoryActivity.class);
                startActivity(intent3);
                break;
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}