package com.example.healthandfitnessapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.models.User;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.activities.HomeActivity;
import com.example.healthandfitnessapp.models.BMI;
import com.example.healthandfitnessapp.models.User;
import com.example.healthandfitnessapp.services.NotificationService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    TextView heightTextView, weightTextView;
    EditText heightEditTextView, weightEditTextView;
    SwitchCompat heightSwitch, weightSwitch;
    TextView bmiResult;


    Button decrese_water_counter, decrese_calories_counter, decrese_sleep_counter;
    Button increase_water_counter, increase_calories_counter, increase_sleep_counter;
    Long water_counter, step_counter, calories_counter, sleep_counter;
    TextView waterText, stepText, caloriesText, sleepText;

    Long day;

    boolean usingCM, usingKG;


    public StatisticsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        waterText=view.findViewById(R.id.text_water_counter_edit);
        decrese_water_counter=view.findViewById(R.id.decrease_water_counter);
        increase_water_counter=view.findViewById(R.id.increase_water_counter);

        stepText=view.findViewById(R.id.text_step_counter_edit);
        stepText.setText(step_counter+"");

        sleepText=view.findViewById(R.id.text_sleep_counter_edit);
        decrese_sleep_counter=view.findViewById(R.id.decrease_sleep_counter);
        increase_sleep_counter=view.findViewById(R.id.increase_sleep_counter);
        sleepText.setText(sleep_counter+"");

        caloriesText=view.findViewById(R.id.text_calories_counter_edit);
        decrese_calories_counter=view.findViewById(R.id.decrease_calories_counter);
        increase_calories_counter=view.findViewById(R.id.increase_calories_counter);
        caloriesText.setText(calories_counter+"");

        bmiResult=view.findViewById(R.id.BMIresult);

        ReadStatisticsFromDatabase();
        InitWaterStatistics();
        InitSleepStatistics();
        InitCaloriesStatistics();

        usingCM=true;
        usingKG=true;
        heightTextView=view.findViewById(R.id.heightTextView);
        heightEditTextView=view.findViewById(R.id.heightEditTextView);
        heightSwitch=(SwitchCompat)view.findViewById(R.id.switchHeight);
        weightTextView=view.findViewById(R.id.weightTextView);
        weightEditTextView=view.findViewById(R.id.weightEditTextView);
        weightSwitch=(SwitchCompat)view.findViewById(R.id.switchWeight);
        heightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useCm();
            }
        });
        weightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useKg();
            }
        });
        heightEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalculateBMI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        weightEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalculateBMI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void useCm() {
        if(heightSwitch.isChecked())
        {
            heightTextView.setText("Height (cm)");
            heightSwitch.setText("Use cm");
            usingCM=true;
        }
        else
        {
            heightTextView.setText("Height (feet)");
            heightSwitch.setText("Use feet");
            usingCM=false;
        }
        CalculateBMI();
    }

    private void useKg() {
        if(weightSwitch.isChecked())
        {
            weightTextView.setText("Weight (kg)");
            weightSwitch.setText("Use kg");
            usingKG=true;
        }
        else
        {
            weightTextView.setText("Weight (lb)");
            weightSwitch.setText("Use lb");
            usingKG=false;
        }
        CalculateBMI();
    }

    private void CalculateBMI()
    {
        String height = heightEditTextView.getText().toString();
        String weight = weightEditTextView.getText().toString();
        if(!height.isEmpty() && !weight.isEmpty())
        {
            float bmi = BMI.calculateBMI(Float.parseFloat(weight), Float.parseFloat(height), usingKG, usingCM);
            String category =BMI.getBMICategory(bmi).toString();
            DecimalFormat df=new DecimalFormat();
            df.setMaximumFractionDigits(2);
            bmiResult.setText("BMI: "+df.format(bmi)+"\n"+category);

        }
        else
        {
            bmiResult.setText("");
        }
    }

    private void ReadStatisticsFromDatabase()
    {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                User user = new User();
                for (DataSnapshot key : snapshot.getChildren()) {
                    if (key.getKey().equals(mAuth.getUid())) {
                        user = key.getValue(User.class);
                        break;
                    }
                }
                waterText.setText(user.drinkerWater.toString());
                water_counter=Long.valueOf(waterText.getText().toString()).longValue();
                sleepText.setText(user.sleepTime.toString());
                sleep_counter=Long.valueOf(sleepText.getText().toString()).longValue();
                stepText.setText(user.steps.toString());
                step_counter=Long.valueOf(stepText.getText().toString()).longValue();
                caloriesText.setText(user.caloriesBurned.toString());
                calories_counter=Long.valueOf(caloriesText.getText().toString()).longValue();
                day=user.date;
                final Calendar now = GregorianCalendar.getInstance();
                Long dayNumber = Long.valueOf(now.get(Calendar.DAY_OF_MONTH));
                if(day!=dayNumber)
                {
                    mDatabase.child(mAuth.getUid()).child("date").setValue(dayNumber);
                    ResetStatistics();
                    day=dayNumber;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void InitWaterStatistics()
    {
        waterText.setText(water_counter+"");
        decrese_water_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water_counter=(water_counter-1<0)?0:water_counter-1;
                waterText.setText(water_counter+"");
                mDatabase.child(mAuth.getUid()).child("drinkerWater").setValue(Long.valueOf(waterText.getText().toString()).longValue());
            }
        });
        increase_water_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water_counter=water_counter+1;
                waterText.setText(water_counter+"");
                mDatabase.child(mAuth.getUid()).child("drinkerWater").setValue(Long.valueOf(waterText.getText().toString()));
            }
        });
    }

    private void InitSleepStatistics()
    {
        sleepText.setText(sleep_counter+"");
        decrese_sleep_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep_counter=(sleep_counter-10<0)?0:sleep_counter-10;
                sleepText.setText(sleep_counter+"");
                mDatabase.child(mAuth.getUid()).child("sleepTime").setValue(Long.valueOf(sleepText.getText().toString()));
            }
        });
        increase_sleep_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep_counter=sleep_counter+10;
                sleepText.setText(sleep_counter+"");
                mDatabase.child(mAuth.getUid()).child("sleepTime").setValue(Long.valueOf(sleepText.getText().toString()));
            }
        });
    }

    private void InitCaloriesStatistics()
    {
        caloriesText.setText(calories_counter+"");
        decrese_calories_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calories_counter=(calories_counter-5<0)?0:calories_counter-5;
                caloriesText.setText(calories_counter+"");
                mDatabase.child(mAuth.getUid()).child("caloriesBurned").setValue(Long.valueOf(caloriesText.getText().toString()));
            }
        });
        increase_calories_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calories_counter=calories_counter+5;
                caloriesText.setText(calories_counter+"");
                mDatabase.child(mAuth.getUid()).child("caloriesBurned").setValue(Long.valueOf(caloriesText.getText().toString()));
            }
        });

    }

    private void ResetStatistics()
    {
        water_counter=0L;
        sleep_counter=0L;
        step_counter=0L;
        calories_counter=0L;

        sleepText.setText("0");
        stepText.setText("0");
        caloriesText.setText("0");
        waterText.setText("0");

        mDatabase.child(mAuth.getUid()).child("caloriesBurned").setValue(Long.valueOf(caloriesText.getText().toString()));
        mDatabase.child(mAuth.getUid()).child("sleepTime").setValue(Long.valueOf(sleepText.getText().toString()));
        mDatabase.child(mAuth.getUid()).child("drinkerWater").setValue(Long.valueOf(waterText.getText().toString()));
        mDatabase.child(mAuth.getUid()).child("steps").setValue(Long.valueOf(stepText.getText().toString()));

        InitWaterStatistics();
        InitSleepStatistics();
        InitCaloriesStatistics();

    }
}