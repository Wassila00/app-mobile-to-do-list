package com.example.sidebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LateTaskAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TASK = 1;

    private List<Object> items;
    private LayoutInflater inflater;

    public LateTaskAdapter(Context context, List<Object> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
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
        return (items.get(position) instanceof String) ? TYPE_HEADER : TYPE_TASK;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER) {
            HeaderViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_header, parent, false);
                holder = new HeaderViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            String date = (String) getItem(position);
            holder.headerText.setText(date);
            return convertView;

        } else {
            TaskViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_late_unfinished, parent, false);
                holder = new TaskViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (TaskViewHolder) convertView.getTag();
            }

            Tache tache = (Tache) getItem(position);
            holder.titreTache.setText(tache.getTitre());

            if (tache.isEstTerminee()) {
                long retard = tache.getRetardMinutes();
                if (retard > 0) {
                    holder.statutTache.setText("Terminée avec un retard de " + retard + " minutes");
                } else {
                    holder.statutTache.setText("Terminée");
                }
            } else {
                holder.statutTache.setText("Non terminée");
            }

            return convertView;
        }
    }

    private static class HeaderViewHolder {
        TextView headerText;

        HeaderViewHolder(View view) {
            headerText = view.findViewById(R.id.headerText);
        }
    }

    private static class TaskViewHolder {
        TextView titreTache;
        TextView statutTache;

        TaskViewHolder(View view) {
            titreTache = view.findViewById(R.id.titreTache);
            statutTache = view.findViewById(R.id.statuttache);
        }
    }
}
