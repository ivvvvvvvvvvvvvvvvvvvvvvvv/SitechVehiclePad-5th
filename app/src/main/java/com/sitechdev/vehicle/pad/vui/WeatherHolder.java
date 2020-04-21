package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sitechdev.vehicle.pad.R;

import org.json.JSONObject;

/**
 * @author zhubaoqiang
 * @date 2019/8/19
 */
public class WeatherHolder extends VUIHolder{

    private ImageView weatherImg;
    private TextView tempReal;
    private TextView tempRange;
    private TextView wind;
    private TextView location;

    public WeatherHolder(@NonNull Context context){
        super(context, R.layout.holder_weather);
    }

    public WeatherHolder(@NonNull LayoutInflater inflater){
        super(inflater, R.layout.holder_weather);
    }

    @Override
    protected void getView() {
        weatherImg = findViewById(R.id.weather_holder_img);
        tempReal = findViewById(R.id.weather_holder_tempReal);
        tempRange = findViewById(R.id.weather_holder_tempRange);
        wind = findViewById(R.id.weather_holder_wind);
        location = findViewById(R.id.weather_holder_location);
    }

    public void adapter(JSONObject weather){
        Glide.with(weatherImg)
            .load(weather.optString("img"))
            .placeholder(R.mipmap.ic_glide_placeholder)
            .into(weatherImg);
        tempReal.setText(weather.optString("tempReal"));
        tempRange.setText(weather.optString("tempRange"));
        wind.setText(weather.optString("wind"));
        location.setText(weather.optString("city"));
    }
}
