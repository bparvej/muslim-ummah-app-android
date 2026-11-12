package com.example.muslimummah.models;

public class MasnoonDua {
    private final String id;
    private final String title;
    private final String arabic;
    private final String pronunciation;
    private final String translation;
    private final String reference;

    public MasnoonDua(String id, String title, String arabic, String pronunciation, String translation, String reference) {
        this.id = id;
        this.title = title;
        this.arabic = arabic;
        this.pronunciation = pronunciation;
        this.translation = translation;
        this.reference = reference;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArabic() { return arabic; }
    public String getPronunciation() { return pronunciation; }
    public String getTranslation() { return translation; }
    public String getReference() { return reference; }
}
