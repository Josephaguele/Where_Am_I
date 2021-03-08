package com.example.whereami;
/*This app features a new Activity that finds the device's last known location using the Fused Location
* Provider from the Google Play services Location Services Library*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class WhereAmIActivity extends AppCompatActivity
{

    private static final String ERROR_MSG = "Google Play services are unavailable.";
    private TextView mTextView;

    // code to update currentLocation by listening for changes
    private LocationRequest mLocationRequest;

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

        // We update the onCreate method to create a new LocationRequest that prioritizes high accuracy
        // and has a 5-second update interval:
        mLocationRequest = new LocationRequest()
                .setInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // we create a new LocationCallback that calls the updateTextView method to update the TextView
    // whenever a new Location update is received.
    LocationCallback mLocationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult)
        {
            Location location = locationResult.getLastLocation();
            if (location !=null)
            {
                updateTextView(location);
            }
        }
    };

    // We’ll update the current location each time the app becomes visible, so we override the onStart
    //method to check for runtime permission to access fine location accuracy. Add the stub
    //method getLastLocation to call when permission is granted or rejected:

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onStart()
    {
        super.onStart();

        // Check if we have permission to access high accuracy fine location.
        int permission = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);

        // if permission is granted, fetch the last location.
        if ( permission == PERMISSION_GRANTED)
        {
            getLastLocation();
        } else
        {
            // If permission has not been granted, request permission.
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_PERMISSION_REQUEST)
        { // this means if the request is not granted on the first click by the user, the toast will
            // be location permission is denied.
            if (grantResults[0] != PERMISSION_GRANTED)
                Toast.makeText(this, "Location Permission Denied",
                        Toast.LENGTH_LONG).show();
            else
                getLastLocation();
        }
    }

    // Now update the getLastLocation stub. Get a reference to the Fused Location Provider and
    //use the getLastLocation method to find the last known location. Create a method stub
    //updateTextView that will take the returned Location and update the Text View. It’s worth
    //noting that the Location Service is capable of detecting and resolving multiple potential issues
    //with the Google Play services APK, so we don’t need to handle the connection or failure cases
    //within our code:
    private void getLastLocation()
    {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location) {
                    updateTextView(location);
                }
            });
        }
    }

    // We finally update the updateTextView method stub to extract the longitude and latitude from each
    // location and display it in the TextView:
    private void updateTextView(Location location) {
        String latLongString = "No location found";
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;
        }
        mTextView.setText(latLongString);
    }
}
