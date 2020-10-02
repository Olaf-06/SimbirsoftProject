package com.example.simbirsoftproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mySharedPreferences";
    public static final String APP_PREFERENCES_NAME = "username";
    public static final String APP_PREFERENCES_SETTINGS_OF_ENTRY = "entry";

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences mySharedPreferences;
    private Boolean entryBool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("logMy","В методе OnCreate");
        super.onCreate(savedInstanceState);
        Log.d("logMy","Скоро поставится SetContentView");
        setContentView(R.layout.activity_main);
        Log.d("logMy","Запуск main activity");
        mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        entryBool = mySharedPreferences.getBoolean(APP_PREFERENCES_SETTINGS_OF_ENTRY, true);
        if (entryBool) {
            Intent intent = new Intent(MainActivity.this, loginActivity.class);
            startActivity(intent);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragmentAchievements,
                R.id.fragmentChat,
                R.id.fragmentGroupLessons,
                R.id.fragmentMain,
                R.id.fragmentShares,
                R.id.fragmentSimulators,
                R.id.fragmentTheory,
                R.id.fragmentTimetable,
                R.id.fragmentTreners)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
