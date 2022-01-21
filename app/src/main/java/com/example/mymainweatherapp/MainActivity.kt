package com.example.mymainweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.jetbrains.anko.doAsync
import java.net.URL
import android.util.Log


class MainActivity : AppCompatActivity() {


    private var curr_city: TextView? = null
    private var curr_temp: TextView? = null
    private var min_temp: TextView? = null
    private var max_temp: TextView? = null
    private var atmo_press: TextView? = null
    private var humidity: TextView? = null
    private var wind_speed: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        curr_city = findViewById(R.id.curr_city)
        curr_temp = findViewById(R.id.curr_temp)
        min_temp = findViewById(R.id.min_temp)
        max_temp = findViewById(R.id.max_temp)
        atmo_press = findViewById(R.id.atmo_press)
        humidity = findViewById(R.id.humidity)
        wind_speed = findViewById(R.id.wind_speed)

        var lat: Double = 33.44
        var lon: Double = -94.04

        val key: String = "5f05959757851e75b4bc0e978af14c8e"
        val url: String = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=minutely,hourly,daily,alerts&units=metric&lang=ru&appid=$key&units=metric&lang=ru"

        doAsync {
            val apiResponse = URL(url).readText()
            Log.d("Message", apiResponse)
        }



    }
}