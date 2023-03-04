package com.app.testtaskvk.recycler

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.app.testtaskvk.R

class GifsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var gif: ImageView = itemView.findViewById(R.id.gif)
    var progress: ProgressBar = itemView.findViewById(R.id.progress)

}