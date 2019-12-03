package jmapps.supplicationsfromquran.presentation.ui.bookmarks

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

class BookmarkAdapter(
    context: Context,
    private val preferences: SharedPreferences,
    private val bookmarkContentList: MutableList<BookmarkModel>,
    private val playItem: PlayItem,
    private val eventCopy: EventCopy,
    private val eventShare: EventShare) : RecyclerView.Adapter<BookmarkViewHolder>() {

    private val inflater = LayoutInflater.from(context)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(inflater.inflate(R.layout.item_bookmark_content, parent, false))
    }

    override fun getItemCount(): Int {
        return bookmarkContentList.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val strBookmarkContentArabic = bookmarkContentList[position].strBookmarkContentArabic
        val strBookmarkContentTranslation = bookmarkContentList[position].strBookmarkContentTranslation
        val strNumberSupplication = bookmarkContentList[position].strNumberSupplication

        holder.tvBookmarkContentArabic.text = strBookmarkContentArabic
        if (!strBookmarkContentTranslation.isNullOrEmpty()) {
            holder.tvBookmarkContentTranslation.text = Html.fromHtml(strBookmarkContentTranslation)
        } else {
            holder.tvBookmarkContentTranslation.visibility = View.GONE
        }
        holder.tvNumberSupplication.text = strNumberSupplication

        val language = Locale.getDefault().language
        if (currentIndex == position) {
            if (language == "ar") {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play_ar_rotate)
            } else {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_play)
                holder.tvBookmarkContentArabic.setTextColor(Color.argb(255, 230, 74, 25))
            }
        } else {
            if (language == "ar") {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_pause_ar_rotate)
            } else {
                holder.btnPlayPause.setBackgroundResource(R.drawable.ic_pause)
                if (preferences.getBoolean("key_night_mode_state", false)) {
                    holder.tvBookmarkContentArabic.setTextColor(Color.argb(255, 184, 184, 184))
                } else {
                    holder.tvBookmarkContentArabic.setTextColor(Color.argb(255, 87, 87, 87))
                }
            }
        }

        holder.findPlayItem(playItem, position)
        if (!strBookmarkContentTranslation.isNullOrEmpty()) {
            holder.findCopy(eventCopy, strBookmarkContentArabic!!, strBookmarkContentTranslation)
            holder.findShare(eventShare, strBookmarkContentArabic, strBookmarkContentTranslation)
        } else {
            holder.findCopy(eventCopy, strBookmarkContentArabic!!, "")
            holder.findShare(eventShare, strBookmarkContentArabic, "")
        }
    }

    fun onItemSelected(currentIndex: Int) {
        this.currentIndex = currentIndex
        notifyDataSetChanged()
    }
}