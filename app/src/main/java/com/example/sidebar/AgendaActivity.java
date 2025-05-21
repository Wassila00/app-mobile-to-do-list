package com.example.sidebar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AgendaActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> allEvents = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private View menuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        drawerLayout = findViewById(R.id.drawer_layout_calendar);
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
            if (id == R.id.calendar) {
                // On est déjà sur cette activité donc on ferme juste le drawer
                drawerLayout.closeDrawers();
            } else if (id == R.id.today) {
                startActivity(new Intent(this, TodayActivity.class));
                finish();
            } else if (id == R.id.upcoming) {
                startActivity(new Intent(this, UpcomingActivity.class));
                finish();
            } else if (id == R.id.week) {
                startActivity(new Intent(this, WeekActivity.class));
                finish();
            } else if (id == R.id.late) {
                startActivity(new Intent(this, LateTaskActivity.class));
                finish();
            } else if (id == R.id.main) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else if (id == R.id.priorite) {
                startActivity(new Intent(this, PrioriteActivity.class));
                finish();
            }


            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        databaseHelper = new DatabaseHelper(this);

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventAdapter(new ArrayList<>(), event -> {
            // 🎯 Récupération de l'ID de la tâche depuis la description
            try {
                int taskId = Integer.parseInt(event.getDescription());
                Intent intent = new Intent(AgendaActivity.this, EditTaskActivity.class);
                intent.putExtra("tacheId", taskId);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(AgendaActivity.this, "Tâche introuvable", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(v -> {
            String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date(calendarView.getDate()));
            Intent intent = new Intent(AgendaActivity.this, AddTaskActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivity(intent);
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            loadEvents(account);
        } else {
            Toast.makeText(this, "Connectez-vous d'abord.", Toast.LENGTH_SHORT).show();
            finish();
        }

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            filterEventsByDate(selectedDate);
        });
    }

    private void loadEvents(GoogleSignInAccount account) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                this, Collections.singleton("https://www.googleapis.com/auth/calendar"));
        credential.setSelectedAccount(account.getAccount());

        Calendar service = new Calendar.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Taskify")
                .build();

        new AsyncTask<Void, Void, List<Event>>() {
            @Override
            protected List<Event> doInBackground(Void... voids) {
                try {
                    Events events = service.events().list("primary")
                            .setMaxResults(100)
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();
                    return events.getItems();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Event> events) {
                if (events != null) {
                    int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .getInt("user_id", -1);

                    if (userId != -1) {
                        for (Event event : events) {
                            databaseHelper.insererTacheDepuisGoogleCalendar(event, userId);
                        }
                        Toast.makeText(AgendaActivity.this, events.size() + " événements importés", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AgendaActivity.this, "Utilisateur non identifié", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute();
    }

    private void filterEventsByDate(String selectedDate) {
        List<Event> filtered = new ArrayList<>();
        int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("user_id", -1);

        // ✅ Récupérer toutes les tâches locales (manuelles + importées)
        List<Tache> taches = databaseHelper.getTachesByDateAndUser(selectedDate, userId);
        for (Tache tache : taches) {
            Event fakeEvent = new Event();
            fakeEvent.setSummary(tache.getTitre());

            // ➕ Injecter l’ID dans la description pour le récupérer plus tard
            fakeEvent.setDescription(String.valueOf(tache.getId()));

            EventDateTime start = new EventDateTime()
                    .setDate(new com.google.api.client.util.DateTime(tache.getDateString()));
            fakeEvent.setStart(start);

            filtered.add(fakeEvent);
        }

        adapter.setEvents(filtered);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date(calendarView.getDate()));
        filterEventsByDate(selectedDate);
    }
}
