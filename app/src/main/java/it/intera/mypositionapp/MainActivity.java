package it.intera.mypositionapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import it.intera.mypositionapp.model.WeatherRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static String API_KEY = "05888a52c5bc61e43f1eff9139c0fa20";
    private OpenWeather openWeather;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView cityView;
    private TextView tempView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();
        initGui();
        initLocationClient();

    }

    private void initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }else {
            requestLocationPermissions();
        }
    }

    private void initGui() {
        cityView = findViewById(R.id.city);
        tempView = findViewById(R.id.temperature);
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        requestRetrofit(location.getLatitude(),location.getLongitude());
                    } else {
                        Log.d("TAG", "ERROR" );
                    }
                }
            });
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    private void requestRetrofit(Double lat, Double lon) {
        openWeather.loadWeather(lat, lon, "metric",API_KEY)
            .enqueue(new Callback<WeatherRequest>() {
                @Override
                public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                    String city = response.body().getName();
                    Float temp = response.body().getMain().getTemp();
                    cityView.setText(city);
                    tempView.setText(Float.toString(temp));
                }

                @Override
                public void onFailure(Call<WeatherRequest> call, Throwable t) {
                    Log.e("TAG", "ERROR", t);
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }
}
