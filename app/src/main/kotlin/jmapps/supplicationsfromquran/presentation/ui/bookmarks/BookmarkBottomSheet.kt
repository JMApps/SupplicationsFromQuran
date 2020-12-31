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
import jmapps.supplicationsfromquran.presentation.mvp.settings.SettingsContract
import jmapps.supplicationsfromquran.presentation.mvp.settings.SettingsPresenterImpl
import kotlinx.android.synthetic.main.content_bookmark.view.*
import kotlinx.android.synthetic.main.content_main.*

class BookmarkBottomSheet : BottomSheetDialogFragment(), MainContract.MainView,
    BookmarkAdapter.PlayItem, BookmarkAdapter.EventCopy, BookmarkAdapter.EventShare,
    SettingsContract.SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var rootBookmark: View
    private lateinit var preferences: SharedPreferences

    private var player: MediaPlayer? = null
    private var database: SQLiteDatabase? = null

    private lateinit var bookmarkContentList: MutableList<BookmarkModel>
    private lateinit var bookmarkAdapter: BookmarkAdapter

    private lateinit var mainPresenterImpl: MainPresenterImpl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootBookmark = inflater.inflate(R.layout.content_bookmark, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        database = DatabaseOpenHelper(requireContext()).readableDatabase

        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)

        mainPresenterImpl = MainPresenterImpl(this, requireContext())

        initMainContent()
        recyclerViewBackgroundColor()

        return rootBookmark
    }

    override fun initMainContent() {
        bookmarkContentList = DatabaseLists(context).getBookmarkList
        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootBookmark.rvBookmarkContent.layoutManager = verticalLayout
        bookmarkAdapter = BookmarkAdapter(requireContext(), preferences, bookmarkContentList, this, this, this)
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recyclerViewBackgroundColor()
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

    override fun onDestroyView() {
        super.onDestroyView()
        clear()
    }

    private fun recyclerViewBackgroundColor() {
        val settingsPresenterImpl = SettingsPresenterImpl(this)
        val whiteMode = preferences.getBoolean("key_white_state", true)
        val sepiaMode = preferences.getBoolean("key_sepia_state", false)
        val nightMode = preferences.getBoolean("key_night_mode_state", false)

        when (true) {

            whiteMode -> settingsPresenterImpl.backgroundMode(1)

            sepiaMode -> settingsPresenterImpl.backgroundMode(2)

            nightMode -> settingsPresenterImpl.backgroundMode(3)
        }
    }

    override fun colorMode(backgroundColor: Int, textArabicColor: Int, textTranslationColor: Int) {
        rootBookmark.rvBookmarkContent.setBackgroundColor(backgroundColor)
    }

    override fun textArabicSize(textArabicSize: Float) {}

    override fun textTranslationSize(textTranslationSize: Float) {}

    private fun initPlayer(index: Int) {
        clear()
        val resId = resources.getIdentifier(bookmarkContentList[index].strNameAudio,
            "raw", "jmapps.supplicationsfromquran")
        player = MediaPlayer.create(context, resId)
        bookmarkAdapter.onItemSelected(index)
    }

    private fun clear() {
        player?.stop()
        player?.release()
        player = null
    }
}