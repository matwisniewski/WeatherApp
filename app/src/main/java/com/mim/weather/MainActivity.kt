package com.mim.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        my_location_button.setOnClickListener{
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getLastLocation()
        }
        search_button.setOnClickListener{
            var cityInput = locality_editText.text.toString()
            var JsonString: String = applicationContext.assets.open("cityList.json").bufferedReader().use {
                it.readText()
            }
            Log.d("z",cityInput)
            val match = JsonString.indexOf(cityInput)

            if (match != -1){
                var coord = geocoderLatLon(this, cityInput)
                apiConnection(coord[0],coord[1])
            }else{
                Toast.makeText(this, "Error of City name", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun apiConnection(lat: Double, lon: Double){

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceHolder: JsonPlaceHolder = retrofit.create(JsonPlaceHolder::class.java)

        val call: Call<Post> = jsonPlaceHolder.getPosts(lat,lon,"4b098911d8fefa615be642cdc61b43d4")
        call.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("test1", "ApiData/apiConnection Error callback")

            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    Log.d("test1", "ApiData/apiConnection Error response isn't successful - returned empty")
                    return
                }
                val post = response.body()!!

                dateTime.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(post.getDt().toLong() * 1000L))
                temp.text = (post.getMain().temp.toDouble() - 273.15).toInt().toString().plus("°C")
                description.text = post.getWeather()[0].description
                sunrise.text = SimpleDateFormat("HH:mm").format(Date(post.getSys().sunrise.toLong() * 1000L))
                sunset.text = SimpleDateFormat("HH:mm").format(Date(post.getSys().sunset.toLong() * 1000L))
                textCity.text = post.getName();
                textPressure.text = post.getMain().pressure;

                var iconUrl = "https://openweathermap.org/img/w/".plus(post.getWeather()[0].icon).plus(".png")
                Log.d("icon", iconUrl)
                Glide.with(this@MainActivity)
                    .load(iconUrl)
                    .fitCenter()
                    .into(icon)
            }
        })
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation(){

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        Log.d("test1", "getLastocation: lat -" + location.latitude.toString())
                        Log.d("test1", "getLastocation: lon -" + location.longitude.toString())
                        var nameOfCity = geocoderName(this, location.latitude, location.longitude)
                        locality_editText.setText(nameOfCity)
                        apiConnection(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            Log.d("test1", "getLastocation: mlat -" + mLastLocation.latitude.toString())
            Log.d("test1", "getLastocation: mlon -" + mLastLocation.longitude.toString())
        }
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}



