package jmapps.supplicationsfromquran.presentation.ui.main

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvContentArabic: TextView = itemView.findViewById(R.id.tvContentArabic)
    val tvContentTranslation: TextView = itemView.findViewById(R.id.tvContentTranslation)
    val tvContentName: TextView = itemView.findViewById(R.id.tvContentName)

    val btnPlayPause: Button = itemView.findViewById(R.id.btnPlayPause)
    private val btnCopy: Button = itemView.findViewById(R.id.btnCopy)
    private val btnShare: Button = itemView.findViewById(R.id.btnShare)

    fun findPlayItem(playItem: MainAdapter.PlayItem, position: Int) {
        itemView.setOnClickListener {
            playItem.playItem(position)
        }
    }

    fun findButtons(findButtons: MainAdapter.FindButtons) {
        findButtons.findButtons(btnCopy, btnShare)
    }
}