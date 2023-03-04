package com.app.testtaskvk.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.testtaskvk.APIService
import com.app.testtaskvk.COUNT_IN_PAGE
import com.app.testtaskvk.R
import com.app.testtaskvk.pojo.GIFS
import com.app.testtaskvk.recycler.GifsAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private var currentPage = 1 // от 1 до ..
    private var totalGifsFound = 0

    private lateinit var pageNumber: TextView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    private lateinit var searchButton: Button

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: GifsAdapter
    private lateinit var nothingFound: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // запускаем recycler view
        recycler = findViewById(R.id.gifs)

        adapter = GifsAdapter(arrayListOf(), this)
        recycler.adapter = adapter

        // используем расположение элеменов в 2 столбца
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        recycler.layoutManager = layoutManager

        val searchField = findViewById<TextInputEditText>(R.id.search_gifs)

        // кнопка поиска
        searchButton = findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            currentPage = 1
            setPageNumber()
            previousButton.isEnabled = false

            getGIFSUrl(searchField.text.toString())

        }

        // отметка, если нет гифок, соответсвующих запросу
        nothingFound = findViewById(R.id.nothing_found)


        // выбор страницы для всего диапазона по 25 шт.
        pageNumber = findViewById(R.id.page_number)
        setPageNumber()

        nextButton = findViewById(R.id.next_page)
        previousButton = findViewById(R.id.previous_page)

        nextButton.setOnClickListener {
            previousButton.isEnabled = true

            currentPage++

            getGIFSUrl(searchField.text.toString())

            setPageNumber()

        }
        previousButton.setOnClickListener {
            if (currentPage == 2) {
                previousButton.isEnabled = false
            }

            currentPage--

            getGIFSUrl(searchField.text.toString())

            setPageNumber()

        }

    }

    private fun getGIFSUrl(request: String) {

        // получаем полный json и автоматически парсим его в дата классы в пакете pojo с помощью retrofit
        // корневой класс данных - com.app.testtaskvk/pojo/GIFS.kt
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/gifs/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        val call = service.getGifs(request, COUNT_IN_PAGE * (currentPage - 1))
        call!!.enqueue(object : Callback<GIFS?> {

            override fun onResponse(call: Call<GIFS?>?, response: Response<GIFS?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.data.size > 0) {
                        // включаем/выключаем кнопки в зависимости от количества гифок и выбранной страницы
                        nothingFound.visibility = View.GONE
                        recycler.visibility = View.VISIBLE

                        totalGifsFound = response.body()!!.pagination!!.totalCount!!
                        if (totalGifsFound <= COUNT_IN_PAGE) {
                            nextButton.isEnabled = false
                            previousButton.isEnabled = false
                        } else {
                            if (currentPage == 1) {
                                nextButton.isEnabled = true
                                previousButton.isEnabled = false
                            } else {
                                nextButton.isEnabled = true
                                previousButton.isEnabled = true
                            }

                            if (totalGifsFound < COUNT_IN_PAGE * currentPage) {
                                nextButton.isEnabled = false
                            }

                        }

                    } else {
                        nothingFound.visibility = View.VISIBLE
                        recycler.visibility = View.INVISIBLE

                    }

                    adapter.updateData(response.body()!!.data)

                } else {
                    Toast.makeText(this@MainActivity, "Ошибка в API", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onFailure(call: Call<GIFS?>?, t: Throwable) {
                Toast.makeText(this@MainActivity, "Проверьте подключение к интернету",
                    Toast.LENGTH_SHORT).show()
            }

        })



    }

    @SuppressLint("SetTextI18n")
    private fun setPageNumber() {
        pageNumber.text = "Страница: $currentPage"
    }

}