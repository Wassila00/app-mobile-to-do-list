package com.example.sidebar;

public class HeaderItem extends ListItem {
    private String date;

    public HeaderItem(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
