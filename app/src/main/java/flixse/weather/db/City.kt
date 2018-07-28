package flixse.weather.db

import org.litepal.crud.LitePalSupport

class City(var id: Int = 0,
           var cityName: String? = null,
           var cityCode: Int = 0,
           var provinceId: Int = 0) : LitePalSupport() {}