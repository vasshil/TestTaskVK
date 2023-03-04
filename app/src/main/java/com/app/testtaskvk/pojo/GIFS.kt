package com.app.testtaskvk.pojo

import com.google.gson.annotations.SerializedName


data class GIFS (

  @SerializedName("data"       ) var data       : ArrayList<Data> = arrayListOf(),
  @SerializedName("pagination" ) var pagination : Pagination?     = Pagination(),
  @SerializedName("meta"       ) var meta       : Meta?           = Meta()

)