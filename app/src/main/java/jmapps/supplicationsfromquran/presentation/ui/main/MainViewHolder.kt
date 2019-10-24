package jmapps.supplicationsfromquran.presentation.ui.main

import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvContentArabic: TextView = itemView.findViewById(R.id.tvContentArabic)
    val tvContentTranslation: TextView = itemView.findViewById(R.id.tvContentTranslation)
    val tvContentName: TextView = itemView.findViewById(R.id.tvContentName)

    private val tbPlayPause: ToggleButton = itemView.findViewById(R.id.tbPlayPause)
    private val sbAudioProgress: SeekBar = itemView.findViewById(R.id.sbAudioProgress)
    private val tbLoop: ToggleButton = itemView.findViewById(R.id.tbLoop)
    private val btnCopy: Button = itemView.findViewById(R.id.btnCopy)
    private val btnShare: Button = itemView.findViewById(R.id.btnShare)

    fun findPlayPause(playPause: MainAdapter.PlayPause, position: Int) {
        tbPlayPause.setOnCheckedChangeListener { _, isChecked ->
            playPause.playPause(isChecked, position)
        }
    }

    fun findAudioProgress(audioProgress: MainAdapter.AudioProgress) {
        sbAudioProgress.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioProgress.audioProgress(progress, fromUser)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun findLoopOnOff(loopOnOff: MainAdapter.LoopOnOff) {
        tbLoop.setOnCheckedChangeListener { _, isChecked ->
            loopOnOff.loopOnOff(isChecked)
        }
    }

    fun findCopy(copyContent: MainAdapter.CopyContent) {
        btnCopy.setOnClickListener {
            copyContent.copyContent()
        }
    }

    fun shareCopy(shareContent: MainAdapter.ShareContent) {
        btnShare.setOnClickListener {
            shareContent.shareContent()
        }
    }
}