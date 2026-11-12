package com.example.muslimummah.models;

public class MakruhStatus {
    private final boolean isMakruh;
    private final String reason;
    private final int remainingMinutes;

    public MakruhStatus(boolean isMakruh, String reason, int remainingMinutes) {
        this.isMakruh = isMakruh;
        this.reason = reason;
        this.remainingMinutes = remainingMinutes;
    }

    public boolean isMakruh() { return isMakruh; }
    public String getReason() { return reason; }
    public int getRemainingMinutes() { return remainingMinutes; }
}
