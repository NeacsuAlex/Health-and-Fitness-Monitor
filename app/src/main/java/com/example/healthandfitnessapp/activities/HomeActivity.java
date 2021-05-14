package com.example.healthandfitnessapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthandfitnessapp.R;
import com.example.healthandfitnessapp.services.LocationService;

import java.util.Formatter;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements LocationListener {

    SwitchCompat switchUnitMeterCompat;
    TextView speedTextView,latitudeTextView,longitudeTextView, distanceTextView;
    Chronometer chronometer;
    Button startChronometer, pauseChronometer, stopChronometer;
    boolean isChronometerRunning, pausePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        switchUnitMeterCompat=findViewById(R.id.switch_unit_meter);
        speedTextView=findViewById(R.id.speed_text);
        latitudeTextView=findViewById(R.id.latitude_text);
        longitudeTextView=findViewById(R.id.longitude_text);
        distanceTextView=findViewById(R.id.distance_text);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        startChronometer=(Button)findViewById(R.id.start_timer);
        pauseChronometer=(Button)findViewById(R.id.pause_timer);
        stopChronometer=(Button)findViewById(R.id.stop_timer);

        isChronometerRunning=false;
        pausePressed=false;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&& checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
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
                HomeActivity.this.updateSpeed(null);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @SuppressLint("MissingPermission")
    private void InitSpeed()
    {
        LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this, "Waiting for GPS connection...", Toast.LENGTH_SHORT).show();
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
                finish();
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