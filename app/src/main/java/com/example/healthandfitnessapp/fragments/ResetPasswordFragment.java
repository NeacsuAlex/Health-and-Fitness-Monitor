package com.example.healthandfitnessapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.interfaces.ActivityFragmentLoginCommunication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ActivityFragmentLoginCommunication activityFragmentLoginCommunication;
    private TextView register;
    private Button resetPasswordButton;
    private EditText email;
    private FirebaseAuth mAuth;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        register = view.findViewById(R.id.registerPView);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        email = view.findViewById(R.id.inputEmailForgotP);
        mAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString().trim();

                if (TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(getContext(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(txtEmail);
                    if (activityFragmentLoginCommunication != null) {
                        activityFragmentLoginCommunication.openLoginFragment();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityFragmentLoginCommunication != null) {
                    activityFragmentLoginCommunication.openRegisterFragment();
                }
            }
        });

        return view;
    }

    private void resetPassword(String emailAddress) {
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ActivityFragmentLoginCommunication) {
            activityFragmentLoginCommunication = (ActivityFragmentLoginCommunication) context;
        }
    }
}