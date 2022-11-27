package com.khamill.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView HikerstextView, longtextView, lattextView, acctextView, alttextView, addtextView;

    public void updateLocation (Location location) throws IOException {
        Log.i("Location Info", location.toString());
        longtextView = (TextView) findViewById(R.id.LongtextView);
        lattextView = (TextView) findViewById(R.id.LattextView);
        acctextView = (TextView) findViewById(R.id.acctextView);
        alttextView = (TextView) findViewById(R.id.alttextView);
        addtextView = (TextView) findViewById(R.id.addtextView);

        String address = "could not find your address";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        if (listAddress != null && listAddress.size() > 0) {
            //this would print to the logcat the address, which comprises of many stuff
            Log.i("PlaceInfo:", listAddress.get(0).toString());
            //this gets the address subthroughfare
            address = "Address \n";
            if (listAddress.get(0).getSubThoroughfare() != null) {
                address += listAddress.get(0).getSubThoroughfare() + "";
            }
            if (listAddress.get(0).getThoroughfare() != null) {
                address += listAddress.get(0).getThoroughfare() + "\n ";
            }
            if (listAddress.get(0).getLocality() != null) {
                address += listAddress.get(0).getLocality() + "\n";
            }
            if (listAddress.get(0).getPostalCode() != null) {
                address += listAddress.get(0).getPostalCode() + "\n ";
            }
            if (listAddress.get(0).getCountryName() != null) {
                address += listAddress.get(0).getCountryName();
            }

                addtextView.setText(address);

            longtextView.setText("Longitude " + location.getLongitude());
            lattextView.setText("Latitude: " + location.getLatitude());
            acctextView.setText("Accuracy: " + location.getAccuracy());
            alttextView.setText("Altitude: " + location.getAltitude());

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                updateLocation(lastlocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                try {
                    updateLocation(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            }

        }

    }
}