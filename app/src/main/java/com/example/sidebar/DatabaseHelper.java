package com.example.sidebar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public static final String COLUMN_TASK_USER_ID = "user_id";

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
            + COLUMN_TASK_USER_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_TASK_PRIORITE + ") REFERENCES " + TABLE_PRIORITE + "(" + COLUMN_PRIORITE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_RECURRENCE + ") REFERENCES " + TABLE_RECURRENCE + "(" + COLUMN_RECURRENCE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_CATEGORIE + ") REFERENCES " + TABLE_CATEGORIE + "(" + COLUMN_CATEGORIE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TASK_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT NOT NULL, "
            + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_PRIORITE = "CREATE TABLE " + TABLE_PRIORITE + " ("
            + COLUMN_PRIORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PRIORITE_TITRE + " TEXT NOT NULL, "
            + COLUMN_PRIORITE_DESCRIPTION + " TEXT);";

    private static final String CREATE_TABLE_RECURRENCE = "CREATE TABLE " + TABLE_RECURRENCE + " ("
            + COLUMN_RECURRENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RECURRENCE_NAME + " TEXT NOT NULL, "
            + COLUMN_RECURRENCE_DESCRIPTION + " TEXT);";

    private static final String CREATE_TABLE_CATEGORIE = "CREATE TABLE " + TABLE_CATEGORIE + " ("
            + COLUMN_CATEGORIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORIE_NAME + " TEXT NOT NULL, "
            + COLUMN_CATEGORIE_DESCRIPTION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, "to_do_list.db", null, 2);
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

    // Authentification
    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getUserIdByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USER +
                " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?", new String[]{email, password});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }

    // Insertion depuis Google Calendar
    public void insererTacheDepuisGoogleCalendar(Event event, int userId) {
        if (event.getSummary() == null || event.getStart() == null) return;

        String titre = event.getSummary();
        String description = event.getDescription() != null ? event.getDescription() : "";
        String dateTache = "", heureDebut = "", heureFinPrevue = "";

        if (event.getStart().getDateTime() != null) {
            String[] parts = event.getStart().getDateTime().toStringRfc3339().split("T");
            dateTache = parts[0];
            heureDebut = parts[1].replace("Z", "");
        } else if (event.getStart().getDate() != null) {
            dateTache = event.getStart().getDate().toString();
        }

        if (event.getEnd() != null && event.getEnd().getDateTime() != null) {
            heureFinPrevue = event.getEnd().getDateTime().toStringRfc3339().split("T")[1].replace("Z", "");
        }

        if (tacheExiste(titre, dateTache, userId)) return;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, titre);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_DATE_TACHE, dateTache);
        values.put(COLUMN_TASK_heure_debut, heureDebut);
        values.put(COLUMN_TASK_heure_fin_prevue, heureFinPrevue);
        values.put(COLUMN_TASK_EST_TERMINEE, 0);
        values.put(COLUMN_TASK_DATE_CREATION, getDateTime());
        values.put(COLUMN_TASK_DATE_MODIFICATION, getDateTime());
        values.put(COLUMN_TASK_USER_ID, userId);
        db.insert(TABLE_TASKS, null, values);
    }

    public boolean tacheExiste(String titre, String dateTache, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " +
                COLUMN_TASK_TITLE + " = ? AND " +
                COLUMN_TASK_DATE_TACHE + " = ? AND " +
                COLUMN_TASK_USER_ID + " = ?", new String[]{titre, dateTache, String.valueOf(userId)});
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        return exists;
    }

    // Insertion manuelle
    public void insertManualTask(String titre, String description, String dateTache, String heureDebut,
                                 String heureFinPrevue, String rappel1, String rappel2,
                                 String dateCreation, String dateModification,
                                 int estTerminee, int userId, int categorieId, int prioriteId) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, titre);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_DATE_TACHE, dateTache);
        values.put(COLUMN_TASK_heure_debut, heureDebut);
        values.put(COLUMN_TASK_heure_fin_prevue, heureFinPrevue);
        values.put(COLUMN_TASK_RAPPEL1, rappel1);
        values.put(COLUMN_TASK_RAPPEL2, rappel2);
        values.put(COLUMN_TASK_DATE_CREATION, dateCreation);
        values.put(COLUMN_TASK_DATE_MODIFICATION, dateModification);
        values.put(COLUMN_TASK_EST_TERMINEE, estTerminee);
        values.put(COLUMN_TASK_USER_ID, userId);
        values.put(COLUMN_TASK_CATEGORIE, categorieId);
        values.put(COLUMN_TASK_PRIORITE, prioriteId);

        db.insert(TABLE_TASKS, null, values);
    }

    // Initialisation des catégories et priorités
    public void insererCategoriesSiVide() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categorie", null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            insertCategorie(db, "Work", "Tasks related to work");
            insertCategorie(db, "Personal", "Personal errands and reminders");
            insertCategorie(db, "Study", "Tasks for school or university");
        }
        cursor.close();
    }

    private void insertCategorie(SQLiteDatabase db, String nom, String description) {
        ContentValues values = new ContentValues();
        values.put("name", nom);
        values.put("description", description);
        db.insert("categorie", null, values);
    }

    public void insererPrioritesSiVide() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM priorite", null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            insertPriorite(db, "Low", "Not urgent");
            insertPriorite(db, "Medium", "Important but not urgent");
            insertPriorite(db, "High", "Urgent task");
        }
        cursor.close();
    }

    private void insertPriorite(SQLiteDatabase db, String titre, String description) {
        ContentValues values = new ContentValues();
        values.put("titre", titre);
        values.put("description", description);
        db.insert("priorite", null, values);
    }

    public int getCategorieIdByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM categorie WHERE name = ?", new String[]{name});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public int getPrioriteIdByTitre(String titre) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM priorite WHERE titre = ?", new String[]{titre});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public List<String> getCategorieNames() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM categorie", null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }

    public List<String> getPrioriteTitres() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT titre FROM priorite", null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }
    public void updateTache(int taskId, String titre, String description, String heureDebut,
                            String heureFinPrevue, String rappel1, String rappel2,
                            String dateModification, int categorieId, int prioriteId, int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK_TITLE, titre);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_heure_debut, heureDebut);
        values.put(COLUMN_TASK_heure_fin_prevue, heureFinPrevue);
        values.put(COLUMN_TASK_RAPPEL1, rappel1);
        values.put(COLUMN_TASK_RAPPEL2, rappel2);
        values.put(COLUMN_TASK_DATE_MODIFICATION, dateModification);
        values.put(COLUMN_TASK_CATEGORIE, categorieId);
        values.put(COLUMN_TASK_PRIORITE, prioriteId);
        values.put(COLUMN_TASK_USER_ID, userId);

        int rows = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        if (rows == 0) {
            Log.e("DB", "❌ Mise à jour échouée pour la tâche ID: " + taskId);
        } else {
            Log.d("DB", "✅ Tâche mise à jour avec succès : ID " + taskId);
        }
    }

    public void deleteTache(int tacheId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(tacheId)});
    }



    public Tache getTacheById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tache WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            return new Tache(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date_tache")),
                    cursor.getString(cursor.getColumnIndexOrThrow("heure_debut")),
                    cursor.getString(cursor.getColumnIndexOrThrow("heure_fin_prevue")),
                    cursor.getString(cursor.getColumnIndexOrThrow("heure_fin_reelle")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("est_terminee")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("priorite")) // ✅ ajout du 9e paramètre
            );
        }

        cursor.close();
        return null;
    }



    @SuppressLint("Range")
    public List<Event> getTachesAsGoogleEventsForDate(String date, int userId) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tache WHERE date_tache = ? AND user_id = ?", new String[]{date, String.valueOf(userId)});
        while (cursor.moveToNext()) {
            Event event = new Event();
            event.setSummary(cursor.getString(cursor.getColumnIndex("title")));

            EventDateTime start = new EventDateTime();
            start.setDate(new DateTime(date));
            event.setStart(start);

            EventDateTime end = new EventDateTime();
            end.setDate(new DateTime(date));
            event.setEnd(end);

            events.add(event);
        }

        cursor.close();
        return events;
    }

    public List<Tache> getTachesByDateAndUser(String date, int userId) {
        List<Tache> taches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tache WHERE date_tache = ? AND user_id = ?",
                new String[]{date, String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                Tache t = new Tache(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date_tache")),
                        cursor.getString(cursor.getColumnIndexOrThrow("heure_debut")),
                        cursor.getString(cursor.getColumnIndexOrThrow("heure_fin_prevue")),
                        cursor.getString(cursor.getColumnIndexOrThrow("heure_fin_reelle")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("est_terminee")) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow("priorite")) // ✅ ajout ici
                );
                taches.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return taches;
    }

    public Tache getTacheByTitreAndDate(String titre, String date, int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tache WHERE title = ? AND date_tache = ? AND user_id = ?",
                new String[]{titre, date, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            return new Tache(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date_tache")),
                    cursor.getString(cursor.getColumnIndexOrThrow("heure_debut")),
                    cursor.getString(cursor.getColumnIndexOrThrow("heure_fin_prevue")),
                    cursor.getString(cursor.getColumnIndexOrThrow("heure_fin_reelle")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("est_terminee")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("priorite")) // ✅ ajout ici

            );
        }

        return null;
    }

    public List<Tache> getTachesAVenir() {
        List<Tache> listeTaches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

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
            } while (cursor.moveToNext());
            cursor.close();
        }

        return taches;
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

    public void mettreAJourEtatTache(int tacheId, boolean estTerminee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_EST_TERMINEE, estTerminee ? 1 : 0);
        values.put(COLUMN_TASK_DATE_MODIFICATION, getDateTime());

        db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(tacheId)});
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
}


