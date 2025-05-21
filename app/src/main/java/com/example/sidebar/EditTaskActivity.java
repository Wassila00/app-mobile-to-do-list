package com.example.sidebar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    private EditText inputTitle, inputDescription, inputHeureDebut, inputHeureFinPrevue, inputRappel1, inputRappel2;
    private Spinner spinnerCategorie, spinnerPriorite;
    private Button btnModifier, btnSupprimer;

    private DatabaseHelper dbHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dbHelper = new DatabaseHelper(this);

        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        inputHeureDebut = findViewById(R.id.inputHeureDebut);
        inputHeureFinPrevue = findViewById(R.id.inputHeureFinPrevue);
        inputRappel1 = findViewById(R.id.inputRappel1);
        inputRappel2 = findViewById(R.id.inputRappel2);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        spinnerPriorite = findViewById(R.id.spinnerPriorite);
        btnModifier = findViewById(R.id.btnAjouterTache);
        btnSupprimer = findViewById(R.id.btnSupprimerTache); // Bouton supprimé

        // Charger les options dans les spinners
        ArrayAdapter<String> categorieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dbHelper.getCategorieNames());
        spinnerCategorie.setAdapter(categorieAdapter);

        ArrayAdapter<String> prioriteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dbHelper.getPrioriteTitres());
        spinnerPriorite.setAdapter(prioriteAdapter);

        taskId = getIntent().getIntExtra("tacheId", -1);





        // Charger les données de la tâche à éditer
        Tache tache = dbHelper.getTacheById(taskId);
        if (tache == null) {
            Toast.makeText(this, "Tâche introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (tache != null) {
            inputTitle.setText(tache.getTitre());
            inputDescription.setText(tache.getDescription());
            inputHeureDebut.setText(tache.getHeureDebut());
            inputHeureFinPrevue.setText(tache.getHeureFinPrevue());
        }

        btnModifier.setText("Modifier la tâche");
        btnModifier.setOnClickListener(v -> {
            String titre = inputTitle.getText().toString().trim();
            String description = inputDescription.getText().toString().trim();
            String heureDebut = inputHeureDebut.getText().toString().trim();
            String heureFin = inputHeureFinPrevue.getText().toString().trim();
            String rappel1 = inputRappel1.getText().toString().trim();
            String rappel2 = inputRappel2.getText().toString().trim();

            String dateModif = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("user_id", -1);
            int categorieId = dbHelper.getCategorieIdByName(spinnerCategorie.getSelectedItem().toString());
            int prioriteId = dbHelper.getPrioriteIdByTitre(spinnerPriorite.getSelectedItem().toString());

            dbHelper.updateTache(taskId, titre, description, heureDebut, heureFin, rappel1, rappel2, dateModif, categorieId, prioriteId, userId);
            Toast.makeText(this, "Tâche mise à jour", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnSupprimer.setOnClickListener(v -> {
            dbHelper.deleteTache(taskId);
            Toast.makeText(this, "Tâche supprimée", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
