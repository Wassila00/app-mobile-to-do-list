// GoogleSignInHelper.java
package com.example.sidebar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignInHelper {
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1001;
    private Context context;

    public GoogleSignInHelper(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                        new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/calendar"),
                        new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/calendar.events")
                )
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signIn(Activity activity) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Intent data, Activity activity) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                Log.d("GoogleSignIn", "Connected: " + account.getEmail());

                // Redirect to AgendaActivity
                Intent intent = new Intent(activity, AgendaActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        } catch (ApiException e) {
            Log.e("GoogleSignIn", "Error during sign-in", e);
            Toast.makeText(activity, "Error during sign-in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }
    public String getSignedInEmail(Context context) {
        return context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("user_email", null);
    }
}
