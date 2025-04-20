package com.example.sidebar;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Créer une instance de DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);


        // Obtenir une base de données en écriture (cela va créer la base si elle n'existe pas)
        dbHelper.getWritableDatabase();

        // Afficher un message pour indiquer que la base de données a été créée
        Toast.makeText(this, "Base de données créée avec succès", Toast.LENGTH_SHORT).show();



    }
}