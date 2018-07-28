package flixse.weather

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import flixse.weather.db.City
import flixse.weather.db.County
import flixse.weather.db.Province
import kotlinx.android.synthetic.main.choose_area.*

class ChooseAreaFragment: Fragment() {
    private var progressBar: ProgressBar? = null

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
        return inflater!!.inflate(R.layout.choose_area, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list_view.adapter = adapter
        list_view.onItemClickListener = AdapterView.OnItemClickListener{_,_,position,_->}
            when (currentLevel) {
                LEVEL_PROVINCE -> {

                }
            }
    }











}