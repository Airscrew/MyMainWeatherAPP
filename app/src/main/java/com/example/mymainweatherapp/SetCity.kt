package com.example.mymainweatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.ktx.widget.PlaceSelectionError
import com.google.android.libraries.places.ktx.widget.PlaceSelectionSuccess
import com.google.android.libraries.places.ktx.widget.placeSelectionEvents
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

var latlng:String = ""

class SetCity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_city)

        val key = "AIzaSyC0dSUlEmIgfJtEHQQadyTsK7u83B6H1HQ"
        Places.initialize(applicationContext, key);
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Listen to place selection events

        lifecycleScope.launchWhenCreated {
            autocompleteFragment.placeSelectionEvents().collect { event ->
                when (event) {
                    is PlaceSelectionSuccess -> latlng = event.place.latLng.toString()

                        /*Toast.makeText(
                        this@SetCity,
                        "Got place '${event.place.latLng}'",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    is PlaceSelectionError -> Toast.makeText(
                        this@SetCity,
                        "Failed to get place '${event.status.statusMessage}'",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                val intent = Intent(this@SetCity,MainActivity::class.java)
                intent.putExtra("latlng", latlng)
                Log.d("set11111", latlng)
                startActivity(intent)
            }
        }




    }



}