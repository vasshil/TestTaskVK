package com.app.testtaskvk.pojo

import com.google.gson.annotations.SerializedName


data class DownsizedMedium (

  @SerializedName("height" ) var height : String? = null,
  @SerializedName("width"  ) var width  : String? = null,
  @SerializedName("size"   ) var size   : String? = null,
  @SerializedName("url"    ) var url    : String? = null

)