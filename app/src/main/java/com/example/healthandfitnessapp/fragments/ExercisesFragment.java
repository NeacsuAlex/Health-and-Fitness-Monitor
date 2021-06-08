package com.example.healthandfitnessapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.adapters.MyAdapter;
import com.example.healthandfitnessapp.models.FitnessProgramme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExercisesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ArrayList<FitnessProgramme> elements = new ArrayList<>();
    private MyAdapter myAdapter = null;

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
        RecyclerView recyclerView = view.findViewById(R.id.fitness_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,false);
        elements.clear();
        getPhotos();
        myAdapter = new MyAdapter(this.elements);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.post(() -> myAdapter.notifyDataSetChanged());
        return view;
    }

    void getPhotos() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://jsonplaceholder.typicode.com" + "/photos?" + "albumId" + "=" + 1;
        StringRequest getAlbumsRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handlePhotosResponse(response);
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "PHOTO ERROR", Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(getAlbumsRequest);
    }

    void handlePhotosResponse(String response) throws JSONException {
        JSONArray photosJSONArray = new JSONArray(response);
        for (int index = 0; index < photosJSONArray.length(); ++index) {
            JSONObject userPostJSON = (JSONObject) photosJSONArray.get(index);
            int id = userPostJSON.getInt("id");
            String title = userPostJSON.getString("title");
            String url = userPostJSON.getString("url");
            String thumbnailUrl = userPostJSON.getString("thumbnailUrl");
            FitnessProgramme fitnessProgramme = new FitnessProgramme(title, url, url,thumbnailUrl,thumbnailUrl,thumbnailUrl);
            this.elements.add(fitnessProgramme);

        }
        myAdapter.notifyDataSetChanged();
    }
}