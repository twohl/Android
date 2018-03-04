package com.script.muhelp.entity;

import java.io.Serializable;

public class Position implements Serializable{
    private static final long serialVersionUID = 1;
    private double lng;
    private double lat;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
