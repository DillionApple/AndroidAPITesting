package com.zelkova.mobiletesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class TestingActivity extends AppCompatActivity {

    private TextView statusTextView;
    private TextView logTextView;
    private LocationManager locationManager;

    private int locationUpdateCount = 0;
    private static final int locationUpdateCountMax = 100;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationUpdateCount += 1;
            final String locationString = String.format(
                    "longitude: %f, latitude: %f, altitude: %f\n",
                    location.getLongitude(),
                    location.getLatitude(),
                    location.getAltitude());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    logTextView.getEditableText().insert(0, locationString);
                }
            });
            if (locationUpdateCount >= locationUpdateCountMax) {
                statusTextView.setText("Finished!");
                locationManager.removeUpdates(this);
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
    };

    private void startLocationApiTest() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.LOCATION_PERMISSION_CODE);
        } else {
            runLocationApiTest();
        }
    }

    private void runLocationApiTest() {
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            statusTextView.setText("Failed to start, please turn on location permission for this app and retry.");
            return;
        }
        statusTextView.setText("Running... ...");
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;
        float trackingDistance = 0;
        long mLocTrackingInterval = 1;
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);
        SmartLocation.with(this)
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                final String locationString = String.format(
                        "longitude: %f, latitude: %f, altitude: %f\n",
                        location.getLongitude(),
                        location.getLatitude(),
                        location.getAltitude());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logTextView.getEditableText().insert(0, locationString);
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MainActivity.LOCATION_PERMISSION_CODE:
                runLocationApiTest();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        statusTextView = (TextView) findViewById(R.id.status_text_view);
        logTextView = (TextView) findViewById(R.id.log_test_view);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        logTextView.setVerticalScrollBarEnabled(true);
        logTextView.setVerticalScrollbarPosition(1);

        statusTextView.setText("Preparing... ...");
        logTextView.setText("Waiting for logs...");
        logTextView.append("");

        Intent intent = getIntent();
        String testCmd = intent.getStringExtra(MainActivity.TEST_CMD);
        setTitle(testCmd);

        switch (testCmd) {
            case MainActivity.TEST_LOCATION_API:
                startLocationApiTest();
                break;
        }
    }
}
