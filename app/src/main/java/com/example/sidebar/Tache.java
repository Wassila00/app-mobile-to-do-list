package com.example.sidebar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tache {

    private int id;
    private String titre;
    private String description;
    private String date;  // format : yyyy-MM-dd
    private String heureDebut;  // format : HH:mm
    private String heureFinPrevue;  // format : HH:mm
    private String heureFinReelle;  // format : HH:mm
    private boolean estTerminee;
    private long retardMinutes = 0;
    private int priorite; // 1 = haute, 2 = moyenne, 3 = basse

// en minutes


    // Constructeur principal
    public Tache(int id, String titre, String description, String date, String heureDebut, String heureFinPrevue, String heureFinReelle, boolean estTerminee, int priorite) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFinPrevue = heureFinPrevue;
        this.heureFinReelle = heureFinReelle;
        this.estTerminee = estTerminee;
        this.priorite = priorite;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return sdf.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateString() {
        return date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public String getHeureFinPrevue() {
        return heureFinPrevue;
    }

    public String getHeureFinReelle() {
        return heureFinReelle;
    }

    public boolean isEstTerminee() {
        return estTerminee;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFinPrevue(String heureFinPrevue) {
        this.heureFinPrevue = heureFinPrevue;
    }

    public void setHeureFinReelle(String heureFinReelle) {
        this.heureFinReelle = heureFinReelle;
    }

    public void setEstTerminee(boolean estTerminee) {
        this.estTerminee = estTerminee;
    }

    @Override
    public String toString() {
        return titre;
    }


    public long getRetardMinutes() {
        return retardMinutes;
    }

    public void setRetardMinutes(long retardMinutes) {
        this.retardMinutes = retardMinutes;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }


}
