package com.example.healthandfitnessapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.adapters.MyAdapter;
import com.example.healthandfitnessapp.constants.Constants;
import com.example.healthandfitnessapp.interfaces.ActivityFragmentHelpFeedbackCommunication;
import com.example.healthandfitnessapp.models.Element;
import com.example.healthandfitnessapp.models.FitnessProgramme;
import com.example.healthandfitnessapp.models.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Element> elements = new ArrayList<>();
    private MyAdapter myAdapter = null;
    private DatabaseReference mDatabase;
    private ActivityFragmentHelpFeedbackCommunication activityFragmentHelpFeedbackCommunication;
    private Button openReviewButton;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
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
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.reviews_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);

        elements.clear();

        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");
        myAdapter = new MyAdapter(this.elements);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.post(() -> myAdapter.notifyDataSetChanged());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        try {
            getDefaultReviews();
        } catch (InterruptedException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        getReviews();

        openReviewButton = view.findViewById(R.id.openReviewButton);
        openReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityFragmentHelpFeedbackCommunication != null) {
                    activityFragmentHelpFeedbackCommunication.OpenFeedbackFragment();
                }
            }
        });

        return view;
    }

    private void getReviews() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Review review = snapshot.getValue(Review.class);
                    elements.add(review);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), Constants.DEFAULT_REVIEW_LOADING_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDefaultReviews() throws InterruptedException {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constants.DEFAULT_REVIEW_URL;
        StringRequest getAlbumsRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        handleCommentsResponse(response);
                    } catch (JSONException exception) {
                        Log.e(Constants.DEFAULT_REVIEW_LOG_ERROR, exception.getMessage());
                    }
                },
                error -> Toast.makeText(getContext(), Constants.DEFAULT_REVIEW_LOADING_ERROR, Toast.LENGTH_SHORT).show()
        );
        queue.add(getAlbumsRequest);
    }

    private void handleCommentsResponse(String response) throws JSONException {
        JSONArray fitnessJSONArray = new JSONArray(response);
        for (int index = 0; index < fitnessJSONArray.length(); ++index) {
            JSONObject fitnessJSON = (JSONObject) fitnessJSONArray.get(index);
            String username = fitnessJSON.getString("username");
            Number nrStars = fitnessJSON.getDouble("nrStars");
            String review = fitnessJSON.getString("review");
            Review fitnessProgramme = new Review(username, review, nrStars.longValue());
            elements.add(fitnessProgramme);
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActivityFragmentHelpFeedbackCommunication) {
            activityFragmentHelpFeedbackCommunication = (ActivityFragmentHelpFeedbackCommunication) context;
        }
    }
}