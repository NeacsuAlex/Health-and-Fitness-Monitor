package com.example.healthandfitnessapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.models.User;

import com.example.healthandfitnessapp.services.NotificationService;
import com.example.healthandfitnessapp.services.SettingsManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                for (DataSnapshot key : snapshot.getChildren()) {
                    if (key.getKey().equals(mAuth.getUid())) {
                        user = key.getValue(User.class);
                        break;
                    }
                }

                EditTextPreference username = (EditTextPreference) findPreference("username");
                username.setText(user.username);

                EditTextPreference email = (EditTextPreference) findPreference("email");
                email.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SwitchPreferenceCompat notificationSwitch=(SwitchPreferenceCompat) findPreference("notifications");
        notificationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((boolean) newValue)
                {
                    SettingsManager.receiveNotifications=true;
                }
                else
                {
                    SettingsManager.receiveNotifications=false;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("username")) {
            EditTextPreference preference = findPreference(key);
            mDatabase.child(mAuth.getUid()).child("username").setValue(preference.getText());
        }

        if (key.equals("email")) {
            EditTextPreference preference = findPreference(key);
            mDatabase.child(mAuth.getUid()).child("email").setValue(preference.getText());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updateEmail(preference.getText())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("tag", "User email address updated.");
                            }
                        }
                    });
        }
    }
}