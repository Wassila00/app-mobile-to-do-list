package com.example.sidebar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

public class AddTaskActivity extends AppCompatActivity {

    private EditText inputTitle, inputDescription, inputHeureDebut, inputHeureFin, inputRappel1, inputRappel2;
    private Spinner spinnerCategorie, spinnerPriorite;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private String dateTache;
    private TFLitePriorityPredictor priorityPredictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        inputHeureDebut = findViewById(R.id.inputHeureDebut);
        inputHeureFin = findViewById(R.id.inputHeureFinPrevue);
        inputRappel1 = findViewById(R.id.inputRappel1);
        inputRappel2 = findViewById(R.id.inputRappel2);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        spinnerPriorite = findViewById(R.id.spinnerPriorite);
        btnSave = findViewById(R.id.btnAjouterTache);

        dbHelper = new DatabaseHelper(this);
        dateTache = getIntent().getStringExtra("selectedDate");
        dbHelper.insererCategoriesSiVide();
        dbHelper.insererPrioritesSiVide();

        try {
            priorityPredictor = new TFLitePriorityPredictor(this);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du chargement du modèle de priorité", Toast.LENGTH_SHORT).show();
        }

        loadCategories();
        loadPriorites();

        btnSave.setOnClickListener(v -> enregistrerTache());
    }

    private void loadCategories() {
        List<String> categories = dbHelper.getCategorieNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);
    }

    private void loadPriorites() {
        List<String> priorites = dbHelper.getPrioriteTitres();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorites);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorite.setAdapter(adapter);
    }

    private void enregistrerTache() {
        String titre = inputTitle.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        String heureDebut = inputHeureDebut.getText().toString().trim();
        String heureFinPrevue = inputHeureFin.getText().toString().trim();
        String rappel1 = inputRappel1.getText().toString().trim();
        String rappel2 = inputRappel2.getText().toString().trim();

        String categorieNom = spinnerCategorie.getSelectedItem().toString();
        String prioriteTitre;

        if (spinnerPriorite.getSelectedItem() == null) {
            // Si aucune priorité n'est sélectionnée, utiliser le modèle pour prédire
            if (priorityPredictor != null) {
                // Préparer les features pour le modèle
                float[] features = prepareFeatures(titre, description, heureDebut, heureFinPrevue);
                int predictedPriority = priorityPredictor.predict(features);
                
                // Convertir la priorité prédite en titre
                switch (predictedPriority) {
                    case 1:
                        prioriteTitre = "High";
                        break;
                    case 2:
                        prioriteTitre = "Medium";
                        break;
                    case 3:
                        prioriteTitre = "Low";
                        break;
                    default:
                        prioriteTitre = "Medium"; // Valeur par défaut
                }
                Toast.makeText(this, "Priorité prédite par l'IA: " + prioriteTitre, Toast.LENGTH_SHORT).show();
            } else {
                prioriteTitre = "Medium"; // Valeur par défaut si le modèle n'est pas disponible
                Toast.makeText(this, "Utilisation de la priorité par défaut: Medium", Toast.LENGTH_SHORT).show();
            }
        } else {
            prioriteTitre = spinnerPriorite.getSelectedItem().toString();
        }

        if (titre.isEmpty() || dateTache == null) {
            Toast.makeText(this, "Veuillez remplir le titre et la date", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non identifié", Toast.LENGTH_SHORT).show();
            return;
        }

        int categorieId = dbHelper.getCategorieIdByName(categorieNom);
        int prioriteId = dbHelper.getPrioriteIdByTitre(prioriteTitre);

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        dbHelper.insertManualTask(
                titre, description, dateTache, heureDebut, heureFinPrevue,
                rappel1, rappel2, now, now,
                0, userId, categorieId, prioriteId
        );

        Toast.makeText(this, "Tâche ajoutée avec succès !", Toast.LENGTH_SHORT).show();
        finish();
    }

    private float[] prepareFeatures(String titre, String description, String heureDebut, String heureFinPrevue) {
        // Créer un tableau de features pour le modèle
        // Vous devrez adapter ces features en fonction de votre modèle
        float[] features = new float[4];
        
        // Exemple de features (à adapter selon votre modèle)
        features[0] = titre.length() / 100.0f; // Normaliser la longueur du titre
        features[1] = description.length() / 500.0f; // Normaliser la longueur de la description
        
        // Convertir les heures en valeurs numériques
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date debut = sdf.parse(heureDebut);
            Date fin = sdf.parse(heureFinPrevue);
            if (debut != null && fin != null) {
                features[2] = debut.getHours() / 24.0f; // Normaliser l'heure de début
                features[3] = (fin.getTime() - debut.getTime()) / (24 * 60 * 60 * 1000.0f); // Normaliser la durée
            }
        } catch (Exception e) {
            features[2] = 0.5f; // Valeur par défaut
            features[3] = 0.5f; // Valeur par défaut
        }
        
        return features;
    }
}
