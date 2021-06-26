package com.example.healthandfitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.constants.Constants;
import com.example.healthandfitnessapp.interfaces.OnItemsClickedListener;
import com.example.healthandfitnessapp.models.FitnessProgramme;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FitnessProgramme> elementList;
    OnItemsClickedListener onItemsClickedListener;

    public MyAdapter(ArrayList<FitnessProgramme> elementList, OnItemsClickedListener onItemsClickedListener) {
        this.elementList = elementList;
        this.onItemsClickedListener = onItemsClickedListener;
    }

    public MyAdapter(ArrayList<FitnessProgramme> elementList) {
        this.elementList = elementList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_fitness, parent, false);
        FitnessProgrammeViewHolder fitnessProgrammeViewHolder = new FitnessProgrammeViewHolder(view);
        return fitnessProgrammeViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FitnessProgrammeViewHolder) {
            FitnessProgramme fitnessProgramme = (FitnessProgramme) elementList.get(position);
            ((FitnessProgrammeViewHolder) holder).bind(fitnessProgramme);
        }
    }

    @Override
    public int getItemCount() {
        return this.elementList.size();
    }

    class FitnessProgrammeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textDetail;
        private final TextView textTime;
        private final ImageView fitnessImage;
        private final View view;

        FitnessProgrammeViewHolder(View view) {
            super(view);
            textDetail = view.findViewById(R.id.fitnessDetailTextView);
            textTime = view.findViewById(R.id.timeTextView);
            fitnessImage = view.findViewById(R.id.fitnessImageView);
            this.view = view;
        }

        void bind(FitnessProgramme fitnessProgramme) {
            textDetail.setText(fitnessProgramme.title);
            textTime.setText(fitnessProgramme.duration);
            String imageViewUrl = fitnessProgramme.urlThumbnail;
            Picasso picasso = Picasso.get();
            picasso.load(imageViewUrl).into(fitnessImage);
            view.setOnClickListener(v -> {
               if (onItemsClickedListener != null) {
                    onItemsClickedListener.onItemClick(fitnessProgramme);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
