package com.example.sidebar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class PrioriteActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private EditText editTextSearch;
    private ImageButton buttonMenu;
    private PrioriteAdapter adapter;
    private List<Tache> allTaches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periorite);

        // Initialisation des vues
        drawerLayout = findViewById(R.id.drawerlayoutpriorite);
        listView = findViewById(R.id.listviewprioritetaches);
        editTextSearch = findViewById(R.id.edittextsearchpriorite);
        buttonMenu = findViewById(R.id.buttonmenupriorite);
        NavigationView navigationView = findViewById(R.id.navigationviewpriorite);

        // Chargement des tâches triées
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        allTaches = dbHelper.getAllTachesTrieesParDateEtPriorite();

        adapter = new PrioriteAdapter(this, allTaches);
        listView.setAdapter(adapter);

        // Gestion du clic sur le bouton hamburger
        buttonMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Filtrage des tâches pendant la saisie dans la barre de recherche
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });

        // Gestion du clic sur les éléments du menu de navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            int id = item.getItemId();

            if (id == R.id.priorite) {
                // Déjà sur cette activité
                drawerLayout.closeDrawers();
            } else if (id == R.id.today) {
                startActivity(new Intent(this, TodayActivity.class));
                finish();
            } else if (id == R.id.week) {
                startActivity(new Intent(this, WeekActivity.class));
                finish();
            } else if (id == R.id.upcoming) {
                startActivity(new Intent(this, UpcomingActivity.class));
                finish();
            } else if (id == R.id.late) {
                startActivity(new Intent(this, LateTaskActivity.class));
                finish();
            } else if (id == R.id.main) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
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
