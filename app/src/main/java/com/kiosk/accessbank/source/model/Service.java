package com.kiosk.accessbank.source.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

public class Service {
    private int icon;

    public Service(int id, String name, @DrawableRes int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }
}
