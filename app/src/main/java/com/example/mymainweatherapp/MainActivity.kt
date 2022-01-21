package com.example.mymainweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.jetbrains.anko.doAsync
import java.net.URL
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.jetbrains.anko.find
import org.json.JSONObject



class MainActivity : AppCompatActivity() {













    private lateinit var button: Button
    private var curr_city: TextView? = null
    private var curr_temp: TextView? = null
    private var min_temp: TextView? = null
    private var max_temp: TextView? = null
    private var atmo_press: TextView? = null
    private var humidity: TextView? = null
    private var wind_speed: TextView? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onStart() {
        super.onStart()
        checkpermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        button = findViewById(R.id.button)
        curr_city = findViewById(R.id.curr_city)
        curr_temp = findViewById(R.id.curr_temp)
        min_temp = findViewById(R.id.min_temp)
        max_temp = findViewById(R.id.max_temp)
        atmo_press = findViewById(R.id.atmo_press)
        humidity = findViewById(R.id.humidity)
        wind_speed = findViewById(R.id.wind_speed)





        button.setOnClickListener {
            //checkpermissions()
        }
    }

    private fun checkpermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1 )
        }else {
            getlocations()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getlocations() {

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            if (it == null){
                Toast.makeText(this,"Невозможно определить геопозицию", Toast.LENGTH_SHORT).show()
            } else it.apply {
                val latitude = it.latitude
                val longitude = it.longitude
                val key: String = "5f05959757851e75b4bc0e978af14c8e"
                val url: String = "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&exclude=minutely,hourly,daily,alerts&units=metric&lang=ru&appid=$key&units=metric&lang=ru"
                val url_current: String = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$key&units=metric&lang=ru"


                doAsync {
                    val apiResponse = URL(url_current).readText()
                    Log.d("Message", apiResponse)

                    runOnUiThread {
                        val main = JSONObject(apiResponse).getJSONObject("main")

                        val round_curr_temp = main.getDouble("temp").toInt()
                        val round_min_temp = main.getDouble("temp_min").toInt()
                        val round_max_temp =main.getDouble("temp_max").toInt()

                        curr_temp?.text = round_curr_temp.toString() + "℃"
                        min_temp?.text =round_min_temp.toString()+" min"
                        max_temp?.text =round_max_temp.toString()+" max"
                        atmo_press?.text =main.getString("pressure")+" hPa"
                        humidity?.text =main.getString("humidity")+" %"

                        val wind = JSONObject(apiResponse).getJSONObject("wind")
                        val round_wind_speed =wind.getDouble("speed").toInt()
                        wind_speed?.text =round_wind_speed.toString()+" m/s"

                        curr_city?.text = JSONObject(apiResponse).getString("name")
                    }





                }




            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode ==1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show()
                    getlocations()
                }
                else {
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}