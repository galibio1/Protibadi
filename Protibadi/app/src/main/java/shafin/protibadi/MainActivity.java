package shafin.protibadi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class MainActivity extends SettingActivity implements LocationListener {


    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private  Button mSendMessageBtn;
    Button getLocationBtn,setting;
    TextView locationText;
    double l;
    double n;
    String a;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final EditText mPhoneNoEt = (EditText) findViewById(R.id.et_phone_no);
        //final EditText messagetEt = (EditText) findViewById(R.id.et_message);
        mSendMessageBtn = (Button) findViewById(R.id.btn_send_message);


        mSendMessageBtn.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)) {
            mSendMessageBtn.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }
        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "My Location is : ("+"http://www.google.com/maps/place/" + l +","+ n+")";
                String phoneNo = getPhone();
                //Toast.makeText(getApplicationContext(), "This is my Toast message!\n"+a, Toast.LENGTH_LONG).show();

                if(!TextUtils.isEmpty(message) && !TextUtils.isEmpty(phoneNo)) {

                    if(checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, getName()+","+getSms()+"\n"+message, null, null);
                    }else {
                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getLocationBtn = (Button)findViewById(R.id.getLocationBtn);

        locationText = (TextView)findViewById(R.id.locationText);

        setting=(Button)findViewById(R.id.setting);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        getLocation();
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                Toast.makeText(getApplicationContext(), "This is my Toast message!\n"+a, Toast.LENGTH_LONG).show();
            }

        });




        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);

            }
        });




    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mSendMessageBtn.setEnabled(true);
                }
                return;
            }
        }
    }




    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        l=location.getLatitude();
        n=location.getLongitude();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
            a=locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2);
        }catch(Exception e)
        {

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}