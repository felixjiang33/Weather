package flixse.weather.util

import okhttp3.OkHttpClient
import okhttp3.Request

class HttpUtil {
    val Url = "https://free-api.heweather.com/s6/weather/forecast?"
    val Key = "key=f2497e1d1c1a41a38cebd20d5783176c"
    val Bing = "http://guolin.tech/api/bing_pic"
    val China = "https://search.heweather.com/find?group=cn"

    fun sendOkHttpRequest(address: String? = null, callback: okhttp3.Callback){
        val client = OkHttpClient()
        val request = Request.Builder().url(address).build()

        client.newCall(request).enqueue(callback)
    }
}