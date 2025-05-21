// EventAdapter.java
package com.example.sidebar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        public interface OnEventClickListener {
                void onEventClick(Event event);
        }

        private List<Event> eventList;
        private OnEventClickListener listener;

        public EventAdapter(List<Event> eventList, OnEventClickListener listener) {
                this.eventList = eventList;
                this.listener = listener;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_event, parent, false);
                return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
                Event event = eventList.get(position);
                String eventTitle = event.getSummary();
                String eventDate = (event.getStart().getDate() != null)
                        ? event.getStart().getDate().toString()
                        : event.getStart().getDateTime().toStringRfc3339().replace("T", " ").replace("Z", "");

                holder.textViewTitle.setText(eventTitle);
                holder.textViewDate.setText(eventDate);

                holder.itemView.setOnClickListener(v -> {
                        if (listener != null) {
                                listener.onEventClick(event);
                        }
                });
        }

        @Override
        public int getItemCount() {
                return eventList.size();
        }

        public void setEvents(List<Event> newEvents) {
                this.eventList = newEvents;
                notifyDataSetChanged();
        }

        static class EventViewHolder extends RecyclerView.ViewHolder {
                TextView textViewTitle;
                TextView textViewDate;

                public EventViewHolder(@NonNull View itemView) {
                        super(itemView);
                        textViewTitle = itemView.findViewById(R.id.textViewTitle);
                        textViewDate = itemView.findViewById(R.id.textViewDate);
                }
        }
}
