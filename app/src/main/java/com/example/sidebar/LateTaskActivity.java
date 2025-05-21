package com.example.sidebar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LateTaskActivity extends AppCompatActivity {

    private ListView listView;
    private LateTaskAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Object> itemsAdapter; // Pour String (header) + Tache
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private View menuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_late_task);

        listView = findViewById(R.id.listViewLate);
        drawerLayout = findViewById(R.id.drawer_layout_late);
        navView = findViewById(R.id.nav_view);
        menuButton = findViewById(R.id.button_menu);

        dbHelper = new DatabaseHelper(this);

        loadLateTasks();



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

            if (id == R.id.late) {
                // On est déjà sur cette activité donc on ferme juste le drawer
                drawerLayout.closeDrawers();
            } else if (id == R.id.today) {
                // Aller à TodayActivity
                startActivity(new Intent(this, TodayActivity.class));
                finish();
            } else if (id == R.id.upcoming) {
                // Aller à TodayActivity
                startActivity(new Intent(this, UpcomingActivity.class));
                finish();
            }
            else if (id == R.id.week) {
                // Aller à MainActivity
                startActivity(new Intent(this, WeekActivity.class));
                finish();
            }
            else if (id == R.id.late) {
                // Aller à MainActivity
                startActivity(new Intent(this, LateTaskActivity.class));
                finish();
            }else if (id == R.id.calendar) {
                startActivity(new Intent(this, AgendaActivity.class));
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

    private void loadLateTasks() {
        List<Tache> toutesTaches = dbHelper.getAllTaches();
        List<Tache> tachesEnRetard = new ArrayList<>();

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date now = new Date();
        String todayStr = sdfDate.format(now);
        String currentTimeStr = sdfTime.format(now);

        for (Tache t : toutesTaches) {
            try {
                Date dateTache = t.getDate(); // type java.util.Date
                if (dateTache == null) continue;

                String dateTacheStr = sdfDate.format(dateTache);

                // Ignorer les tâches futures (date_tache > aujourd’hui)
                if (dateTacheStr.compareTo(todayStr) > 0) continue;

                Date heureFinPrevue = sdfTime.parse(t.getHeureFinPrevue());

                if (!t.isEstTerminee()) {
                    // Cas 1 : Tâche non terminée
                    boolean isLate = false;

                    if (dateTacheStr.compareTo(todayStr) < 0) {
                        isLate = true; // jour précédent → en retard
                    } else if (dateTacheStr.equals(todayStr)) {
                        Date heureActuelle = sdfTime.parse(currentTimeStr);
                        if (heureFinPrevue != null && heureActuelle != null && heureActuelle.after(heureFinPrevue)) {
                            isLate = true;
                        }
                    }

                    if (isLate) {
                        t.setRetardMinutes(-1); // -1 = non terminée
                        tachesEnRetard.add(t);
                    }

                } else {
                    // Cas 2 : Tâche terminée avec retard
                    Date heureFinReelle = sdfTime.parse(t.getHeureFinReelle());
                    if (heureFinPrevue != null && heureFinReelle != null && heureFinReelle.after(heureFinPrevue)) {
                        long retardMillis = heureFinReelle.getTime() - heureFinPrevue.getTime();
                        long retardMinutes = retardMillis / (60 * 1000);
                        t.setRetardMinutes(retardMinutes);
                        tachesEnRetard.add(t);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Grouper les tâches par date
        Map<String, List<Tache>> tachesParJour = new LinkedHashMap<>();
        for (Tache t : tachesEnRetard) {
            String date = (t.getDate() != null) ? sdfDate.format(t.getDate()) : "Date inconnue";
            tachesParJour.computeIfAbsent(date, k -> new ArrayList<>()).add(t);
        }

        // Créer la liste finale (headers + tâches)
        itemsAdapter = new ArrayList<>();
        for (String date : tachesParJour.keySet()) {
            itemsAdapter.add(date); // Header de jour
            itemsAdapter.addAll(tachesParJour.get(date));
        }

        adapter = new LateTaskAdapter(this, itemsAdapter);
        listView.setAdapter(adapter);
    }
}
