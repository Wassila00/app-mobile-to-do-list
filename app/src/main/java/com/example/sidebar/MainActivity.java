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

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Créer une instance de DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase(); // Crée la base si elle n'existe pas
        Toast.makeText(this, "Base de données créée avec succès", Toast.LENGTH_SHORT).show();

    }



}
