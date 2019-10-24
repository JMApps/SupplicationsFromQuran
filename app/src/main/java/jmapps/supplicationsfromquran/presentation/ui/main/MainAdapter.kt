package jmapps.supplicationsfromquran.presentation.ui.main

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R

class MainAdapter(private val mainContentList: MutableList<MainModel>,
                  private val playPause: PlayPause,
                  private val audioProgress: AudioProgress,
                  private val loopOnOff: LoopOnOff,
                  private val copyContent: CopyContent,
                  private val shareLink: ShareContent) :
    RecyclerView.Adapter<MainViewHolder>() {

    interface PlayPause {
        fun playPause(state: Boolean, position: Int)
    }

    interface AudioProgress {
        fun audioProgress(progress: Int, fromUser: Boolean)
    }

    interface LoopOnOff {
        fun loopOnOff(state: Boolean)
    }

    interface CopyContent {
        fun copyContent()
    }

    interface ShareContent {
        fun shareContent()
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
        val strNameAudio = mainContentList[position].strNameAudio
        val strContentName = mainContentList[position].strContentName

        holder.tvContentArabic.text = strContentArabic
        holder.tvContentTranslation.text = Html.fromHtml(strContentTranslation)
        holder.tvContentName.text = strContentName

        holder.findPlayPause(playPause, position)
        holder.findAudioProgress(audioProgress)
        holder.findLoopOnOff(loopOnOff)
        holder.findCopy(copyContent)
        holder.shareCopy(shareLink)
    }
}