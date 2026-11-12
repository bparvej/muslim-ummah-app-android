package com.example.muslimummah.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muslimummah.R;
import com.example.muslimummah.models.MasnoonDua;

import java.util.List;

public class DuaAdapter extends RecyclerView.Adapter<DuaAdapter.DuaViewHolder> {

    public interface OnDuaClickListener {
        void onDuaClick(MasnoonDua dua);
    }

    private List<MasnoonDua> duaList;
    private final OnDuaClickListener clickListener;

    public DuaAdapter(List<MasnoonDua> duaList, OnDuaClickListener clickListener) {
        this.duaList = duaList;
        this.clickListener = clickListener;
    }

    public void updateList(List<MasnoonDua> newList) {
        this.duaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dua, parent, false);
        return new DuaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DuaViewHolder holder, int position) {
        MasnoonDua dua = duaList.get(position);
        holder.tvTitle.setText(dua.getTitle());
        holder.tvSnippet.setText(dua.getPronunciation());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDuaClick(dua);
            }
        });
    }

    @Override
    public int getItemCount() {
        return duaList != null ? duaList.size() : 0;
    }

    static class DuaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSnippet;

        public DuaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_dua_title);
            tvSnippet = itemView.findViewById(R.id.tv_dua_snippet);
        }
    }
}
