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
import com.example.healthandfitnessapp.models.Element;
import com.example.healthandfitnessapp.models.FitnessProgramme;
import com.example.healthandfitnessapp.models.Notification;
import com.example.healthandfitnessapp.models.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Element> elementList;
    OnItemsClickedListener onItemsClickedListener;

    public MyAdapter(ArrayList<Element> elementList, OnItemsClickedListener onItemsClickedListener) {
        this.elementList = elementList;
        this.onItemsClickedListener = onItemsClickedListener;
    }

    public MyAdapter(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }

    @Override
    public int getItemViewType(int position) {
        if (elementList.get(position) instanceof FitnessProgramme) {
            return 0;
        }
        if (elementList.get(position) instanceof Review) {
            return 1;
        }
        if (elementList.get(position) instanceof Notification) {
            return 2;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_fitness, parent, false);
            return new FitnessProgrammeViewHolder(view);
        }
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_review, parent, false);
            return new ReviewViewHolder(view);
        }
        if (viewType == 2) {
            View view = inflater.inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FitnessProgrammeViewHolder) {
            FitnessProgramme fitnessProgramme = (FitnessProgramme) elementList.get(position);
            ((FitnessProgrammeViewHolder) holder).bind(fitnessProgramme);
        }

        if (holder instanceof ReviewViewHolder) {
            Review review = (Review) elementList.get(position);
            ((ReviewViewHolder) holder).bind(review);
        }
        if (holder instanceof NotificationViewHolder) {
            Notification notification = (Notification) elementList.get(position);
            ((NotificationViewHolder) holder).bind(notification);
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

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView textReview;
        private final TextView textUsername;
        private final TextView nrStars;
        private final View view;

        ReviewViewHolder(View view) {
            super(view);
            textReview = view.findViewById(R.id.reviewTextView);
            textUsername = view.findViewById(R.id.usernameTextView);
            nrStars = view.findViewById(R.id.starTextView);
            this.view = view;
        }

        void bind(Review review) {
            textReview.setText(review.review);
            textUsername.setText(review.username);
            nrStars.setText(review.nrStars.toString()+" Stars");

        }
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView message;
        private final TextView date;
        private final View view;

        NotificationViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.notificationTitleTextView);
            message = view.findViewById(R.id.notificationMessageTextView);
            date = view.findViewById(R.id.notificationDateTextView);
            this.view = view;
        }

        void bind(Notification notification) {
            title.setText(notification.title);
            message.setText(notification.message);
            date.setText(notification.date.toString());

        }
    }
}
