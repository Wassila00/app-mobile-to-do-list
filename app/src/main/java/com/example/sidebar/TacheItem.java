package com.example.sidebar;

public class TacheItem extends ListItem {
    private Tache tache;

    public TacheItem(Tache tache) {
        this.tache = tache;
    }

    public Tache getTache() {
        return tache;
    }

    @Override
    public int getType() {
        return TYPE_TASK;
    }
}
