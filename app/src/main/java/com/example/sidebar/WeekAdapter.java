package com.example.sidebar;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class WeekAdapter extends BaseAdapter {

    private Context context;
    private List<ListItem> items;
    private LayoutInflater inflater;
    private DatabaseHelper dbHelper;

    public WeekAdapter(Context context, List<ListItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.dbHelper = new DatabaseHelper(context); // initialise le dbHelper
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2; // HEADER + TASK
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = items.get(position);

        if (item.getType() == ListItem.TYPE_HEADER) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_header, parent, false);
            }
            TextView headerText = convertView.findViewById(R.id.headerText);
            HeaderItem header = (HeaderItem) item;
            headerText.setText(header.getDate());

        } else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_tache, parent, false);
            }

            TacheItem taskItem = (TacheItem) item;
            Tache tache = taskItem.getTache();

            TextView titre = convertView.findViewById(R.id.titreTache);
            TextView heure = convertView.findViewById(R.id.heureTache);
            CheckBox checkbox = convertView.findViewById(R.id.checkboxTache);

            titre.setText(tache.getTitre());
            heure.setText(tache.getHeureDebut());

            // Supprimer l'ancien listener
            checkbox.setOnCheckedChangeListener(null);
            checkbox.setChecked(tache.isEstTerminee());
            updateStrikeThrough(titre, tache.isEstTerminee());

            // Nouveau listener
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tache.setEstTerminee(isChecked); // met à jour dans l'objet
                updateStrikeThrough(titre, isChecked); // met à jour l'affichage
                dbHelper.mettreAJourEtatTache(tache.getId(), isChecked); // met à jour la base
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
