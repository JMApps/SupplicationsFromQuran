package jmapps.supplicationsfromquran.presentation.ui.main

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R

class MainAdapter(
    private val mainContentList: MutableList<MainModel>,
    private val playItem: PlayItem,
    private val eventCopy: EventCopy,
    private val eventShare: EventShare) :
    RecyclerView.Adapter<MainViewHolder>() {

    private var currentIndex: Int = -1

    interface PlayItem {
        fun playItem(position: Int)
    }

    interface EventCopy {
        fun copy(contentArabic: String, contentTranslation: String)
    }

    interface EventShare {
        fun share(contentArabic: String, contentTranslation: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_main_content, parent, false))
    }

    override fun getItemCount(): Int {
        return mainContentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val strContentArabic = mainContentList[position].strContentArabic
        val strContentTranslation = mainContentList[position].strContentTranslation
        val strContentName = mainContentList[position].strContentName

        holder.tvContentArabic.text = strContentArabic
        holder.tvContentTranslation.text = Html.fromHtml(strContentTranslation)
        holder.tvContentName.text = strContentName

        if (currentIndex == position) {
            holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play)
        } else {
            holder.btnPlayPause.setBackgroundResource(R.drawable.ic_pause)
        }

        holder.findPlayItem(playItem, position)
        holder.findCopy(eventCopy, strContentArabic!!, strContentTranslation!!)
        holder.findShare(eventShare, strContentArabic, strContentTranslation)
    }

    fun onItemSelected(currentIndex: Int) {
        this.currentIndex = currentIndex
        notifyDataSetChanged()
    }
}