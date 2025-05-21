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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class WeekActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private View menuButton;

    private ListView listViewWeek;
    private DatabaseHelper dbHelper;
    private WeekAdapter adapter;
    private List<ListItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_week);

        // Initialisation des vues
        listViewWeek = findViewById(R.id.listViewWeek);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        menuButton = findViewById(R.id.button_menu);

        dbHelper = new DatabaseHelper(this);

        loadTasksForWeek();

        // Gestion clic bouton hamburger
        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Gestion clic sur éléments du menu de navigation
        navView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            int id = item.getItemId();

            if (id == R.id.week) {
                // On est déjà sur cette activité donc on ferme juste le drawer
                drawerLayout.closeDrawers();
            } else if (id == R.id.today) {
                // Aller à TodayActivity
                startActivity(new Intent(this, TodayActivity.class));
                finish();
            }else if (id == R.id.upcoming) {
                // Aller à TodayActivity
                startActivity(new Intent(this, UpcomingActivity.class));
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
                // Aller à MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

            // Autres cas si besoin...

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void loadTasksForWeek() {
        List<Tache> allTasks = dbHelper.getTachesForCurrentWeek();
        itemList = new ArrayList<>();

        Map<String, List<Tache>> tasksByDate = new TreeMap<>(); // Trie par date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.getDefault());

        for (Tache tache : allTasks) {
            Date dateObj = tache.getDate();
            String dateKey;
            if (dateObj != null) {
                dateKey = sdf.format(dateObj);
            } else {
                dateKey = "Non daté";
            }
            if (!tasksByDate.containsKey(dateKey)) {
                tasksByDate.put(dateKey, new ArrayList<>());
            }
            tasksByDate.get(dateKey).add(tache);
        }

        for (Map.Entry<String, List<Tache>> entry : tasksByDate.entrySet()) {
            itemList.add(new HeaderItem(entry.getKey()));
            for (Tache t : entry.getValue()) {
                itemList.add(new TacheItem(t));
            }
        }

        adapter = new WeekAdapter(this, itemList);
        listViewWeek.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
