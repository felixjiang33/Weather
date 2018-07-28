package flixse.weather.db

import org.litepal.crud.LitePalSupport

class Province(var id: Int = 0,
               var provinceName: String? = null,
               var provinceCode: Int = 0) : LitePalSupport() {}