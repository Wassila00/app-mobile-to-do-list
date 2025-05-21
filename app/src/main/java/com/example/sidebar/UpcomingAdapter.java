package com.example.sidebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.CheckBox;
import android.graphics.Paint;
import java.util.List;

public class UpcomingAdapter extends BaseAdapter {
    private Context context;
    private List<ListItem> items;

    public UpcomingAdapter(Context context, List<ListItem> items) {
        this.context = context;
        this.items = items;
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
            HeaderItem headerItem = (HeaderItem) item;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            }
            TextView headerText = convertView.findViewById(R.id.headerText);
            headerText.setText(headerItem.getDate());
        } else {
            TacheItem tacheItem = (TacheItem) item;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_tache, parent, false);
            }

            TextView title = convertView.findViewById(R.id.titreTache);
            TextView heure = convertView.findViewById(R.id.heureTache);
            CheckBox checkBox = convertView.findViewById(R.id.checkboxTache);

            Tache tache = tacheItem.getTache();
            title.setText(tache.getTitre());
            heure.setText(tache.getHeureDebut() + " - " + tache.getHeureFinPrevue());

            checkBox.setOnCheckedChangeListener(null); // IMPORTANT: éviter les appels redondants
            checkBox.setChecked(tache.isEstTerminee());
            updateStrikeThrough(title, tache.isEstTerminee());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tache.setEstTerminee(isChecked);
                updateStrikeThrough(title, isChecked);
                // Mise à jour base de données
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.mettreAJourEtatTache(tache.getId(), isChecked);
            });
        }

        return convertView;
    }

    private void updateStrikeThrough(TextView textView, boolean isStriked) {
        if (isStriked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }


}
