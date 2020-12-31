package jmapps.supplicationsfromquran.presentation.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R
import java.util.*

class MainAdapter(
    context: Context,
    private val preferences: SharedPreferences,
    private val mainContentList: MutableList<MainModel>,
    private val playItem: PlayItem,
    private val eventBookmark: EventBookmark,
    private val eventCopy: EventCopy,
    private val eventShare: EventShare) :
    RecyclerView.Adapter<MainViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var currentIndex: Int = -1

    interface PlayItem {
        fun playItem(position: Int)
    }

    interface EventBookmark {
        fun bookmark(state: Boolean, position: Int)
    }

    interface EventCopy {
        fun copy(contentArabic: String, contentTranslation: String)
    }

    interface EventShare {
        fun share(contentArabic: String, contentTranslation: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(inflater.inflate(R.layout.item_main_content, parent, false))
    }

    override fun getItemCount(): Int {
        return mainContentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val strContentArabic = mainContentList[position].strContentArabic
        val strContentTranslation = mainContentList[position].strContentTranslation
        val strNumberSupplication = mainContentList[position].strNumberSupplication

        holder.tvContentArabic.text = strContentArabic

        if (!strContentTranslation.isNullOrEmpty()) {
            holder.tvContentTranslation.text = Html.fromHtml(strContentTranslation)
        } else {
            holder.tvContentTranslation.visibility = View.GONE
        }

        holder.tvNumberSupplication.text = strNumberSupplication

        val language = Locale.getDefault().language

        if (currentIndex == position) {
            if (language == "ar") {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play_accent_ar_rotate)
                holder.tvContentArabic.setTextColor(Color.argb(255, 230, 74, 25))
            } else {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play_accent)
                holder.tvContentArabic.setTextColor(Color.argb(255, 230, 74, 25))
            }
        } else {
            if (language == "ar") {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play_grey_ar_rotate)
                if (preferences.getBoolean("key_night_mode_state", false)) {
                    holder.tvContentArabic.setTextColor(Color.argb(255, 184, 184, 184))
                } else {
                    holder.tvContentArabic.setTextColor(Color.argb(255, 87, 87, 87))
                }
            } else {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play_grey)
                if (preferences.getBoolean("key_night_mode_state", false)) {
                    holder.tvContentArabic.setTextColor(Color.argb(255, 184, 184, 184))
                } else {
                    holder.tvContentArabic.setTextColor(Color.argb(255, 87, 87, 87))
                }
            }
        }

        holder.tbBookmarks.setOnCheckedChangeListener(null)
        holder.tbBookmarks.isChecked =
            preferences.getBoolean("key_bookmark_supplication_${position + 1}", false)

        holder.findPlayItem(playItem, position)
        holder.findBookmark(eventBookmark, position + 1)

        if (!strContentTranslation.isNullOrEmpty()) {
            holder.findCopy(eventCopy, strContentArabic!!, strContentTranslation)
            holder.findShare(eventShare, strContentArabic, strContentTranslation)
        } else {
            holder.findCopy(eventCopy, strContentArabic!!, "")
            holder.findShare(eventShare, strContentArabic, "")
        }
    }

    fun onItemSelected(currentIndex: Int) {
        this.currentIndex = currentIndex
        notifyDataSetChanged()
    }
}