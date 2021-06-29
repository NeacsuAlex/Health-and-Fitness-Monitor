package com.example.healthandfitnessapp.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.activities.HomeActivity;
import com.example.healthandfitnessapp.adapters.MyAdapter;
import com.example.healthandfitnessapp.constants.Constants;
import com.example.healthandfitnessapp.interfaces.OnItemsClickedListener;
import com.example.healthandfitnessapp.models.Element;
import com.example.healthandfitnessapp.models.FitnessProgramme;
import com.example.healthandfitnessapp.models.Notification;
import com.example.healthandfitnessapp.services.NotificationService;
import com.example.healthandfitnessapp.services.SettingsManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static com.example.healthandfitnessapp.constants.Constants.DEFAULT_YOUTUBE_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExercisesFragment extends Fragment implements OnItemsClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ArrayList<Element> elements = new ArrayList<>();
    private MyAdapter myAdapter = null;
    private FitnessProgramme selectedFitnessProgramme;
    private DatabaseReference mDatabase;
    private TextView fitnessTitleText;
    private TextView fitnessDescriptionText;
    private TextView durationText;
    private TextView difficultyText;
    private Button startProgrammeButton;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExercicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExercisesFragment newInstance(String param1, String param2) {
        ExercisesFragment fragment = new ExercisesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exercices, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.reviews_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);

        durationText =view.findViewById(R.id.durationTextView);
        fitnessTitleText =view.findViewById(R.id.fitnessTitleTextView);
        fitnessDescriptionText =view.findViewById(R.id.fitnessDescriptionTextView);
        difficultyText =view.findViewById(R.id.difficultyTextView);
        startProgrammeButton=view.findViewById(R.id.fitnessButton);

        elements.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("fitness");
        getImageData();
        myAdapter = new MyAdapter(this.elements,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.post(() -> myAdapter.notifyDataSetChanged());

        startProgrammeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFitnessProgramme != null) {
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    NotificationService notificationService=new NotificationService();
                    if(SettingsManager.receiveNotifications) {
                        long millis=System.currentTimeMillis();
                        java.sql.Date date=new java.sql.Date(millis);
                        notificationService.showNotification(getContext(), "You start a new programme!", fitnessTitleText.getText().toString(), intent, 1);
                        NotificationsFragment.AddNotification(new Notification("You start a new programme!",fitnessTitleText.getText().toString(),date));
                    }
                    watchYoutubeVideo(selectedFitnessProgramme.urlVideo);
                }
            }
        });

        return view;
    }

    private void setFirstFitnessProgramme() {
        selectedFitnessProgramme= (FitnessProgramme) elements.get(0);
        durationText.setText(selectedFitnessProgramme.duration);
        fitnessTitleText.setText(selectedFitnessProgramme.title);
        fitnessDescriptionText.setText(selectedFitnessProgramme.description);
        difficultyText.setText(selectedFitnessProgramme.difficulty);
    }

    private void getImageData() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FitnessProgramme fitnessProgramme = snapshot.getValue(FitnessProgramme.class);
                    elements.add(fitnessProgramme);
                    myAdapter.notifyDataSetChanged();
                }
                setFirstFitnessProgramme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(DEFAULT_YOUTUBE_URL + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onItemClick(FitnessProgramme fitnessProgramme) {
        durationText.setText(fitnessProgramme.duration);
        fitnessTitleText.setText(fitnessProgramme.title);
        fitnessDescriptionText.setText(fitnessProgramme.description);
        difficultyText.setText(fitnessProgramme.difficulty);
        selectedFitnessProgramme=fitnessProgramme;
    }
}