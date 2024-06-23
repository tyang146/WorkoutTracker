package com.example.finalproject.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;

// for use in WorkoutMenuActivity
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RViewHolder> {
    final ArrayList<SubmenuPreviewCard> cardsList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class RViewHolder extends RecyclerView.ViewHolder {
        //The TextView inside the cards
        public TextView rvTextView;

        public RViewHolder(@NonNull View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            rvTextView  = itemView.findViewById(R.id.cardTextView);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public RecyclerViewAdapter(ArrayList<SubmenuPreviewCard> cardList) {
        this.cardsList = cardList;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submenu_preview_card_layout, parent, false);
        return new RViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        SubmenuPreviewCard currentCard = cardsList.get(position);
        holder.rvTextView.setText(currentCard.getDescription());// display proper text for each card
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }
}