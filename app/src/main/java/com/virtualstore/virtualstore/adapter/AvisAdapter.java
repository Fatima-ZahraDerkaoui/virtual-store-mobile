package com.virtualstore.virtualstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.Avis;
import java.util.List;

public class AvisAdapter extends RecyclerView.Adapter<AvisAdapter.AvisViewHolder> {

    private List<Avis> avisList;

    public AvisAdapter(List<Avis> avisList) {
        this.avisList = avisList;
    }

    public static class AvisViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuteur, tvCommentaire;
        RatingBar ratingAvis;

        public AvisViewHolder(View view) {
            super(view);
            tvAuteur = view.findViewById(R.id.tvAuteur);
            tvCommentaire = view.findViewById(R.id.tvCommentaire);
            ratingAvis = view.findViewById(R.id.ratingAvis);
        }
    }

    @NonNull
    @Override
    public AvisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avis, parent, false);
        return new AvisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisViewHolder holder, int position) {
        Avis avis = avisList.get(position);
        holder.tvAuteur.setText(avis.getAuteur());
        holder.tvCommentaire.setText(avis.getCommentaire());
        holder.ratingAvis.setRating(avis.getNote());
    }

    @Override
    public int getItemCount() { return avisList.size(); }
}