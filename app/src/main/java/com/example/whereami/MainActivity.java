package com.example.whereami;
/*This app features a new Activity that finds the device's last known location using the Fused Location
* Provider from the Google Play services Location Services Library*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
{

    private static final String ERROR_MSG = "Google Play services are unavailable.";
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.myLocationText);

        // Confirm that google Play services are ( or could be ) available on this device, and
        // obtain a reference to the TextView from the Layout.
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int result = availability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS)
        {
            if (!availability.isUserResolvableError(result))
            {
                Toast.makeText(this,ERROR_MSG, Toast.LENGTH_LONG).show();
            }
        }
    }

    // We’ll update the current location each time the app becomes visible, so we override the onStart
    //method to check for runtime permission to access fine location accuracy. Add the stub
    //method getLastLocation to call when permission is granted or rejected:

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onStart()
    {
        super.onStart();

        // Check if we have permission to access high accuracy fine location.
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // if permission is granted, fetch the last location.
        if ( permission == PERMISSION_GRANTED)
        {
            getLastLocation();
        } else
        {
            // If permission has not been granted, request permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_PERMISSION_REQUEST)
        {
            if (grantResults[0] != PERMISSION_GRANTED)
                Toast.makeText(this, "Location Permission Denied",
                        Toast.LENGTH_LONG).show();
            else
                getLastLocation();
        }
    }

    private void getLastLocation(){}
}
