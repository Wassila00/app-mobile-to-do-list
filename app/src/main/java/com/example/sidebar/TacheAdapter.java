package com.example.sidebar;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TacheAdapter extends ArrayAdapter<Tache> {

    private final List<Tache> taches;     // Liste affichée (filtrée)
    private final List<Tache> allTaches;  // Liste complète
    private final DatabaseHelper dbHelper;

    public TacheAdapter(Context context, List<Tache> taches) {
        super(context, 0, taches);
        this.taches = new ArrayList<>(taches);
        this.allTaches = new ArrayList<>(taches);
        this.dbHelper = new DatabaseHelper(context); // <- correction ici
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView titre;
        TextView heure;
    }

    @Override
    public int getCount() {
        return taches.size();
    }

    @Override
    public Tache getItem(int position) {
        return taches.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tache tache = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tache, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.checkboxTache);
            holder.titre = convertView.findViewById(R.id.titreTache);
            holder.heure = convertView.findViewById(R.id.heureTache);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (tache != null) {
            holder.titre.setText(tache.getTitre());
            holder.heure.setText(tache.getHeureDebut() + " - " + tache.getHeureFinPrevue());


            holder.checkBox.setOnCheckedChangeListener(null); // important
            holder.checkBox.setChecked(tache.isEstTerminee());

            updateStrikeThrough(holder.titre, tache.isEstTerminee());

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tache.setEstTerminee(isChecked);
                updateStrikeThrough(holder.titre, isChecked);
                dbHelper.mettreAJourEtatTache(tache.getId(), isChecked);
            });
        }

        return convertView;
    }

    private void updateStrikeThrough(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }


}
