package com.example.healthandfitnessapp.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.adapters.MyAdapter;
import com.example.healthandfitnessapp.models.Element;
import com.example.healthandfitnessapp.models.Notification;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static ArrayList<Element> elements = new ArrayList<>();
    private static MyAdapter myAdapter = null;

    private static TextView noNotificationText;
    private static View view = null;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.notifications_list);
        //elements.clear();
        myAdapter = new MyAdapter(this.elements);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.myAdapter);
        recyclerView.post(() -> myAdapter.notifyDataSetChanged());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        noNotificationText = view.findViewById(R.id.noNotificationsTextView);
        if (elements.size() == 0)
            noNotificationText.setVisibility(View.VISIBLE);
        else
            noNotificationText.setVisibility(View.INVISIBLE);
        return view;
    }

    public static void AddNotification(Notification notification) {
        elements.add(notification);
        myAdapter = new MyAdapter(elements);
        myAdapter.notifyDataSetChanged();
    }


}