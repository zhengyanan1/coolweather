package com.study.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.study.coolweather.R;
import com.study.coolweather.util.HttpCallbackListener;
import com.study.coolweather.util.HttpUtil;
import com.study.coolweather.util.Utility;

import org.w3c.dom.Text;

/**
 * Created by ASUS on 2016/5/10.
 */
public class WeatherActivity extends Activity {
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText=(TextView)findViewById(R.id.city_name);
        publishText=(TextView)findViewById(R.id.publish_text);
        weatherDespText=(TextView)findViewById(R.id.weather_desp);
        temp1Text=(TextView)findViewById(R.id.temp1);
        temp2Text=(TextView)findViewById(R.id.temp2);
        currentDateText=(TextView)findViewById(R.id.current_date);
        String countyCode=getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else {
            showWeather();
        }

    }

    private void queryWeatherCode(String  countyCode){
        String address="http://www.weather.com.cn/data/list3/city" +
                countyCode+".xml";
        queryFromServer(address,"countyCode");
    }
    private void queryWeatherInfo(String weatherCode){
        String address="http://www.weather.com.cn/data/cityinfo/" +
                weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }
    private void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if("countyCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        String [] array=response.split("\\|");
                        if(array!=null&&2==array.length){
                            String weatherCode=array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }
                else if("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDateText.setText(prefs.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
