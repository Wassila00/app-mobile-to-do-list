package com.example.sidebar;
import com.example.sidebar.Tache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;
import com.example.sidebar.Tache;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASKS = "tache";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESCRIPTION = "description";

    public static final String COLUMN_TASK_EST_TERMINEE = "est_terminee";
    public static final String COLUMN_TASK_DATE_CREATION = "date_creation";
    public static final String COLUMN_TASK_DATE_MODIFICATION = "date_modification";
    public static final String COLUMN_TASK_DATE_TACHE = "date_tache";
    public static final String COLUMN_TASK_RAPPEL1 = "rappel_avant_debut";
    public static final String COLUMN_TASK_RAPPEL2 = "rappel_retard";
    public static final String COLUMN_TASK_heure_debut = "heure_debut";

    public static final String COLUMN_TASK_heure_fin_prevue = "heure_fin_prevue";
    public static final String COLUMN_TASK_heure_fin_reelle = "heure_fin_reelle";

    public static final String COLUMN_TASK_PRIORITE = "priorite";
    public static final String COLUMN_TASK_RECURRENCE = "recurrence";
    public static final String COLUMN_TASK_CATEGORIE = "categorie";

    public static final String COLUMN_TASK_USER_ID ="user_id";



    public static final String TABLE_USER = "utilisateur";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";



    public static final String TABLE_PRIORITE = "priorite";
    public static final String COLUMN_PRIORITE_ID = "id";
    public static final String COLUMN_PRIORITE_TITRE = "titre";
    public static final String COLUMN_PRIORITE_DESCRIPTION = "description";

    public static final String TABLE_RECURRENCE = "recurrence";
    public static final String COLUMN_RECURRENCE_ID = "id";
    public static final String COLUMN_RECURRENCE_NAME = "name";
    public static final String COLUMN_RECURRENCE_DESCRIPTION = "description";

    public static final String TABLE_CATEGORIE = "categorie";
    public static final String COLUMN_CATEGORIE_ID = "id";
    public static final String COLUMN_CATEGORIE_NAME = "name";
    public static final String COLUMN_CATEGORIE_DESCRIPTION = "description";


    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " ("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_TITLE + " TEXT NOT NULL, "
            + COLUMN_TASK_DESCRIPTION + " TEXT, "
            + COLUMN_TASK_EST_TERMINEE + " BOOLEAN, "
            + COLUMN_TASK_DATE_CREATION + " DATETIME, "
            + COLUMN_TASK_DATE_MODIFICATION + " DATETIME, "
            + COLUMN_TASK_DATE_TACHE + " DATE, "
            + COLUMN_TASK_RAPPEL1 + " INTEGER, "
            + COLUMN_TASK_RAPPEL2 + " INTEGER, "
            + COLUMN_TASK_heure_debut + " TIME, "
            + COLUMN_TASK_heure_fin_prevue + " TIME, "
            + COLUMN_TASK_heure_fin_reelle + " TIME, "
            + COLUMN_TASK_PRIORITE + " INTEGER, "
            + COLUMN_TASK_RECURRENCE + " INTEGER, "
            + COLUMN_TASK_CATEGORIE + " INTEGER, "
            + COLUMN_TASK_USER_ID + " INTEGER, " // ✅ colonne manquante ajoutée ici
            + "FOREIGN KEY(" + COLUMN_TASK_PRIORITE + ") REFERENCES " + TABLE_PRIORITE + "(" + COLUMN_PRIORITE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_RECURRENCE + ") REFERENCES " + TABLE_RECURRENCE + "(" + COLUMN_RECURRENCE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_CATEGORIE + ") REFERENCES " + TABLE_CATEGORIE + "(" + COLUMN_CATEGORIE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";


    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT NOT NULL, "
            + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL"
            + ");";


    private static final String CREATE_TABLE_PRIORITE = "CREATE TABLE " + TABLE_PRIORITE + " ("
            + COLUMN_PRIORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PRIORITE_TITRE + " TEXT NOT NULL, "
            + COLUMN_PRIORITE_DESCRIPTION + " TEXT"
            + ");"; // ✅ Supprimé la virgule après COLUMN_PRIORITE_DESCRIPTION

    private static final String CREATE_TABLE_RECURRENCE = "CREATE TABLE " + TABLE_RECURRENCE + " ("
            + COLUMN_RECURRENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RECURRENCE_NAME + " TEXT NOT NULL, "
            + COLUMN_RECURRENCE_DESCRIPTION + " TEXT"
            + ");";

    private static final String CREATE_TABLE_CATEGORIE = "CREATE TABLE " + TABLE_CATEGORIE + " ("
            + COLUMN_CATEGORIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORIE_NAME + " TEXT NOT NULL, "
            + COLUMN_CATEGORIE_DESCRIPTION + " TEXT"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, "to_do_list.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PRIORITE);
        db.execSQL(CREATE_TABLE_RECURRENCE);
        db.execSQL(CREATE_TABLE_CATEGORIE);
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRIORITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURRENCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE);
        onCreate(db);
    }



    private String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public List<Tache> getTachesDuJour() {
        List<Tache> taches = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String dateAujourdhui = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String query = "SELECT * FROM " + TABLE_TASKS +
                " WHERE " + COLUMN_TASK_DATE_TACHE + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{dateAujourdhui})) {
            while (cursor.moveToNext()) {
                Tache tache = new Tache(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE_TACHE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_debut)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_prevue)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_reelle)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_EST_TERMINEE)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITE))
                );

                taches.add(tache);
            }
        }
        return taches;
    }

    public void mettreAJourEtatTache(int tacheId, boolean estTerminee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_EST_TERMINEE, estTerminee ? 1 : 0);
        values.put(COLUMN_TASK_DATE_MODIFICATION, getDateTime());

        db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(tacheId)});
    }


    public List<Tache> getTachesForCurrentWeek() {
        List<Tache> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Début et fin de semaine
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();

        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = sdf.format(startOfWeek);
        String endDate = sdf.format(endOfWeek);

        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_DATE_TACHE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                Tache tache = new Tache(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE_TACHE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_debut)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_prevue)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_reelle)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_EST_TERMINEE)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITE))
                );

                list.add(tache);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public List<Tache> getAllTaches() {
        List<Tache> taches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS;

        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                Tache tache = new Tache(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE_TACHE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_debut)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_prevue)),  // heureFinPrevue
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_reelle)),   // heureFinReelle
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_EST_TERMINEE)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITE))
                );
                taches.add(tache);
            }
        }

        return taches;
    }

    //authentification

    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        // Si l'insertion échoue, la méthode insert() retourne -1
        return result!=-1;
}


    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
}

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
}

    public List<Tache> getTachesAVenir() {
        List<Tache> listeTaches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Correction du nom de la table et des colonnes
        String query = "SELECT * FROM " + TABLE_TASKS +
                " WHERE " + COLUMN_TASK_DATE_TACHE + " > ?" +
                " ORDER BY " + COLUMN_TASK_DATE_TACHE + " ASC, " + COLUMN_TASK_heure_debut + " ASC";

        try (Cursor cursor = db.rawQuery(query, new String[]{today})) {
            while (cursor.moveToNext()) {
                Tache tache = new Tache(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE_TACHE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_debut)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_prevue)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_reelle)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_EST_TERMINEE)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITE))
                );
                listeTaches.add(tache);
            }
        }

        return listeTaches;
    }

    public List<Tache> getAllTachesTrieesParDateEtPriorite() {
        List<Tache> taches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy = COLUMN_TASK_DATE_TACHE + " ASC, " + COLUMN_TASK_PRIORITE + " ASC";

        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID));
                String titre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE_TACHE));
                String heureDebut = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_debut));
                String heureFinPrevue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_prevue));
                String heureFinReelle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_heure_fin_reelle));
                boolean estTerminee = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_EST_TERMINEE)) == 1;
               int priorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_PRIORITE));

                Tache tache = new Tache(id, titre, description, date, heureDebut, heureFinPrevue, heureFinReelle, estTerminee, priorite);
                taches.add(tache);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return taches;
    }











}
