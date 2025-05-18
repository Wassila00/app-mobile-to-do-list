package com.example.sidebar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASKS = "tache";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_HEURE = "heure"; // Heure de la tâche
    public static final String COLUMN_TASK_EST_TERMINEE = "est_terminee"; // Statut de la tâche
    public static final String COLUMN_TASK_DATE_CREATION = "date_creation"; // Date de création (LocalDateTime)
    public static final String COLUMN_TASK_DATE_MODIFICATION = "date_modification"; // Date de modification (LocalDateTime)
    public static final String COLUMN_TASK_DATE_TACHE = "date_tache"; // Date de la tâche (LocalDate)
    public static final String COLUMN_TASK_RAPPEL1 = "rappel_avant_debut"; // Rappel avant le début (Duration)
    public static final String COLUMN_TASK_RAPPEL2 = "rappel_retard";
    public static final String COLUMN_TASK_PRIORITE = "priorite";
    public static final String COLUMN_TASK_RECURRENCE = "recurrence";
    public static final String COLUMN_TASK_CATEGORIE = "categorie";

    // Table Utilisateur
    public static final String TABLE_USER = "utilisateur";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Table Sous-tâche
    public static final String TABLE_SOUS_TACHE = "sous_tache";
    public static final String COLUMN_SOUS_TACHE_ID = "id";
    public static final String COLUMN_SOUS_TACHE_TITRE = "titre";
    public static final String COLUMN_SOUS_TACHE_DESCRIPTION = "description";
    public static final String COLUMN_SOUS_TACHE_EST_TERMINEE = "est_terminee";

    public static final String COLUMN_SOUS_TACHE_Tache= "tache";

    // Table Priorité
    public static final String TABLE_PRIORITE = "priorite";
    public static final String COLUMN_PRIORITE_ID = "id";
    public static final String COLUMN_PRIORITE_TITRE = "titre";
    public static final String COLUMN_PRIORITE_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITE_EST_TERMINEE = "est_terminee";

    // Table Recurrence
    public static final String TABLE_RECURRENCE = "recurrence";
    public static final String COLUMN_RECURRENCE_ID = "id";
    public static final String COLUMN_RECURRENCE_NAME = "name";
    public static final String COLUMN_RECURRENCE_DESCRIPTION = "description";

    // Table Categorie
    public static final String TABLE_CATEGORIE = "categorie";
    public static final String COLUMN_CATEGORIE_ID = "id";
    public static final String COLUMN_CATEGORIE_NAME = "name";
    public static final String COLUMN_CATEGORIE_DESCRIPTION = "description";

    // Requête SQL pour créer la table "tasks"
    // Requête SQL pour créer la table "tasks"
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " ("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_TITLE + " TEXT NOT NULL, "
            + COLUMN_TASK_DESCRIPTION + " TEXT, "
            + COLUMN_TASK_HEURE + " TIME, "
            + COLUMN_TASK_EST_TERMINEE + " BOOLEAN, "
            + COLUMN_TASK_DATE_CREATION + " DATETIME, "
            + COLUMN_TASK_DATE_MODIFICATION + " DATETIME, "
            + COLUMN_TASK_DATE_TACHE + " DATE, "
            + COLUMN_TASK_RAPPEL1 + " INTEGER, "
            + COLUMN_TASK_RAPPEL2 + " INTEGER, "
            + COLUMN_TASK_PRIORITE + " INTEGER, "
            + COLUMN_TASK_RECURRENCE + " INTEGER, "
            + COLUMN_TASK_CATEGORIE + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_TASK_PRIORITE + ") REFERENCES " + TABLE_PRIORITE + "(" + COLUMN_PRIORITE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_RECURRENCE + ") REFERENCES " + TABLE_RECURRENCE + "(" + COLUMN_RECURRENCE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_CATEGORIE + ") REFERENCES " + TABLE_CATEGORIE + "(" + COLUMN_CATEGORIE_ID + ")"
            + ");";


    // Requête SQL pour créer la table "utilisateur"
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT NOT NULL, "
            + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL"
            + ");";

    // Requête SQL pour créer la table "sous_tache"
    private static final String CREATE_TABLE_SOUS_TACHE = "CREATE TABLE " + TABLE_SOUS_TACHE + " ("
            + COLUMN_SOUS_TACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SOUS_TACHE_TITRE + " TEXT NOT NULL, "
            + COLUMN_SOUS_TACHE_DESCRIPTION + " TEXT, "
            + COLUMN_SOUS_TACHE_EST_TERMINEE + " BOOLEAN, "
            + COLUMN_SOUS_TACHE_Tache + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_SOUS_TACHE_Tache + ") REFERENCES " + TABLE_TASKS + "(" + COLUMN_TASK_ID + ")"
            + ");";

    // Requête SQL pour créer la table "priorite"
    private static final String CREATE_TABLE_PRIORITE = "CREATE TABLE " + TABLE_PRIORITE + " ("
            + COLUMN_PRIORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PRIORITE_TITRE + " TEXT NOT NULL, "
            + COLUMN_PRIORITE_DESCRIPTION + " TEXT, "
            + COLUMN_PRIORITE_EST_TERMINEE + " BOOLEAN"
            + ");";

    // Requête SQL pour créer la table "recurrence"
    private static final String CREATE_TABLE_RECURRENCE = "CREATE TABLE " + TABLE_RECURRENCE + " ("
            + COLUMN_RECURRENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RECURRENCE_NAME + " TEXT NOT NULL, "
            + COLUMN_RECURRENCE_DESCRIPTION + " TEXT"
            + ");";

    // Requête SQL pour créer la table "categorie"
    private static final String CREATE_TABLE_CATEGORIE = "CREATE TABLE " + TABLE_CATEGORIE + " ("
            + COLUMN_CATEGORIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORIE_NAME + " TEXT NOT NULL, "
            + COLUMN_CATEGORIE_DESCRIPTION + " TEXT"
            + ");";

    public DatabaseHelper(Context context) {
        super(context,"to_do_list.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer toutes les tables
        db.execSQL(CREATE_TABLE_TASKS);       // Créer la table "tasks"
        db.execSQL(CREATE_TABLE_USER);        // Créer la table "utilisateur"
        db.execSQL(CREATE_TABLE_SOUS_TACHE);  // Créer la table "sous_tache"
        db.execSQL(CREATE_TABLE_PRIORITE);    // Créer la table "priorite"
        db.execSQL(CREATE_TABLE_RECURRENCE);  // Créer la table "recurrence"
        db.execSQL(CREATE_TABLE_CATEGORIE);   // Créer la table "categorie"
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la base de données existe déjà, on la supprime et on la recrée
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUS_TACHE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRIORITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURRENCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE);
        onCreate(db);
    }
    // Méthode pour ajouter un utilisateur
    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        // Si l'insertion échoue, la méthode insert() retourne -1
        return result != -1;
    }
    // Vérifier si l'email est déjà enregistré
    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Vérifier si les identifiants sont corrects
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

}
