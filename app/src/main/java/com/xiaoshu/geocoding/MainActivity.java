package com.xiaoshu.geocoding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshu.geocodinglibrary.model.Country;
import com.xiaoshu.geocodinglibrary.utils.LogUtils;
import com.xiaoshu.geocodinglibrary.utils.ReverseGeocodingUtils;

public class MainActivity extends AppCompatActivity {
    private Button locationBtn, coordinateBtn;
    private TextView logTv;
    private EditText longitudeEt, latitudeEt;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationBtn = (Button) findViewById(R.id.location_btn);
        logTv = (TextView) findViewById(R.id.log_tv);
        coordinateBtn = (Button) findViewById(R.id.coordinate_btn);
        longitudeEt = (EditText) findViewById(R.id.longitude_et);
        latitudeEt = (EditText) findViewById(R.id.latitude_et);
        initListener();
    }

    private void initListener() {
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                try {
                    if (locationManager == null) {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    }
                    LogUtils.d(locationManager);
                    if (locationManager == null) {
                        showToast("开权限啊大哥！");
                    } else {
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location == null) {
                            showToast("还没定到位，等下再试！");
                        } else {
                            Country country = ReverseGeocodingUtils.getCountry(MainActivity.this, location);
                            if (country == null) {
                                showToast("坐标不对！");
                            } else {
                                logTv.setText("国家码：" + country.getId() + "\n 全称：" + country.getName() );
                            }
                        }
                    }
                } catch (Exception e) {
                    showToast("开权限啊大哥！" + e.toString());
                }

            }
        });

        coordinateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLongitude(Double.valueOf(longitudeEt.getText().toString()));
                    location.setLatitude(Double.valueOf(latitudeEt.getText().toString()));
                    Country country = ReverseGeocodingUtils.getCountry(MainActivity.this, location);
                    if (country == null) {
                        showToast("坐标不对！");
                    } else {
                        logTv.setText("国家码：" + country.getId() + "\n 全称：" + country.getName() );
                    }
                } catch (Exception e) {
                    showToast(e.toString());
                }
            }
        });
    }

    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
