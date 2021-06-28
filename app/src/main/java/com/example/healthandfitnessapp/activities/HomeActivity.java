package com.example.healthandfitnessapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.healthandfitnessapp.fragments.AboutUsFragment;
import com.example.healthandfitnessapp.fragments.AlarmFragment;
import com.example.healthandfitnessapp.fragments.NotificationsFragment;
import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.fragments.AchievementsFragment;
import com.example.healthandfitnessapp.fragments.ExercisesFragment;
import com.example.healthandfitnessapp.fragments.SettingsFragment;
import com.example.healthandfitnessapp.fragments.StatisticsFragment;
import com.example.healthandfitnessapp.fragments.TravelFragment;
import com.example.healthandfitnessapp.fragments.WriteFeedbackFragment;
import com.example.healthandfitnessapp.interfaces.ActivityFragmentHomeComunication;
import com.example.healthandfitnessapp.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity implements ActivityFragmentHomeComunication {

    private DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    TextView userName;
    TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StatisticsFragment fragment = new StatisticsFragment();
        fragmentTransaction.add(R.id.home_frame_layout, fragment);
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.user_name);
        userEmail = (TextView) headerView.findViewById(R.id.user_email);

        navigationView.bringToFront();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.closeDrawer(GravityCompat.START);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_statistics: {
                        OpenStatisticsFragment();
                        return true;
                    }
                    case R.id.nav_exercices: {
                        OpenExercisesFragment();
                        return true;
                    }
                    case R.id.nav_achievements: {
                        OpenAchievementsFragment();
                        return true;
                    }
                    case R.id.nav_notifications: {
                        OpenNotificationsFragment();
                        return true;
                    }
                    case R.id.nav_travel: {
                        OpenTravelFragment();
                        return true;
                    }
                    case R.id.nav_alarm: {
                        OpenAlarmFragment();
                        return true;
                    }
                    case R.id.nav_settings: {
                        OpenSettingsFragment();
                        return true;
                    }
                    case R.id.nav_help: {
                        OpenHelpActivity();
                        return true;
                    }
                    case R.id.nav_sign_out: {
                        SignOut();
                        return true;
                    }
                    default:
                        break;
                }
                return false;
            }
        });

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
                userName.setText(user.username);
                userEmail.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OpenStatisticsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = StatisticsFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, StatisticsFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenExercisesFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ExercisesFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, ExercisesFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenAchievementsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = AchievementsFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, AchievementsFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenNotificationsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = NotificationsFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, NotificationsFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenTravelFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = TravelFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, TravelFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenAlarmFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = AlarmFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, AlarmFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenSettingsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SettingsFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.home_frame_layout, new SettingsFragment(), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void OpenHelpActivity() {
        Intent intent = new Intent(this, HelpAndFeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}