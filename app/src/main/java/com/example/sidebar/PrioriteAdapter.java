package com.example.sidebar;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrioriteAdapter extends BaseAdapter {

    private Context context;
    private List<Tache> originalTaches;
    private List<Object> displayItems;
    private LayoutInflater inflater;
    private DatabaseHelper dbHelper;

    public PrioriteAdapter(Context context, List<Tache> taches) {
        this.context = context;
        this.originalTaches = new ArrayList<>(taches);
        this.displayItems = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.dbHelper = new DatabaseHelper(context);
        buildDisplayItems(originalTaches);
    }

    private void buildDisplayItems(List<Tache> taches) {
        displayItems.clear();
        Map<String, List<Tache>> groupedByDate = new LinkedHashMap<>();

        for (Tache tache : taches) {
            String date = tache.getDateString();
            if (!groupedByDate.containsKey(date)) {
                groupedByDate.put(date, new ArrayList<>());
            }
            groupedByDate.get(date).add(tache);
        }

        for (Map.Entry<String, List<Tache>> entry : groupedByDate.entrySet()) {
            displayItems.add(entry.getKey());
            List<Tache> group = entry.getValue();
            group.sort((a, b) -> Integer.compare(a.getPriorite(), b.getPriorite()));
            displayItems.addAll(group);
        }
    }

    @Override
    public int getCount() {
        return displayItems.size();
    }

    @Override
    public Object getItem(int position) {
        return displayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return (displayItems.get(position) instanceof String) ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Header ou Tâche
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        if (type == 0) { // Header (date)
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_header, parent, false);
            }
            TextView headerText = convertView.findViewById(R.id.headerText);
            headerText.setText((String) displayItems.get(position));
        } else { // Item Tâche
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_tache, parent, false);
                holder = new ViewHolder();
                holder.titre = convertView.findViewById(R.id.titreTache);
                holder.heure = convertView.findViewById(R.id.heureTache);
                holder.checkBox = convertView.findViewById(R.id.checkboxTache);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Tache tache = (Tache) displayItems.get(position);

            holder.titre.setText(tache.getTitre());
            holder.heure.setText(tache.getHeureDebut() + " → " + tache.getHeureFinPrevue());

            holder.checkBox.setOnCheckedChangeListener(null);
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

    public void filter(String query) {
        List<Tache> filtered = new ArrayList<>();
        for (Tache t : originalTaches) {
            if (t.getTitre().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(t);
            }
        }
        buildDisplayItems(filtered);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView titre;
        TextView heure;
    }
}
