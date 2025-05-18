package com.example.sidebar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputUsername, inputEmail, inputPassword, inputPasswordConfirm;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register3);

        // Initialiser les vues
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordConfirm = findViewById(R.id.inputPasswordConfirm);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView btnAlreadyHaveAccount = findViewById(R.id.AlreadyHaveAccount);

        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);

        // Gestion des insets pour le design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gérer l'inscription
        btnRegister.setOnClickListener(v -> {
            String username = inputUsername.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String confirmPassword = inputPasswordConfirm.getText().toString().trim();

            // Vérifier les champs vides
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier que les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier si l'email est déjà utilisé
            if (dbHelper.isEmailRegistered(email)) {
                Toast.makeText(RegisterActivity.this, "Cet email est déjà utilisé", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insérer l'utilisateur dans la base de données
            boolean success = dbHelper.addUser(username, email, password);
            if (success) {
                Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();

                // Rediriger vers LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
            }
        });

        // Gérer le clic sur "Already Have Account?"
        btnAlreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
