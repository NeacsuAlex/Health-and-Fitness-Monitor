package com.example.healthandfitnessapp.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.fragments.LoginFragment;
import com.example.healthandfitnessapp.fragments.RegisterFragment;
import com.example.healthandfitnessapp.fragments.ResetPasswordFragment;
import com.example.healthandfitnessapp.interfaces.ActivityFragmentLoginCommunication;


public class LoginActivity extends AppCompatActivity implements ActivityFragmentLoginCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openLoginFragment();
    }

    @Override
    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void openLoginFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = LoginFragment.class.getName();
        FragmentTransaction addTransaction = transaction.replace(
                R.id.login_frame_layout, LoginFragment.newInstance("", ""), tag
        );

        addTransaction.commit();
    }

    @Override
    public void openRegisterFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = RegisterFragment.class.getName();
        FragmentTransaction addTransaction = transaction.replace(
                R.id.login_frame_layout, RegisterFragment.newInstance("", ""), tag
        );

        addTransaction.commit();
    }

    @Override
    public void openResetPasswordFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ResetPasswordFragment.class.getName();
        FragmentTransaction addTransaction = transaction.replace(
                R.id.login_frame_layout, ResetPasswordFragment.newInstance("", ""), tag
        );

        addTransaction.commit();
    }
}