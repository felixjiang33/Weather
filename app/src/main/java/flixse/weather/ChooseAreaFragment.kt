package flixse.weather

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import flixse.weather.db.City
import flixse.weather.db.County
import flixse.weather.db.Province
import flixse.weather.util.HttpUtil
import flixse.weather.util.Utility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.choose_area.*
import okhttp3.Call
import okhttp3.Response
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport
import java.io.IOException
import java.net.CacheResponse
import java.net.Inet4Address

class ChooseAreaFragment: Fragment() {

    private val adapter by lazy {
        ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
    }

    private val dataList = ArrayList<String>()
    private var provinceList: List<Province>? = null
    private var cityList: List<City>? = null
    private var countyList: List<County>? = null
    private var selectedProvince: Province? = null
    private var selectedCity: City? = null
    private var currentLevel: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(android.R.layout.choose_area, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        private val progressBar: ProgressBar = this.progressBar1

        list_view.adapter = adapter
        list_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (currentLevel) {
                LEVEL_PROVINCE -> {
                    selectedProvince = provinceList!![position]
                    queryCities()
                }
                LEVEL_CITY -> {
                    selectedCity =  cityList!![position]
                    queryCounties()
                }
                LEVEL_COUNTY -> {
                    val weatherId = countyList!![position].weatherId
                    weatherId?.let{
                        when (activity){
                            is MainActivity -> {
                                val intent = Intent(activity, WeaterActivity::class.java)
                                intent.putExtra("weather_id", weatherId)
                                startActivity(intent)
                                activity.finish()
                            }
                            is WeatherActivity -> {
                                val activity = activity as WeatherActivity
                                activity.drawerLayout.closeDrawer()
                                activity.swipeRefresh.isRefreshing = true
                                activity.requsetWeather(weatherId)
                            }
                            else -> {}
                        }
                    }
                }
                else -> {}
            }
        }

        back_button.setOnClickListener {
            when (currentLevel){
                LEVEL_COUNTY -> {
                    queryCities()
                }

                LEVEL_CITY -> {
                    queryProvinces()
                }

                else -> {}
            }
            queryProvinces()
        }
    }

    private fun queryProvinces() {
        title_text.setText("中国")
        back_button.visibility = View.GONE
        provinceList = LitePal.findAll(Province::class.java)
        provinceList?.let {
            if (it.isNotEmpty()) {
                dataList.clear()
                it.map {
                    dataList.add(it.provinceName!!)
                }
                adapter.notifyDataSetChanged()
                list_view.setSelection(0)
                currentLevel = LEVEL_PROVINCE
                return
            }
        }
        queryFromServer(HttpUtil.China, "province")
    }

    private fun queryCities() {
        title_text.text = selectedProvince!!.provinceName
        backbutton.visibility = View.VISIBLE
        cityList = LitePal.where("provinceid = ?", selectedProvince!!.id.toString())
                .find(City::class.java)
        cityList?.let {
            if (it.isNotEmpty()) {
                dataList.clear()
                it.map {
                    dataList.add(it.cityName!!)
                }
                adapter.notifyDataSetChanged()
                list_view.setSelection(0)
                currentLevel = LEVEL_CITY
                return
            }
        }
        val provinceCode = selectedProvince!!.provinceCode
        val address = "${HttpUtil.China}/$provinceCode"
        queryFromServer(address, "city")
    }

    private  fun queryCounties() {
        title_text.text = selectedCity!!.cityName
        backbutton.visibility = View.VISIBLE
        countyList = LitePal.where("cityid = ?", selectedCity!!.id.toString())
                .find(County::class.java)
        countyList?.let {
            if (it.isNotEmpty()) {
                dataList.clear()
                it.map {
                    dataList.add(it.countyName!!)
                }
                adapter.notifyDataSetChanged()
                list_view.setSelection(0)
                currentLevel = LEVEL_COUNTY
                return
            }
        }
        val provinceCode = selectedProvince!!.provinceCode
        val cityCode = selectedCity!!.cityCode
        val address = "${HttpUtil.China}/$provinceCode/$cityCode"

        queryFromServer(address, "county")
    }

    private fun queryFromServer(address: String, type: String) {
        showProgressBar()
        HttpUtil.sendOkHttpRequest(address, object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body().string()
                var result = false
                when (type) {
                    "province" -> {
                        result = Utility.handleProvinceResponse(responseText)
                    }
                    "city" -> {
                        result = Utility.handleCityResponse(responseText, selectedProvince!!.id)
                    }
                    "county" -> {
                        result = Utility.handleCountyResponse(responseText, selectedCity!!.id)
                    }
                }

                if (result) {
                    activity.runOnUiThread(Runnable {
                        closeProgressDialog()
                        when (type) {
                            "province" -> {
                                queryProvinces()
                            }
                            "city" -> {
                                queryCities()
                            }
                            "county" -> {
                                queryCounties()
                            }
                        }
                    })
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread(Runnable {
                    clossProgressBar()
                    Toast.makeText(context, "load failed", Toast.LENGTH_SHORT).show()
                })
            }
        })
    }
    private fun showProgressBar() {

        if (progressBar == null) {
            progressBar = ProgressBar(activity)
            progressBar?.
        }
    }

}












}