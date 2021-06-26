package com.example.healthandfitnessapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.services.LocationService;

import java.util.Formatter;
import java.util.Locale;

public class TravelFragment extends Fragment implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SwitchCompat switchUnitMeterCompat;
    TextView speedTextView,latitudeTextView,longitudeTextView, distanceTextView;
    Chronometer chronometer;
    Button startChronometer, pauseChronometer, stopChronometer;
    boolean isChronometerRunning, pausePressed;

    public TravelFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TravelFragment newInstance(String param1, String param2) {
        TravelFragment fragment = new TravelFragment();
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
        View view = inflater.inflate(R.layout.fragment_travel, container, false);
        switchUnitMeterCompat=view.findViewById(R.id.switch_unit_meter);
        speedTextView=view.findViewById(R.id.speed_text);
        latitudeTextView=view.findViewById(R.id.latitude_text);
        longitudeTextView=view.findViewById(R.id.longitude_text);
        distanceTextView=view.findViewById(R.id.distance_text);
        chronometer = (Chronometer)view.findViewById(R.id.chronometer);
        startChronometer=(Button)view.findViewById(R.id.start_timer);
        pauseChronometer=(Button)view.findViewById(R.id.pause_timer);
        stopChronometer=(Button)view.findViewById(R.id.stop_timer);

        isChronometerRunning=false;
        pausePressed=false;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&& ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        else
        {
            InitSpeed();
        }

        this.updateSpeed(null);
        this.updateLocation(null);
        this.updateDistance(null);

        switchUnitMeterCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateSpeed(null);
            }
        });

        startChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartChronometer();
            }
        });

        pauseChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PauseChronometer();
            }
        });

        stopChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopChronometer();
            }
        });
        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            LocationService locationService=new LocationService(location, this.useMetricUnits());
            this.updateSpeed(locationService);
            this.updateLocation(locationService);
            this.updateDistance(locationService);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void InitSpeed()
    {
        LocationManager locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void updateSpeed(LocationService locationService)
    {
        float currentSpeed=0f;
        if(locationService!=null)
        {
            locationService.setUserMetricUnits(this.useMetricUnits());
            currentSpeed=locationService.getSpeed();
        }

        Formatter formatter=new Formatter(new StringBuilder());
        formatter.format(Locale.US, "%5.1f", currentSpeed);
        String speed=formatter.toString();
        speed=speed.replace(" ", "0");

        if(this.useMetricUnits())
        {
            speedTextView.setText("Speed: "+speed+" km/h");
        }
        else
        {
            speedTextView.setText("Speed: "+speed+" miles/h");
        }
    }

    private void updateLocation(LocationService locationService) {
        double latitude =0,longitude =0;
        if(locationService!=null){
            latitude = locationService.getLatitude();
            longitude = locationService.getLongitude();
        }

        latitudeTextView.setText("Latitude: "+latitude);
        longitudeTextView.setText("Longitude: "+longitude);

    }

    private void updateDistance(LocationService locationService) {
        float distance=0;
        if(locationService!=null){

            if(isChronometerRunning)
            {
                int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
                distance= locationService.getSpeed()*(elapsedMillis/1000);
            }
        }

        if(this.useMetricUnits())
        {
            distanceTextView.setText("Distance: "+distance+" m");
        }
        else
        {
            distanceTextView.setText("Distance: "+distance+" ft");
        }

    }



    private boolean useMetricUnits() {
        return switchUnitMeterCompat.isChecked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1000)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                InitSpeed();
            }
            else
            {
                getActivity().getFragmentManager().popBackStack();
            }
        }
    }

    private void StartChronometer()
    {
        pauseChronometer.setText("Pause");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        isChronometerRunning=true;
    }

    private void PauseChronometer()
    {
        pausePressed=!pausePressed;
        if(pausePressed)
        {
            pauseChronometer.setText("Resume");
            chronometer.stop();
            isChronometerRunning=false;
        }
        else
        {
            pauseChronometer.setText("Pause");
            chronometer.start();
            isChronometerRunning=true;
        }
    }

    private void StopChronometer()
    {
        pauseChronometer.setText("Pause");
        chronometer.stop();
        isChronometerRunning=false;
    }

}