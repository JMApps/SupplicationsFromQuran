package jmapps.supplicationsfromquran.presentation.ui.bookmarks

import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.supplicationsfromquran.R
import jmapps.supplicationsfromquran.data.database.DatabaseLists
import jmapps.supplicationsfromquran.data.database.DatabaseOpenHelper
import jmapps.supplicationsfromquran.presentation.mvp.main.MainContract
import jmapps.supplicationsfromquran.presentation.mvp.main.MainPresenterImpl
import kotlinx.android.synthetic.main.content_bookmark.view.*

class BookmarkBottomSheet : BottomSheetDialogFragment(), MainContract.MainView,
    BookmarkAdapter.PlayItem, BookmarkAdapter.EventCopy, BookmarkAdapter.EventShare {

    private lateinit var rootBookmark: View

    private lateinit var preferences: SharedPreferences

    private var player: MediaPlayer? = null
    private var database: SQLiteDatabase? = null

    private lateinit var bookmarkContentList: MutableList<BookmarkModel>
    private lateinit var bookmarkAdapter: BookmarkAdapter

    private lateinit var mainPresenterImpl: MainPresenterImpl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootBookmark = inflater.inflate(R.layout.content_bookmark, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        mainPresenterImpl = MainPresenterImpl(this, context)
        openDatabase()
        initMainContent()

        return rootBookmark
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clear()
    }

    override fun openDatabase() {
        database = DatabaseOpenHelper(context).readableDatabase
    }

    override fun closeDatabase() {
        database?.close()
    }

    override fun initMainContent() {
        bookmarkContentList = DatabaseLists(context).getBookmarkList
        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootBookmark.rvBookmarkContent.layoutManager = verticalLayout
        bookmarkAdapter = BookmarkAdapter(context!!, preferences, bookmarkContentList, this, this, this)
        rootBookmark.rvBookmarkContent.adapter = bookmarkAdapter

        if (bookmarkAdapter.itemCount <= 0) {
            rootBookmark.ivBookmarkDescription?.visibility = View.VISIBLE
            rootBookmark.tvAddBookmarkDescription?.visibility = View.VISIBLE
            rootBookmark.rvBookmarkContent.visibility = View.GONE
        } else {
            rootBookmark.ivBookmarkDescription?.visibility = View.GONE
            rootBookmark.tvAddBookmarkDescription?.visibility = View.GONE
            rootBookmark.rvBookmarkContent.visibility = View.VISIBLE
        }
    }

    override fun copy(contentArabic: String, contentTranslation: String) {
        mainPresenterImpl.copyContent(contentArabic, contentTranslation)
        Toast.makeText(context, R.string.action_copied, Toast.LENGTH_SHORT).show()
    }

    override fun share(contentArabic: String, contentTranslation: String) {
        mainPresenterImpl.shareContent(contentArabic, contentTranslation)
    }

    override fun getSettings() {}

    override fun getAboutUs() {}

    override fun playItem(position: Int) {
        initPlayer(position)
        player?.start()
        player?.setOnCompletionListener {
            bookmarkAdapter.onItemSelected(- 1)
        }
    }

    private fun initPlayer(index: Int) {
        clear()
        val resId: Int? = resources.getIdentifier(bookmarkContentList[index].strNameAudio,
            "raw", "jmapps.supplicationsfromquran")
        player = MediaPlayer.create(context, resId!!)
        bookmarkAdapter.onItemSelected(index)
    }

    private fun clear() {
        player?.stop()
        player?.release()
        player = null
    }
}