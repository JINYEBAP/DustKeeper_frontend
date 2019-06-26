package com.example.dustkeeper;

import com.example.dustkeeper.FragmentHome;
import com.example.dustkeeper.FragmentChallengeList;
import com.example.dustkeeper.FragmentChallengeGallery;
import com.example.dustkeeper.FragmentMileageShop;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.location.Geocoder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Fragment List
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentChallengeList fragmentChallengeList = new FragmentChallengeList();
    private FragmentChallengeGallery fragmentChallengeGallery = new FragmentChallengeGallery();
    private FragmentMileageShop fragmentMileageShop = new FragmentMileageShop();
    //위치
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private Geocoder geocoder = new Geocoder(MainActivity.this);
    public String Address;
    int flag;
    //

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_challenge_list:
                    transaction.replace(R.id.frameLayout, fragmentChallengeList).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_challenge_gallery:
                    transaction.replace(R.id.frameLayout, fragmentChallengeGallery).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_mileage_shop:
                    transaction.replace(R.id.frameLayout, fragmentMileageShop).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag=0;
        // Fragment 전환
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //위치
        // GPS 잘 작동하나 체크하는 Text
        // xml 레이아웃에 textView 하나 추가하면 확인가능
        //text = findViewById(R.id.textView);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("Main", "isGPSEnabled="+ isGPSEnabled);
        Log.d("Main", "isNetworkEnabled="+ isNetworkEnabled);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                // GPS 잘 작동하나 체크하는 Text
                //text.setText(lat+", "+lng);
                getAddressListUsingGeolocation(new GeoLocation(lat,lng));
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

        // 퍼미션 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Register the listener with the Location Manager to receive location updates

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                // 수동으로 위치 구하기
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                if (lastKnownLocation != null) {
                    double lng = lastKnownLocation.getLatitude();
                    double lat = lastKnownLocation.getLatitude();
                    //text.setText(lat+", "+lng);
                }

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                return;
            }
        }

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();

            // GPS 잘 작동하나 체크하는 Text
            //text.setText(lat+", "+lng);
            Address = new String(getAddressListUsingGeolocation(new GeoLocation(lat,lng)));
        }
        //
    }

    // 위치
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    this.finish();
                    Intent mainIntent = new Intent(this,MainActivity.class);
                    this.startActivity(mainIntent);
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
                    localBuilder.setTitle("권한 설정")
                            .setMessage("권한 거절로 인해 일부기능이 제한됩니다.")
                            .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
                                    try {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        startActivity(intent);
                                    }
                                }})
                            .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                                }})
                            .create()
                            .show();
                }
                return;
        }
    }
    public static class GeoLocation {

        double latitude;
        double longitude;

        public GeoLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public String getAddressListUsingGeolocation(GeoLocation location) {
        ArrayList<String> resultList = new ArrayList<>();
        String address[] = {""};
        String result = "";
        try {
            List<Address> list = geocoder.getFromLocation(location.latitude, location.longitude, 10);

            for (Address addr : list) {
                resultList.add(addr.toString());
            }

            address = list.get(0).getAddressLine(0).split(" ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag==0) {
            result += address[1] + " " + address[2] + " " + address[3];
            Address = new String(address[2]);
            FragmentHome FH = (FragmentHome) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            FH.setGpsLocation(Address);
            flag = 1;
        }
         return result;
    }
    //

}
