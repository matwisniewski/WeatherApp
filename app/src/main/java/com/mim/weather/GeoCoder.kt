package com.mim.weather

import android.content.Context
import android.location.Geocoder
import android.util.Log

fun geocoderName(context: Context, lat: Double, lon: Double): String{
    var geocoder = Geocoder(context)
    val list  = geocoder.getFromLocation(lat, lon, 1)
//    Log.d("test1", list[0].getAddressLine(0)) //- całość adresu
    Log.d("test1", "geocoderName : " + list[0].locality)
    return list[0].locality
}

fun geocoderLatLon(context: Context, locality: String): List<Double>{
    var geocoder = Geocoder(context)
    val list = geocoder.getFromLocationName(locality, 1)
    Log.d("test1", "geocoderLatLon: lat - " + list[0].latitude + ", lon - " + list[0].longitude)
    return listOf(list[0].latitude, list[0].longitude)
}