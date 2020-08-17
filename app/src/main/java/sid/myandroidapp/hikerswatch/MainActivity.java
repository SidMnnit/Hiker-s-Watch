package sid.myandroidapp.hikerswatch;

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

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //1st
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2nd
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //7th
                updateLocationInformation(location);
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
        //3rd
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //6th
            if (lastKnownLocation != null) {
                updateLocationInformation(lastKnownLocation);
            }
        }
    }

    //5th
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //6th
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            }
    }
    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    //4th
    public void updateLocationInformation(Location location){
        //8th
        TextView latTextView = findViewById(R.id.latTextView);
        TextView longTextView = findViewById(R.id.longTextView);
        TextView accTextView = findViewById(R.id.accTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);

        //9th
        latTextView.setText("Latitude: "+Double.toString(location.getLatitude()));
        longTextView.setText("Longitude: "+Double.toString(location.getLongitude()));
        accTextView.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: "+Double.toString(location.getAltitude()));

        //10th
        String address="Could not find address :(";
        Geocoder geoCoder = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> listAddress=geoCoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(listAddress!=null && listAddress.size()>0){
                address ="Address:\n";

                if(listAddress.get(0).getThoroughfare()!=null){
                    address+=listAddress.get(0).getThoroughfare() +"\n";
                }
                if(listAddress.get(0).getLocality()!=null){
                    address+=listAddress.get(0).getLocality() +" ";
                }
                if(listAddress.get(0).getSubAdminArea()!=null){
                    address+=listAddress.get(0).getThoroughfare() +" ";
                }
                if(listAddress.get(0).getPostalCode()!=null){
                    address+=listAddress.get(0).getPostalCode() +"\n";
                }
                if(listAddress.get(0).getAdminArea()!=null){
                    address+=listAddress.get(0).getAdminArea()+"\n";
                }

                if(listAddress.get(0).getCountryName()!=null){
                    address+=listAddress.get(0).getCountryName() ;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        addressTextView.setText(address);
    }
}