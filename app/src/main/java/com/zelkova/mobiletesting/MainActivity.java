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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TEST_CMD = "TEST_CMD";
    public static final String TEST_LOCATION_API = "TEST LOCATION API";

    public static final int LOCATION_PERMISSION_CODE = 1;

    Button testLocationApiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testLocationApiBtn = (Button)findViewById(R.id.test_location_api_btn);
        testLocationApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestingActivity.class);
                intent.putExtra(TEST_CMD, TEST_LOCATION_API);
                startActivity(intent);
            }
        });
    }
}
