package com.example.muslimummah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muslimummah.R;
import com.example.muslimummah.models.MasnoonDua;

import java.util.ArrayList;
import java.util.List;

public class DuaLibraryActivity extends AppCompatActivity {

    private EditText etSearch;
    private RecyclerView rvDuas;
    private DuaAdapter duaAdapter;
    private List<MasnoonDua> allDuas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua_library);

        etSearch = findViewById(R.id.et_search_dua);
        rvDuas = findViewById(R.id.rv_duas);

        populateDuas();

        rvDuas.setLayoutManager(new LinearLayoutManager(this));
        duaAdapter = new DuaAdapter(allDuas, dua -> {
            Intent intent = new Intent(this, DuaDetailActivity.class);
            intent.putExtra("dua_title", dua.getTitle());
            intent.putExtra("dua_arabic", dua.getArabic());
            intent.putExtra("dua_pronunciation", dua.getPronunciation());
            intent.putExtra("dua_translation", dua.getTranslation());
            intent.putExtra("dua_ref", dua.getReference());
            startActivity(intent);
        });
        rvDuas.setAdapter(duaAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDuas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void populateDuas() {
        allDuas.add(new MasnoonDua(
                "sleep",
                "ঘুমানোর পূর্বের দোয়া",
                "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                "বিসমিকাল্লা-হুম্মা আমূতু ওয়া আহ্ইয়া",
                "হে আল্লাহ! আপনারই নামে আমি মৃত্যুবরণ (ঘুমাই) করি এবং আপনারই নামে জীবিত (জাগ্রত) হই।",
                "সহিহ বুখারি ৬৩১২"
        ));
        allDuas.add(new MasnoonDua(
                "wake",
                "ঘুম থেকে ওঠার দোয়া",
                "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                "আলহামদু লিল্লা-হিল্লাযী আহ্ইয়ানা বা'দা মা আমাতানা ওয়া ইলাইহিন নুশূর",
                "সকল প্রশংসা আল্লাহর জন্য, যিনি আমাদেরকে মৃত্যু দেওয়ার পর পুনরায় জীবিত করলেন, আর তাঁর দিকেই সকলের পুনরুত্থান।",
                "সহিহ বুখারি ৬৩১২"
        ));
        allDuas.add(new MasnoonDua(
                "travel",
                "সফর বা যানবাহনে ওঠার দোয়া",
                "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ",
                "সুবহা-নাল্লাযী সাখ্খারা লানা হা-যা ওয়ামা কুন্না লাহু মুক্বরিনীনা, ওয়া ইন্না ইলা রব্বিনা লামুনক্বালিবূন",
                "পবিত্র ও মহান সেই সত্তা, যিনি এটিকে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা এটিকে বশীভূত করতে সক্ষম ছিলাম না।",
                "সূরা আয-যুখরুফ ১৩-১৪"
        ));
        allDuas.add(new MasnoonDua(
                "eating_start",
                "খাবার শুরুর দোয়া",
                "بِسْمِ اللَّهِ وَعَلَى بَرَكَةِ اللَّهِ",
                "বিসমিল্লা-হি ওয়া 'আলা বারাকাতিল্লা-হ",
                "আল্লাহর নামে এবং আল্লাহর বরকতের উপর শুরু করছি।",
                "মুস্তাদরাকে হাকেম"
        ));
        allDuas.add(new MasnoonDua(
                "eating_end",
                "খাবার শেষের দোয়া",
                "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ",
                "আলহামদু লিল্লা-হিল্লাযী আত'আমানা ওয়াসাক্বানা ওয়াজা'আলানা মুসলিমীন",
                "সমস্ত প্রশংসা সেই আল্লাহর জন্য যিনি আমাদেরকে খাওয়ালেন, পান করালেন এবং মুসলিমদের অন্তর্ভুক্ত করলেন।",
                "সুনানে আবু দাউদ ৩৮৫০"
        ));
    }

    private void filterDuas(String query) {
        if (query == null || query.trim().isEmpty()) {
            duaAdapter.updateList(allDuas);
            return;
        }

        String lower = query.toLowerCase().trim();
        List<MasnoonDua> filtered = new ArrayList<>();
        for (MasnoonDua d : allDuas) {
            if (d.getTitle().toLowerCase().contains(lower) || d.getPronunciation().toLowerCase().contains(lower) || d.getTranslation().toLowerCase().contains(lower)) {
                filtered.add(d);
            }
        }
        duaAdapter.updateList(filtered);
    }
}
