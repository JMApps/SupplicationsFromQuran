package jmapps.supplicationsfromquran.presentation.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvContentArabic: TextView = itemView.findViewById(R.id.tvContentArabic)
    val tvContentTranslation: TextView = itemView.findViewById(R.id.tvContentTranslation)
    val tvContentName: TextView = itemView.findViewById(R.id.tvContentName)
}