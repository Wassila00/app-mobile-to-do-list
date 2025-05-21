// LoginActivity.java
package com.example.sidebar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private GoogleSignInHelper googleSignInHelper;
    private DatabaseHelper dbHelper;
    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        Button btnLogin = findViewById(R.id.btnlogin);
        Button btnGoogleLogin = findViewById(R.id.btnGoogle);

        dbHelper = new DatabaseHelper(this);
        googleSignInHelper = new GoogleSignInHelper(this);

        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.validateUser(email, password)) {
                int userId = dbHelper.getUserIdByEmailAndPassword(email, password);
                if (userId != -1) {
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    prefs.edit().putInt("user_id", userId).apply();

                    Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, AgendaActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Utilisateur introuvable.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoogleLogin.setOnClickListener(v -> googleSignInHelper.signIn(this));
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                googleSignInHelper.handleSignInResult(data, this);

                if (googleSignInHelper.isSignedIn()) {
                    String email = googleSignInHelper.getSignedInEmail(this);

                    int userId = dbHelper.getUserIdByEmailAndPassword(email, ""); // si tu veux adapter cela à Google users
                    if (userId != -1) {
                        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                .edit()
                                .putInt("user_id", userId)
                                .apply();
                    }

                    Intent intent = new Intent(this, AgendaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                Log.e("GoogleSignIn", "Erreur lors de la connexion", e);
                Toast.makeText(this, "Erreur lors de la connexion : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}