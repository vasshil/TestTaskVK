package com.app.testtaskvk

import com.app.testtaskvk.pojo.GIFS
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "xsyzlWuU1CWbOLI2RyZ2qRkDtG2n9oUA"

// количество гифок на странице
const val COUNT_IN_PAGE = 25

interface APIService {

    // в запросе используем 2 переменных параметра:
    // строка запроса и номер первой получаемой гифки из общего списка
    @GET("search?api_key=$API_KEY&limit=$COUNT_IN_PAGE&rating=g&lang=en")
    fun getGifs(@Query("q") request: String, @Query("offset") offset: Int): Call<GIFS?>?

}