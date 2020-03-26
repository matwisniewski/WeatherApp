package com.mim.weather

import com.google.gson.annotations.SerializedName

class Post {
    private val coord = coordClass()
    private val weather = listOf<weatherClass>()
    private val base: String = ""
    private val main = mainClass()
    private val visibility: String = ""
    private val wind = windClass()
    private val clouds = cloudsClass()
    private val dt: String = ""
    private val sys = sysClass()
    private val id: String = ""
    private val name: String = ""
    private val cod: String = ""

    @SerializedName("body")

    fun getCoord(): coordClass{
        return coord
    }
    fun getWeather(): List<weatherClass>{
        return weather
    }
    fun getBase(): String{
        return base
    }
    fun getMain(): mainClass{
        return main
    }
    fun getVisibility(): String{
        return visibility
    }
    fun getWind(): windClass{
        return wind
    }
    fun getClouds(): cloudsClass{
        return clouds
    }
    fun getDt(): String{
        return dt
    }
    fun getSys(): sysClass{
        return sys
    }
    fun getId(): String{
        return id
    }
    fun getName(): String{
        return name
    }
    fun getCod(): String{
        return cod
    }
}


class coordClass{
    val lon: String = ""
    val lat: String = ""
}
class weatherClass{
    val id: String = ""
    val main: String = ""
    val description: String = ""
    val icon: String = ""
}
class mainClass{
    val temp: String = ""
    val pressure: String = ""
    val humidity: String = ""
    val temp_min: String = ""
    val temp_max: String = ""
}
class windClass{
    val speed: String = ""
    val deg: String = ""
}
class cloudsClass{
    val all: String = ""
}
class sysClass{
    val type: String = ""
    val id: String = ""
    val massage: String = ""
    val country: String = ""
    val sunrise: String = ""
    val sunset: String = ""
}