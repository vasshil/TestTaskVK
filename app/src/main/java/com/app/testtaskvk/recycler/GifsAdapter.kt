package com.app.testtaskvk.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.testtaskvk.activities.GifInfoActivity
import com.app.testtaskvk.R
import com.app.testtaskvk.pojo.Data
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class GifsAdapter(var data: ArrayList<Data>, private val context: Context): RecyclerView.Adapter<GifsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_view, null, false)

        return GifsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifsViewHolder, position: Int) {
        val realPosition = holder.adapterPosition

        // загружаем гиф по url со сжатым качеством
        Glide
            .with(context)
            .asGif()
            .load(data[realPosition].images!!.downsized!!.url)
            .listener(object : RequestListener<GifDrawable> {

                // по окончании загрузки отключаем progress bar
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {

                    holder.progress.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    holder.progress.visibility = View.GONE
                    return false
                }

            })
            .into(holder.gif)

        // при нажатии на гифку переходим во 2ю активность,
        // передаем 3 строки с url и доп информацией
        holder.gif.setOnClickListener {
            val intent = Intent(context, GifInfoActivity::class.java)

            intent.putExtra("title", data[realPosition].title)
            intent.putExtra("date", data[realPosition].importDatetime)
            intent.putExtra("url", data[realPosition].images!!.downsizedLarge!!.url)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: ArrayList<Data>) {
        // при изменении поискового запроса или переходе на другую страницу
        // меняем датасет и обновляем все элементы
        this.data = data
        notifyDataSetChanged()
    }

}