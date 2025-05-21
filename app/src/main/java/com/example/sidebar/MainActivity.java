package com.example.sidebar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Créer une instance de DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        dbHelper.getWritableDatabase(); // Crée la base si elle n'existe pas

        Toast.makeText(this, "Base de données créée avec succès", Toast.LENGTH_SHORT).show();

        // ➕ Insérer une tâche factice pour tester l'affichage



        // 🟣 Initialisation de la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 🟣 Initialisation du Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 🟣 Activation du bouton hamburger (menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 🟣 Méthode appelée quand l'utilisateur clique sur un élément du menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.today) {
            Intent intent = new Intent(this, TodayActivity.class);
            startActivity(intent);
        } else if (id == R.id.week) {
            Intent intent = new Intent(this, WeekActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.late) {  // Ajout pour LateTaskActivity
            Intent intent = new Intent(this, LateTaskActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.upcoming) {  // Ajout pour LateTaskActivity
            Intent intent = new Intent(this,UpcomingActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.priorite) {  // Ajout pour LateTaskActivity
            Intent intent = new Intent(this,PrioriteActivity.class);
            startActivity(intent);
        }
        // Tu peux gérer les autres éléments ici : upcoming, week, etc.

        drawerLayout.closeDrawers(); // Fermer le menu après clic
        return true;
    }
}
