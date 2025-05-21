package com.example.sidebar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayList;

public class TodayActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ImageButton menuButton;

    private TacheAdapter adapter;

    private EditText searchEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        // Initialisation base
        listView = findViewById(R.id.listViewToday);
        dbHelper = new DatabaseHelper(this);

        List<Tache> tachesDuJour = dbHelper.getTachesDuJour();
        TacheAdapter adapter = new TacheAdapter(this, tachesDuJour);
        listView.setAdapter(adapter);

        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        menuButton = findViewById(R.id.button_menu);

        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        navView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            int id = item.getItemId();

            if (id == R.id.today) {
                drawerLayout.closeDrawers(); // Already on Today
            } else if (id == R.id.upcoming) {
                // Aller à TodayActivity
                startActivity(new Intent(this, UpcomingActivity.class));
                finish();

            }else if (id == R.id.week) {
                // Aller à TodayActivity
                startActivity(new Intent(this, WeekActivity.class));
                finish();

            }
            else if (id == R.id.late) {
                // Aller à TodayActivity
                startActivity(new Intent(this, LateTaskActivity.class));
                finish();

            }
            else if (id == R.id.main) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

            // Ajoute d'autres cas de menu si besoin...

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
