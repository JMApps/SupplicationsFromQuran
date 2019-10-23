package jmapps.supplicationsfromquran.presentation.ui.main

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R

class MainAdapter(private val mainContentList: MutableList<MainModel>) :
    RecyclerView.Adapter<MainViewHolder>() {

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
        val strNameAudio = mainContentList[position].strNameAudio

        holder.tvContentArabic.text = strContentArabic
        holder.tvContentTranslation.text = Html.fromHtml(strContentTranslation)
        holder.tvContentName.text = strContentName
    }
}