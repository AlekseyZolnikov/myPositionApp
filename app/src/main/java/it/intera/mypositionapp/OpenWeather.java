package it.intera.mypositionapp;


import it.intera.mypositionapp.model.WeatherRequest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("units") String units,
            @Query("appid") String keyApi
    );
}
