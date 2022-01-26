package com.example.mymainweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils.substring
import android.widget.TextView
import org.jetbrains.anko.doAsync
import java.net.URL
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private var curr_city: TextView? = null
    private var curr_date: TextView? = null
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

    private var  curr_icon: ImageView? = null

    private var  imageViewRow1: ImageView? = null
    private var  imageViewRow2: ImageView? = null
    private var  imageViewRow3: ImageView? = null
    private var  imageViewRow4: ImageView? = null
    private var  imageViewRow5: ImageView? = null
    private var  imageViewRow6: ImageView? = null
    private var  imageViewRow7: ImageView? = null
    private var  imageViewRow8: ImageView? = null
    private var  imageViewRow9: ImageView? = null
    private var  imageViewRow10: ImageView? = null
    private var  imageViewRow11: ImageView? = null
    private var  imageViewRow12: ImageView? = null

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

    private var day1_tview: TextView? = null
    private var day2_tview: TextView? = null
    private var day3_tview: TextView? = null
    private var day4_tview: TextView? = null
    private var day5_tview: TextView? = null
    private var day6_tview: TextView? = null
    private var day7_tview: TextView? = null

    private var wind_deg_image1: ImageView? = null
    private var wind_deg_image2: ImageView? = null
    private var wind_deg_image3: ImageView? = null
    private var wind_deg_image4: ImageView? = null
    private var wind_deg_image5: ImageView? = null
    private var wind_deg_image6: ImageView? = null
    private var wind_deg_image7: ImageView? = null
    private var wind_deg_image8: ImageView? = null
    private var wind_deg_image9: ImageView? = null
    private var wind_deg_image10: ImageView? = null
    private var wind_deg_image11: ImageView? = null
    private var wind_deg_image12: ImageView? = null



    private var day1_mintemp: TextView? = null
    private var day2_mintemp: TextView? = null
    private var day3_mintemp: TextView? = null
    private var day4_mintemp: TextView? = null
    private var day5_mintemp: TextView? = null
    private var day6_mintemp: TextView? = null
    private var day7_mintemp: TextView? = null

    private var day1_maxtemp: TextView? = null
    private var day2_maxtemp: TextView? = null
    private var day3_maxtemp: TextView? = null
    private var day4_maxtemp: TextView? = null
    private var day5_maxtemp: TextView? = null
    private var day6_maxtemp: TextView? = null
    private var day7_maxtemp: TextView? = null

    private var day1_imageView: ImageView? = null
    private var day2_imageView: ImageView? = null
    private var day3_imageView: ImageView? = null
    private var day4_imageView: ImageView? = null
    private var day5_imageView: ImageView? = null
    private var day6_imageView: ImageView? = null
    private var day7_imageView: ImageView? = null

    private var state: String = "current"

    private var C: String = "°"


//Cоздание меню для смены города
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //R.id.menu_change_city ->
        val intent = Intent(this, SetCity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

//получение координат города со второго окна приложения
    private fun getnewcity() : String {
        val latlng = intent.getStringExtra("latlng")
        return latlng.toString()
    }

//проверка- если город былыл выбран, то меняем state  (просто делаем пометку о том что нужно использовать новые координаты вместо текущей геолокации)
    private fun checkstate() {
        val latlng = intent.getStringExtra("latlng")
        if (latlng != null) {
            state = "new_location"
        }
    }

    override fun onStart() {
        super.onStart()
        checkstate()
        checkpermissions()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        curr_city = findViewById(R.id.curr_city)
        curr_date = findViewById(R.id.curr_date)
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

        curr_icon = findViewById(R.id.curr_icon)

        imageViewRow1 = findViewById(R.id.imageViewRow1)
        imageViewRow2 = findViewById(R.id.imageViewRow2)
        imageViewRow3 = findViewById(R.id.imageViewRow3)
        imageViewRow4 = findViewById(R.id.imageViewRow4)
        imageViewRow5 = findViewById(R.id.imageViewRow5)
        imageViewRow6 = findViewById(R.id.imageViewRow6)
        imageViewRow7 = findViewById(R.id.imageViewRow7)
        imageViewRow8 = findViewById(R.id.imageViewRow8)
        imageViewRow9 = findViewById(R.id.imageViewRow9)
        imageViewRow10 = findViewById(R.id.imageViewRow10)
        imageViewRow11 = findViewById(R.id.imageViewRow11)
        imageViewRow12 = findViewById(R.id.imageViewRow12)

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

        day1_tview = findViewById(R.id.day1_tview)
        day2_tview = findViewById(R.id.day2_tview)
        day3_tview = findViewById(R.id.day3_tview)
        day4_tview = findViewById(R.id.day4_tview)
        day5_tview = findViewById(R.id.day5_tview)
        day6_tview = findViewById(R.id.day6_tview)
        day7_tview = findViewById(R.id.day7_tview)


        wind_deg_image1 = findViewById(R.id.wind_deg_image1)
        wind_deg_image2 = findViewById(R.id.wind_deg_image2)
        wind_deg_image3 = findViewById(R.id.wind_deg_image3)
        wind_deg_image4 = findViewById(R.id.wind_deg_image4)
        wind_deg_image5 = findViewById(R.id.wind_deg_image5)
        wind_deg_image6 = findViewById(R.id.wind_deg_image6)
        wind_deg_image7 = findViewById(R.id.wind_deg_image7)
        wind_deg_image8 = findViewById(R.id.wind_deg_image8)
        wind_deg_image9 = findViewById(R.id.wind_deg_image9)
        wind_deg_image10 = findViewById(R.id.wind_deg_image10)
        wind_deg_image11 = findViewById(R.id.wind_deg_image11)
        wind_deg_image12 = findViewById(R.id.wind_deg_image12)



        day1_mintemp = findViewById(R.id.day1_mintemp)
        day2_mintemp = findViewById(R.id.day2_mintemp)
        day3_mintemp = findViewById(R.id.day3_mintemp)
        day4_mintemp = findViewById(R.id.day4_mintemp)
        day5_mintemp = findViewById(R.id.day5_mintemp)
        day6_mintemp = findViewById(R.id.day6_mintemp)
        day7_mintemp = findViewById(R.id.day7_mintemp)

        day1_maxtemp = findViewById(R.id.day1_maxtemp)
        day2_maxtemp = findViewById(R.id.day2_maxtemp)
        day3_maxtemp = findViewById(R.id.day3_maxtemp)
        day4_maxtemp = findViewById(R.id.day4_maxtemp)
        day5_maxtemp = findViewById(R.id.day5_maxtemp)
        day6_maxtemp = findViewById(R.id.day6_maxtemp)
        day7_maxtemp = findViewById(R.id.day7_maxtemp)

        day1_imageView = findViewById(R.id.day1_imageView)
        day2_imageView = findViewById(R.id.day2_imageView)
        day3_imageView = findViewById(R.id.day3_imageView)
        day4_imageView = findViewById(R.id.day4_imageView)
        day5_imageView = findViewById(R.id.day5_imageView)
        day6_imageView = findViewById(R.id.day6_imageView)
        day7_imageView = findViewById(R.id.day7_imageView)


    }


//проверка разрешений на доступ к геолокации
    private fun checkpermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1 )
        }else {

            getlocations(state)
        }
    }

//преобразование unix штампа в дату время
    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String {
        try {
            val netDate = Date(epoc*1000)
            return netDate.toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }


//закгрузка соответствующих погодных иконок в разбивку по дням
    private fun setimage_daily(icon_code: String, i: Int) {

        val urlImage = URL("https://openweathermap.org/img/wn/$icon_code@2x.png")
        // async task to get bitmap from url
        val result: Deferred<Bitmap?> = lifecycleScope.async(Dispatchers.IO) {
            urlImage.toBitmap
        }

        lifecycleScope.launch(Dispatchers.Main) {
            // show bitmap on image view when available
            if (i==1){day1_imageView?.setImageBitmap(result.await())}
            if (i==2){day2_imageView?.setImageBitmap(result.await())}
            if (i==3){day3_imageView?.setImageBitmap(result.await())}
            if (i==4){day4_imageView?.setImageBitmap(result.await())}
            if (i==5){day5_imageView?.setImageBitmap(result.await())}
            if (i==6){day6_imageView?.setImageBitmap(result.await())}
            if (i==7){day7_imageView?.setImageBitmap(result.await())}
        }
    }

//закгрузка соответствующих погодных иконок в разбивку по часам
    private fun setimage_hourly(icon_code: String, i: Int) {

        val urlImage = URL("https://openweathermap.org/img/wn/$icon_code@2x.png")
        // async task to get bitmap from url
        val result: Deferred<Bitmap?> = lifecycleScope.async(Dispatchers.IO) {
            urlImage.toBitmap
        }

        lifecycleScope.launch(Dispatchers.Main) {
            // show bitmap on image view when available
            if (i==0){
                imageViewRow1?.setImageBitmap(result.await())
                curr_icon?.setImageBitmap(result.await())
            }
            if (i==1){imageViewRow2?.setImageBitmap(result.await())}
            if (i==2){imageViewRow3?.setImageBitmap(result.await())}
            if (i==3){imageViewRow4?.setImageBitmap(result.await())}
            if (i==4){imageViewRow5?.setImageBitmap(result.await())}
            if (i==5){imageViewRow6?.setImageBitmap(result.await())}
            if (i==6){imageViewRow7?.setImageBitmap(result.await())}
            if (i==7){imageViewRow8?.setImageBitmap(result.await())}
            if (i==8){imageViewRow9?.setImageBitmap(result.await())}
            if (i==9){imageViewRow10?.setImageBitmap(result.await())}
            if (i==10){imageViewRow11?.setImageBitmap(result.await())}
            if (i==11){imageViewRow12?.setImageBitmap(result.await())}
        }
    }


    //закгрузка соответствующих погодных иконок направление ветра
    private fun setimage_wind_deg(wind_deg: Double, i: Int) {


        var uri: Uri? = null

        if((wind_deg > 337.5)||( wind_deg <= 22.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.n}")
        } else  if ((wind_deg > 22.5)&&(wind_deg <= 67.5)) {
            uri = Uri.parse("android.resource://$packageName/${R.drawable.ne}")
        } else if ((wind_deg > 67.5)&&(wind_deg <= 112.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.e}")
        }else if ((wind_deg > 112.5)&&(wind_deg <= 157.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.se}")
        } else if ((wind_deg > 157.5)&&(wind_deg <= 202.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.s}")
        } else if ((wind_deg > 202.5)&&(wind_deg <= 247.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.sw}")
        } else if ((wind_deg > 247.5)&&(wind_deg <= 292.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.w}")
        } else if ((wind_deg > 292.5)&&(wind_deg <= 337.5)){
            uri = Uri.parse("android.resource://$packageName/${R.drawable.nw}")
        }


        if (i==0){ wind_deg_image1?.setImageURI(uri)}
        if (i==1){ wind_deg_image2?.setImageURI(uri)}
        if (i==2){ wind_deg_image3?.setImageURI(uri)}
        if (i==3){ wind_deg_image4?.setImageURI(uri)}
        if (i==4){ wind_deg_image5?.setImageURI(uri)}
        if (i==5){ wind_deg_image6?.setImageURI(uri)}
        if (i==6){ wind_deg_image7?.setImageURI(uri)}
        if (i==7){ wind_deg_image8?.setImageURI(uri)}
        if (i==8){ wind_deg_image9?.setImageURI(uri)}
        if (i==9){ wind_deg_image10?.setImageURI(uri)}
        if (i==10){ wind_deg_image11?.setImageURI(uri)}
        if (i==11){ wind_deg_image12?.setImageURI(uri)}

    }





    //оппеделяем координаты и выводим данные ->
    @SuppressLint("MissingPermission")
    private fun getlocations(state:String) {
        val key = "5f05959757851e75b4bc0e978af14c8e"
        var urlTodayandOther: String
        var url_current: String

        if (state == "new_location") {
            val newlatlng = getnewcity()
            var latitude =
                newlatlng.substring(newlatlng.indexOf("(") + 1, newlatlng.indexOf(",")).toDouble()
            var longitude =
                newlatlng.substring(newlatlng.indexOf(",") + 1, newlatlng.indexOf(")")).toDouble()

            Log.d("Message1", "$state $latitude $longitude")
            urlTodayandOther = "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&exclude=current,minutely,alerts&units=metric&lang=en&appid=$key"
            url_current = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$key&units=metric&lang=en"

            doAsync {
                val apiResponse = URL(url_current).readText()
                val apiResponceTodayandOther = URL(urlTodayandOther).readText()
                Log.d("Message", apiResponse)
                Log.d("Message", apiResponceTodayandOther)


                runOnUiThread {

                    /*------------current----------*/

                    val main = JSONObject(apiResponse).getJSONObject("main")

                    val round_curr_temp = main.getDouble("temp").roundToInt()
                    val round_min_temp = main.getDouble("temp_min").roundToInt()
                    val round_max_temp = main.getDouble("temp_max").roundToInt()

                    curr_date?.text = substring(Date().toString(),0,16)
                    curr_temp?.text = "$round_curr_temp℃"
                    min_temp?.text = "$round_min_temp° min"
                    max_temp?.text = "$round_max_temp° max"
                    atmo_press?.text =main.getString("pressure")+" hPa"
                    humidity?.text =main.getString("humidity")+" %"

                    val wind = JSONObject(apiResponse).getJSONObject("wind")
                    val round_wind_speed = wind.getDouble("speed").roundToInt()
                    wind_speed?.text = "$round_wind_speed m/s"

                    curr_city?.text = JSONObject(apiResponse).getString("name")


                    /*------------hourly----------*/

                    val hourly = JSONObject(apiResponceTodayandOther).getJSONArray("hourly")
                    var k = 0
                    var hourly_weather_ico_code : String
                    var wind_deg : Double
                    while(k<12) {
                        hourly_weather_ico_code = hourly.getJSONObject(k).getJSONArray("weather").getJSONObject(0).getString("icon")
                        setimage_hourly(hourly_weather_ico_code, k)
                        wind_deg = hourly.getJSONObject(k).getString("wind_deg").toDouble()
                        setimage_wind_deg(wind_deg, k)
                        //Log.d("Message wind-deg", wind_deg.toString())

                        k++
                    }

                    //Log.d("Message2", hourly_weather_ico_code)

                    val hours_temperature : Array<Array<String>> = Array(12, { Array(2, {""}) })

                    var j = 0
                    var i = 0
                    while(i < 12){
                        val date_time = hourly.getJSONObject(i).getString("dt").toLong()
                        val round_hourly_temp = hourly.getJSONObject(i).getDouble("temp").roundToInt()
                        hours_temperature[i][j] = substring(getDateTimeFromEpocLongOfSeconds(date_time),11,16)
                        //Log.d("Message", hours_temperature[i][j])
                        j=1
                        hours_temperature[i][j] =round_hourly_temp.toString()
                        //Log.d("Message", hours_temperature[i][j])
                        j=0
                        i++
                    }






                    timerow1?.text = hours_temperature [0][0]
                    temprow1?.text = hours_temperature [0][1]+C

                    timerow2?.text = hours_temperature [1][0]
                    temprow2?.text = hours_temperature [1][1]+C

                    timerow3?.text = hours_temperature [2][0]
                    temprow3?.text = hours_temperature [2][1]+C

                    timerow4?.text = hours_temperature [3][0]
                    temprow4?.text = hours_temperature [3][1]+C

                    timerow5?.text = hours_temperature [4][0]
                    temprow5?.text = hours_temperature [4][1]+C

                    timerow6?.text = hours_temperature [5][0]
                    temprow6?.text = hours_temperature [5][1]+C

                    timerow7?.text = hours_temperature [6][0]
                    temprow7?.text = hours_temperature [6][1]+C

                    timerow8?.text = hours_temperature [7][0]
                    temprow8?.text = hours_temperature [7][1]+C

                    timerow9?.text = hours_temperature [8][0]
                    temprow9?.text = hours_temperature [8][1]+C

                    timerow10?.text = hours_temperature [9][0]
                    temprow10?.text = hours_temperature [9][1]+C

                    timerow11?.text = hours_temperature [10][0]
                    temprow11?.text = hours_temperature [10][1]+C

                    timerow12?.text = hours_temperature [11][0]
                    temprow12?.text = hours_temperature [11][1]+C



                    /*-----------daily-----------*/

                    val daily = JSONObject(apiResponceTodayandOther).getJSONArray("daily")

                    var n = 1
                    var weather_ico_code_array : String
                    while(n<8) {
                        weather_ico_code_array = daily.getJSONObject(n).getJSONArray("weather").getJSONObject(0).getString("icon")
                        setimage_daily(weather_ico_code_array, n)
                        n++
                    }


                    day1_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(1).getString("dt").toLong()),0,10)
                    day1_mintemp?.text = daily.getJSONObject(1).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day1_maxtemp?.text = daily.getJSONObject(1).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                    day2_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(2).getString("dt").toLong()),0,10)
                    day2_mintemp?.text = daily.getJSONObject(2).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day2_maxtemp?.text = daily.getJSONObject(2).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                    day3_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(3).getString("dt").toLong()),0,10)
                    day3_mintemp?.text = daily.getJSONObject(3).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day3_maxtemp?.text = daily.getJSONObject(3).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                    day4_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(4).getString("dt").toLong()),0,10)
                    day4_mintemp?.text = daily.getJSONObject(4).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day4_maxtemp?.text = daily.getJSONObject(4).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                    day5_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(5).getString("dt").toLong()),0,10)
                    day5_mintemp?.text = daily.getJSONObject(5).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day5_maxtemp?.text = daily.getJSONObject(5).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                    day6_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(6).getString("dt").toLong()),0,10)
                    day6_mintemp?.text = daily.getJSONObject(6).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day6_maxtemp?.text = daily.getJSONObject(6).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                    day7_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(7).getString("dt").toLong()),0,10)
                    day7_mintemp?.text = daily.getJSONObject(7).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                    day7_maxtemp?.text = daily.getJSONObject(7).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                }
            }






            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {


            if (it == null){
                val intent = Intent(this, SetCity::class.java)
                startActivity(intent)
                Toast.makeText(this,"Невозможно определить геопозицию", Toast.LENGTH_SHORT).show()

            } else it.apply {
//если выбран какой-то город из списка, то получаем координаты города выбранного ранее (getnewcity())
                if (state != "current"){
                    val newlatlng = getnewcity()
                        latitude = newlatlng.substring(newlatlng.indexOf("(")+1,newlatlng.indexOf(",")).toDouble()
                        longitude = newlatlng.substring(newlatlng.indexOf(",")+1,newlatlng.indexOf(")")).toDouble()

                        Log.d("Message1", "$state $latitude $longitude")
                        urlTodayandOther = "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&exclude=current,minutely,alerts&units=metric&lang=en&appid=$key"
                        url_current = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$key&units=metric&lang=en"

                }
//если город не выбирался через список то используем координаты текущего местоположения
                else {
                    val latitude = it.latitude.toString()
                    val longitude = it.longitude.toString()

                        Log.d("Message2", "$state $latitude $longitude")
                        urlTodayandOther = "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&exclude=current,minutely,alerts&units=metric&lang=en&appid=$key"
                        url_current = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$key&units=metric&lang=en"
                }

//отправляем url запросы и обрабатываем ответы
                doAsync {
                    val apiResponse = URL(url_current).readText()
                    val apiResponceTodayandOther = URL(urlTodayandOther).readText()
                    Log.d("Message", apiResponse)
                    Log.d("Message", apiResponceTodayandOther)


                    runOnUiThread {

                        /*------------current----------*/

                        val main = JSONObject(apiResponse).getJSONObject("main")

                        val round_curr_temp = main.getDouble("temp").roundToInt()
                        val round_min_temp = main.getDouble("temp_min").roundToInt()
                        val round_max_temp = main.getDouble("temp_max").roundToInt()

                        curr_date?.text = substring(Date().toString(),0,16)
                        curr_temp?.text = "$round_curr_temp℃"
                        min_temp?.text = "$round_min_temp° min"
                        max_temp?.text = "$round_max_temp° max"
                        atmo_press?.text =main.getString("pressure")+" hPa"
                        humidity?.text =main.getString("humidity")+" %"

                        val wind = JSONObject(apiResponse).getJSONObject("wind")
                        val round_wind_speed = wind.getDouble("speed").roundToInt()
                        wind_speed?.text = "$round_wind_speed m/s"

                        curr_city?.text = JSONObject(apiResponse).getString("name")


                        /*------------hourly----------*/

                        val hourly = JSONObject(apiResponceTodayandOther).getJSONArray("hourly")
                        var k = 0
                        var hourly_weather_ico_code : String
                        var wind_deg : Double
                        while(k<12) {
                            hourly_weather_ico_code = hourly.getJSONObject(k).getJSONArray("weather").getJSONObject(0).getString("icon")
                            setimage_hourly(hourly_weather_ico_code, k)
                            wind_deg = hourly.getJSONObject(k).getString("wind_deg").toDouble()
                            setimage_wind_deg(wind_deg, k)
                            //Log.d("Message wind-deg", wind_deg.toString())

                            k++
                        }

                        //Log.d("Message2", hourly_weather_ico_code)

                        val hours_temperature : Array<Array<String>> = Array(12, { Array(2, {""}) })

                        var j = 0
                        var i = 0
                        while(i < 12){
                            val date_time = hourly.getJSONObject(i).getString("dt").toLong()
                            val round_hourly_temp = hourly.getJSONObject(i).getDouble("temp").roundToInt()
                            hours_temperature[i][j] = substring(getDateTimeFromEpocLongOfSeconds(date_time),11,16)
                            //Log.d("Message", hours_temperature[i][j])
                            j=1
                            hours_temperature[i][j] =round_hourly_temp.toString()
                            //Log.d("Message", hours_temperature[i][j])
                            j=0
                            i++
                        }






                        timerow1?.text = hours_temperature [0][0]
                        temprow1?.text = hours_temperature [0][1]+C

                        timerow2?.text = hours_temperature [1][0]
                        temprow2?.text = hours_temperature [1][1]+C

                        timerow3?.text = hours_temperature [2][0]
                        temprow3?.text = hours_temperature [2][1]+C

                        timerow4?.text = hours_temperature [3][0]
                        temprow4?.text = hours_temperature [3][1]+C

                        timerow5?.text = hours_temperature [4][0]
                        temprow5?.text = hours_temperature [4][1]+C

                        timerow6?.text = hours_temperature [5][0]
                        temprow6?.text = hours_temperature [5][1]+C

                        timerow7?.text = hours_temperature [6][0]
                        temprow7?.text = hours_temperature [6][1]+C

                        timerow8?.text = hours_temperature [7][0]
                        temprow8?.text = hours_temperature [7][1]+C

                        timerow9?.text = hours_temperature [8][0]
                        temprow9?.text = hours_temperature [8][1]+C

                        timerow10?.text = hours_temperature [9][0]
                        temprow10?.text = hours_temperature [9][1]+C

                        timerow11?.text = hours_temperature [10][0]
                        temprow11?.text = hours_temperature [10][1]+C

                        timerow12?.text = hours_temperature [11][0]
                        temprow12?.text = hours_temperature [11][1]+C



                        /*-----------daily-----------*/

                        val daily = JSONObject(apiResponceTodayandOther).getJSONArray("daily")

                        var n = 1
                        var weather_ico_code_array : String
                        while(n<8) {
                            weather_ico_code_array = daily.getJSONObject(n).getJSONArray("weather").getJSONObject(0).getString("icon")
                            setimage_daily(weather_ico_code_array, n)
                            n++
                        }


                        day1_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(1).getString("dt").toLong()),0,10)
                        day1_mintemp?.text = daily.getJSONObject(1).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day1_maxtemp?.text = daily.getJSONObject(1).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                        day2_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(2).getString("dt").toLong()),0,10)
                        day2_mintemp?.text = daily.getJSONObject(2).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day2_maxtemp?.text = daily.getJSONObject(2).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                        day3_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(3).getString("dt").toLong()),0,10)
                        day3_mintemp?.text = daily.getJSONObject(3).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day3_maxtemp?.text = daily.getJSONObject(3).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                        day4_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(4).getString("dt").toLong()),0,10)
                        day4_mintemp?.text = daily.getJSONObject(4).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day4_maxtemp?.text = daily.getJSONObject(4).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                        day5_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(5).getString("dt").toLong()),0,10)
                        day5_mintemp?.text = daily.getJSONObject(5).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day5_maxtemp?.text = daily.getJSONObject(5).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                        day6_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(6).getString("dt").toLong()),0,10)
                        day6_mintemp?.text = daily.getJSONObject(6).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day6_maxtemp?.text = daily.getJSONObject(6).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

                        day7_tview?.text = substring(getDateTimeFromEpocLongOfSeconds(daily.getJSONObject(7).getString("dt").toLong()),0,10)
                        day7_mintemp?.text = daily.getJSONObject(7).getJSONObject("temp").getDouble("min").roundToInt().toString()+C
                        day7_maxtemp?.text = daily.getJSONObject(7).getJSONObject("temp").getDouble("max").roundToInt().toString()+C

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
                    getlocations(state)
                }
                else {
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


val URL.toBitmap:Bitmap?
    get() {
        return try {
            BitmapFactory.decodeStream(openStream())
        }catch (e: IOException){null}
    }