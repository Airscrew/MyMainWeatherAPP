package com.example.mymainweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.substring
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
import java.util.*


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
    private var timerow1: TextView? = null
    private var timerow2: TextView? = null
    private var timerow3: TextView? = null
    private var timerow4: TextView? = null
    private var timerow5: TextView? = null
    private var timerow6: TextView? = null
    private var timerow7: TextView? = null
    private var timerow8: TextView? = null
    private var timerow9: TextView? = null
    private var timerow10: TextView? = null
    private var timerow11: TextView? = null
    private var timerow12: TextView? = null
    private var temprow1: TextView? = null
    private var temprow2: TextView? = null
    private var temprow3: TextView? = null
    private var temprow4: TextView? = null
    private var temprow5: TextView? = null
    private var temprow6: TextView? = null
    private var temprow7: TextView? = null
    private var temprow8: TextView? = null
    private var temprow9: TextView? = null
    private var temprow10: TextView? = null
    private var temprow11: TextView? = null
    private var temprow12: TextView? = null




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
        timerow1 = findViewById(R.id.timerow1)
        timerow2 = findViewById(R.id.timerow2)
        timerow3 = findViewById(R.id.timerow3)
        timerow4 = findViewById(R.id.timerow4)
        timerow5 = findViewById(R.id.timerow5)
        timerow6 = findViewById(R.id.timerow6)
        timerow7 = findViewById(R.id.timerow7)
        timerow8 = findViewById(R.id.timerow8)
        timerow9 = findViewById(R.id.timerow9)
        timerow10 = findViewById(R.id.timerow10)
        timerow11 = findViewById(R.id.timerow11)
        timerow12 = findViewById(R.id.timerow12)
        temprow1 = findViewById(R.id.temprow1)
        temprow2 = findViewById(R.id.temprow2)
        temprow3 = findViewById(R.id.temprow3)
        temprow4 = findViewById(R.id.temprow4)
        temprow5 = findViewById(R.id.temprow5)
        temprow6 = findViewById(R.id.temprow6)
        temprow7 = findViewById(R.id.temprow7)
        temprow8 = findViewById(R.id.temprow8)
        temprow9 = findViewById(R.id.temprow9)
        temprow10 = findViewById(R.id.temprow10)
        temprow11 = findViewById(R.id.temprow11)
        temprow12 = findViewById(R.id.temprow12)





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


    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
        try {
            val netDate = Date(epoc*1000)

            return netDate.toString()
        } catch (e: Exception) {
            return e.toString()
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
                val urlTodayandOther: String = "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&exclude=current,minutely,alerts&units=metric&lang=ru&appid=$key&units=metric&lang=ru"
                val url_current: String = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$key&units=metric&lang=ru"


                doAsync {
                    val apiResponse = URL(url_current).readText()
                    val apiResponceTodayandOther = URL(urlTodayandOther).readText()
                    Log.d("Message", apiResponceTodayandOther)

                    runOnUiThread {
                        val main = JSONObject(apiResponse).getJSONObject("main")

                        val round_curr_temp = Math.round(main.getDouble("temp"))
                        val round_min_temp = Math.round(main.getDouble("temp_min"))
                        val round_max_temp = Math.round(main.getDouble("temp_max"))

                        curr_temp?.text = round_curr_temp.toString() + "℃"
                        min_temp?.text =round_min_temp.toString()+" min"
                        max_temp?.text =round_max_temp.toString()+" max"
                        atmo_press?.text =main.getString("pressure")+" hPa"
                        humidity?.text =main.getString("humidity")+" %"

                        val wind = JSONObject(apiResponse).getJSONObject("wind")
                        val round_wind_speed =Math.round(wind.getDouble("speed"))
                        wind_speed?.text =round_wind_speed.toString()+" m/s"

                        curr_city?.text = JSONObject(apiResponse).getString("name")

                        /*----------------------*/

                        val hourly = JSONObject(apiResponceTodayandOther).getJSONArray("hourly")
                        val hours_temperature : Array<Array<String>> = Array(12, { Array(2, {""}) })

                        var j = 0
                        var i = 0
                        while(i < 12){
                            val date_time = hourly.getJSONObject(i).getString("dt").toLong()
                            val round_hourly_temp = Math.round(hourly.getJSONObject(i).getDouble("temp"))
                            hours_temperature[i][j] = substring(getDateTimeFromEpocLongOfSeconds(date_time).toString(),11,16)
                            Log.d("Message", hours_temperature[i][j])
                            j=1
                            hours_temperature[i][j] =round_hourly_temp.toString()
                            Log.d("Message", hours_temperature[i][j])
                            j=0

                            i++


                        }

                        timerow1?.text = hours_temperature [0][0]
                        temprow1?.text = hours_temperature [0][1]

                        timerow2?.text = hours_temperature [1][0]
                        temprow2?.text = hours_temperature [1][1]

                        timerow3?.text = hours_temperature [2][0]
                        temprow3?.text = hours_temperature [2][1]

                        timerow4?.text = hours_temperature [3][0]
                        temprow4?.text = hours_temperature [3][1]

                        timerow5?.text = hours_temperature [4][0]
                        temprow5?.text = hours_temperature [4][1]

                        timerow6?.text = hours_temperature [5][0]
                        temprow6?.text = hours_temperature [5][1]

                        timerow7?.text = hours_temperature [6][0]
                        temprow7?.text = hours_temperature [6][1]

                        timerow8?.text = hours_temperature [7][0]
                        temprow8?.text = hours_temperature [7][1]

                        timerow9?.text = hours_temperature [8][0]
                        temprow9?.text = hours_temperature [8][1]

                        timerow10?.text = hours_temperature [9][0]
                        temprow10?.text = hours_temperature [9][1]

                        timerow11?.text = hours_temperature [10][0]
                        temprow11?.text = hours_temperature [10][1]

                        timerow12?.text = hours_temperature [11][0]
                        temprow12?.text = hours_temperature [11][1]


                       // timerow1?.text = substring(getDateTimeFromEpocLongOfSeconds(date_time).toString(),11,16)

                      //  temprow1?.text = round_hourly_temp.toString()

                       // Log.d("Message", substring(getDateTimeFromEpocLongOfSeconds(date_time).toString(),11,16))
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