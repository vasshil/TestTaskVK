package com.app.testtaskvk.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.app.testtaskvk.R
import com.bumptech.glide.Glide

class GifInfoActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_info)

        val gifImageView = findViewById<ImageView>(R.id.gif_image_detailed)
        val gifInfo = findViewById<TextView>(R.id.gif_info)
        val returnButton = findViewById<Button>(R.id.return_button)

        // заново загружаем в gif в imageView большего размера
        Glide
            .with(this)
            .asGif()
            .load(intent.getStringExtra("url"))
            .into(gifImageView)

        // отображаем доп. информацию о гифке через html, для простоты форматирования
        val text =
            """
                <html>
                <b>Название гифки:</b>
                ${intent.getStringExtra("title")}
                <br>
                <b>Дата и время загрузки:</b>
                ${intent.getStringExtra("date")}
                </html>
            """.trimIndent()

        gifInfo.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(text)
        }

        // возврат к главному экрану
        returnButton.setOnClickListener {
            finish()
        }


    }
}