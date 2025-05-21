package com.example.sidebar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UpcomingActivity extends AppCompatActivity {

    private ListView listViewUpcoming;
    private UpcomingAdapter adapter;
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private View menuButton;// Ton helper pour accéder aux tâches

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_activity); // ta layout contenant la ListView

        listViewUpcoming = findViewById(R.id.listViewUpcoming);
        databaseHelper = new DatabaseHelper(this);

        List<Tache> taches = databaseHelper.getTachesAVenir();  // Méthode à créer
        List<ListItem> groupedList = groupTachesByDate(taches);

        adapter = new UpcomingAdapter(this, groupedList);
        listViewUpcoming.setAdapter(adapter);
        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_upcoming);
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

            if (id == R.id.upcoming) {
                drawerLayout.closeDrawers(); // Already on upcoming
            } else if (id == R.id.today) {
                // Aller à TodayActivity
                startActivity(new Intent(this, TodayActivity.class));
                finish();

            }
            else if (id == R.id.week) {
                // Aller à TodayActivity
                startActivity(new Intent(this, WeekActivity.class));
                finish();

            }
            else if (id == R.id.late) {
                // Aller à TodayActivity
                startActivity(new Intent(this, LateTaskActivity.class));
                finish();

            }
            else if (id == R.id.priorite) {
                // Aller à TodayActivity
                startActivity(new Intent(this, PrioriteActivity.class));
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

    private List<ListItem> groupTachesByDate(List<Tache> taches) {
        List<ListItem> result = new ArrayList<>();
        // LinkedHashMap pour garder l'ordre d'insertion
        Map<String, List<Tache>> map = new LinkedHashMap<>();

        // Grouper les tâches par date
        for (Tache t : taches) {
            String date = t.getDateString();
            if (!map.containsKey(date)) {
                map.put(date, new ArrayList<>());
            }
            map.get(date).add(t);
        }

        // Construire la liste mixte header + tâches
        for (Map.Entry<String, List<Tache>> entry : map.entrySet()) {
            result.add(new HeaderItem(entry.getKey()));  // header avec la date complète
            for (Tache t : entry.getValue()) {
                result.add(new TacheItem(t));
            }
        }
        return result;
    }
}
