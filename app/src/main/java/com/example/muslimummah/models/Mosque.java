package com.example.muslimummah.models;

import java.util.Map;

public class Mosque {
    private final String name;
    private final String address;
    private final double lat;
    private final double lon;
    private final double distanceKm;
    private final String photoUrl;
    private final Map<String, String> zamatSchedule;

    public Mosque(String name, String address, double lat, double lon, double distanceKm, String photoUrl, Map<String, String> zamatSchedule) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.distanceKm = distanceKm;
        this.photoUrl = photoUrl;
        this.zamatSchedule = zamatSchedule;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public double getDistanceKm() { return distanceKm; }
    public String getPhotoUrl() { return photoUrl; }
    public Map<String, String> getZamatSchedule() { return zamatSchedule; }
}
