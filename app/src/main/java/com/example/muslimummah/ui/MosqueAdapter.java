package com.example.muslimummah.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muslimummah.R;
import com.example.muslimummah.models.Mosque;

import java.util.List;

public class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.MosqueViewHolder> {

    public interface OnMosqueClickListener {
        void onMosqueClick(Mosque mosque);
    }

    private List<Mosque> mosqueList;
    private final OnMosqueClickListener clickListener;

    public MosqueAdapter(List<Mosque> mosqueList, OnMosqueClickListener clickListener) {
        this.mosqueList = mosqueList;
        this.clickListener = clickListener;
    }

    public void updateList(List<Mosque> newList) {
        this.mosqueList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MosqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mosque, parent, false);
        return new MosqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueViewHolder holder, int position) {
        Mosque mosque = mosqueList.get(position);
        holder.tvName.setText(mosque.getName());
        holder.tvAddress.setText("📍 " + mosque.getAddress());
        holder.tvDistance.setText(mosque.getDistanceKm() + " km");

        if (mosque.getZamatSchedule() != null && mosque.getZamatSchedule().containsKey("Asr")) {
            holder.tvNextJamaat.setText("🕒 পরবর্তী জামাত: আসর " + mosque.getZamatSchedule().get("Asr") + " PM");
        } else {
            holder.tvNextJamaat.setText("🕒 নিয়মিত জামাত অনুষ্ঠিত হয়");
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onMosqueClick(mosque);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mosqueList != null ? mosqueList.size() : 0;
    }

    static class MosqueViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvDistance, tvNextJamaat;

        public MosqueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_mosque_name);
            tvAddress = itemView.findViewById(R.id.tv_mosque_address);
            tvDistance = itemView.findViewById(R.id.tv_mosque_distance);
            tvNextJamaat = itemView.findViewById(R.id.tv_next_jamaat);
        }
    }
}
