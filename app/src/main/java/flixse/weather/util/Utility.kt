package flixse.weather.util

import android.text.TextUtils
import flixse.weather.db.City
import flixse.weather.db.County
import flixse.weather.db.Province
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Utility {
    fun handleProvinceResponse(response: String? = null): Boolean{
        if (!TextUtils.isEmpty(response)){
            try{
                val allProvinces = JSONArray(response)
                for(i in 0..(allProvinces.length() - 1))
                    val provinceObject = allProvinces.getJSONObject(i)
                    val province = Province()
                    province.provinceName = provinceObject.getString("name")
                    province.provinceCode = provinceObject.getInt("id")
                    province.save()
                return true
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }
        return false
    }

    fun handleCityResponse(response: String, provinceId: Int): Boolean{
        if (!TextUtils.isEmpty(response)){
            try{
                val allCities = JSONArray(response)
                for (i in 0..(allCities.length() - 1)) {
                    val cityObject = allCities.getJSONObject(i)
                    val city = City()
                    city.cityName = cityObject.getString("name")
                    city.cityCode = cityObject.getInt("id")
                    city.provinceId = provinceId
                    city.save()
                }

                return true
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }
        return false
    }

    fun handleCountryResponse(reponse: String, cityId: Int): Boolean {
        if (!TextUtils.isEmpty(reponse)){
            try {
                val allCountries = JSONArray(reponse)
                for (i in 0..allCountries.length() - 1) {
                    val countyObject = allCountries.getJSONObject(i)
                    val county = County()

                    county.cityId = cityId
                    county.countyName = countyObject.getString("name")
                    county.weatherId = countyObject.getString("weather_id")
                    county.save()
                }
            }catch (e: JSONException){
                e.printStackTrace()
            }

        }
    }
    fun handleWeatherResponse(response: String): Weather? {
        try{
            val jsonObject  = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray()
            val weatherContent = jsonArray
        }
    }
}