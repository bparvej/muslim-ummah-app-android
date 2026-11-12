package com.example.muslimummah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muslimummah.R;
import com.example.muslimummah.config.AppConfig;
import com.example.muslimummah.models.Mosque;
import com.example.muslimummah.repository.MosqueRepository;

import java.util.ArrayList;
import java.util.List;

public class MosquesFragment extends Fragment {

    private TextView tvRadiusSubtitle;
    private EditText etSearch;
    private RecyclerView rvMosques;
    private MosqueAdapter mosqueAdapter;

    private AppConfig appConfig;
    private MosqueRepository mosqueRepository;
    private List<Mosque> allMosques = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mosques, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appConfig = AppConfig.getInstance(requireContext());
        mosqueRepository = new MosqueRepository(requireContext());

        tvRadiusSubtitle = view.findViewById(R.id.tv_radius_subtitle);
        etSearch = view.findViewById(R.id.et_search_mosque);
        rvMosques = view.findViewById(R.id.rv_mosques);

        rvMosques.setLayoutManager(new LinearLayoutManager(requireContext()));
        mosqueAdapter = new MosqueAdapter(allMosques, mosque -> {
            Intent intent = new Intent(requireContext(), MosqueDetailActivity.class);
            intent.putExtra("mosque_name", mosque.getName());
            intent.putExtra("mosque_address", mosque.getAddress());
            intent.putExtra("mosque_lat", mosque.getLat());
            intent.putExtra("mosque_lon", mosque.getLon());
            startActivity(intent);
        });
        rvMosques.setAdapter(mosqueAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMosques(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadMosques();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvRadiusSubtitle.setText(appConfig.getSearchRadiusKm() + " কিমি ব্যাসার্ধে অনুসন্ধান করা হচ্ছে (" + appConfig.getBaseLocationName() + ")");
    }

    private void loadMosques() {
        tvRadiusSubtitle.setText(appConfig.getSearchRadiusKm() + " কিমি ব্যাসার্ধে অনুসন্ধান করা হচ্ছে (" + appConfig.getBaseLocationName() + ")");
        mosqueRepository.fetchNearbyMosques(new MosqueRepository.MosqueCallback() {
            @Override
            public void onSuccess(List<Mosque> mosques) {
                allMosques = mosques;
                if (isAdded()) {
                    mosqueAdapter.updateList(allMosques);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void filterMosques(String query) {
        if (query == null || query.trim().isEmpty()) {
            mosqueAdapter.updateList(allMosques);
            return;
        }

        String lower = query.toLowerCase().trim();
        List<Mosque> filtered = new ArrayList<>();
        for (Mosque m : allMosques) {
            if (m.getName().toLowerCase().contains(lower) || m.getAddress().toLowerCase().contains(lower)) {
                filtered.add(m);
            }
        }
        mosqueAdapter.updateList(filtered);
    }
}
