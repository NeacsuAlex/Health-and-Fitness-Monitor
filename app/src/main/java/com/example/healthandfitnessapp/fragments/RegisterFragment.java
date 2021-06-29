package com.example.healthandfitnessapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.activities.LoginActivity;
import com.example.healthandfitnessapp.interfaces.ActivityFragmentLoginCommunication;
import com.example.healthandfitnessapp.models.User;
import com.example.healthandfitnessapp.services.NotificationService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ActivityFragmentLoginCommunication activityFragmentLoginCommunication;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;
    private TextView login;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private NotificationService notificationService = new NotificationService();

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        username = view.findViewById(R.id.inputUserName);
        email = view.findViewById(R.id.inputEmailRegister);
        password = view.findViewById(R.id.inputPasswordRegister);
        confirmPassword = view.findViewById(R.id.inputCPasswordRegister);
        registerButton = view.findViewById(R.id.registerButton);
        login = view.findViewById(R.id.loginView);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityFragmentLoginCommunication != null) {
                    activityFragmentLoginCommunication.openLoginFragment();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtEmail = email.getText().toString().trim();
                String txtPassword = password.getText().toString();
                String txtCPassword = confirmPassword.getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtEmail)
                        || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtCPassword)) {
                    Toast.makeText(getContext(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtCPassword.length() < 6 || !txtCPassword.equals(txtPassword)) {
                    Toast.makeText(getContext(), "Password too short or invalid password!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername, txtEmail, txtPassword);
                }
            }
        });

        return view;
    }

    private void registerUser(String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User user = new User(username, email, 0L, 0L, 0L, 0L, 0L,0L, 0L);
                String userID = mAuth.getUid();
                mDatabase.child("users").child(userID).setValue(user);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username",user.username);
                editor.putString("email",user.email);
                editor.commit();

                if (activityFragmentLoginCommunication != null) {
                    activityFragmentLoginCommunication.openLoginFragment();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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