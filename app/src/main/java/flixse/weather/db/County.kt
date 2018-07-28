package flixse.weather.db

import org.litepal.crud.LitePalSupport

class County(var id: Int = 0,
              var countyName: String? = null,
              var weatherId: String? = null,
              var cityId: Int = 0) : LitePalSupport() {}