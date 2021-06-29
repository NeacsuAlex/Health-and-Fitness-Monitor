package com.example.healthandfitnessapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.fragments.AboutUsFragment;
import com.example.healthandfitnessapp.fragments.WriteFeedbackFragment;
import com.example.healthandfitnessapp.interfaces.ActivityFragmentHelpFeedbackCommunication;

public class HelpAndFeedbackActivity extends AppCompatActivity implements ActivityFragmentHelpFeedbackCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
        OpenAboutFragment();
    }

    @Override
    public void OpenAboutFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = AboutUsFragment.class.getName();
        FragmentTransaction addTransaction = transaction.replace(
                R.id.about_feedback_frame_layout, AboutUsFragment.newInstance("", ""), tag
        );

        addTransaction.commit();
    }

    @Override
    public void OpenFeedbackFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = WriteFeedbackFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.about_feedback_frame_layout, WriteFeedbackFragment.newInstance("", ""), tag
        ).addToBackStack(null);

        replaceTransaction.commit();
    }

    @Override
    public void ReplaceWithAboutFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = AboutUsFragment.class.getName();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.about_feedback_frame_layout, AboutUsFragment.newInstance("", ""), tag
        );

        replaceTransaction.commit();
    }
}