package com.example.muslimummah.models;

public class PrayerTimings {
    private final String fajr;
    private final String sunrise;
    private final String dhuhr;
    private final String asr;
    private final String maghrib;
    private final String isha;
    private final String imsak;

    public PrayerTimings(String fajr, String sunrise, String dhuhr, String asr, String maghrib, String isha, String imsak) {
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
        this.imsak = imsak;
    }

    public String getFajr() { return fajr; }
    public String getSunrise() { return sunrise; }
    public String getDhuhr() { return dhuhr; }
    public String getAsr() { return asr; }
    public String getMaghrib() { return maghrib; }
    public String getIsha() { return isha; }
    public String getImsak() { return imsak; }
}
