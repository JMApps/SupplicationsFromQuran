package jmapps.supplicationsfromquran

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.supplicationsfromquran.data.database.DatabaseLists
import jmapps.supplicationsfromquran.data.database.DatabaseOpenHelper
import jmapps.supplicationsfromquran.databinding.ActivityMainBinding
import jmapps.supplicationsfromquran.presentation.mvp.bookmarks.BookmarkContract
import jmapps.supplicationsfromquran.presentation.mvp.bookmarks.BookmarkPresenter
import jmapps.supplicationsfromquran.presentation.mvp.main.MainContract
import jmapps.supplicationsfromquran.presentation.mvp.main.MainPresenterImpl
import jmapps.supplicationsfromquran.presentation.mvp.settings.SettingsContract
import jmapps.supplicationsfromquran.presentation.mvp.settings.SettingsPresenterImpl
import jmapps.supplicationsfromquran.presentation.ui.about.BottomSheetAboutUs
import jmapps.supplicationsfromquran.presentation.ui.bookmarks.BookmarkBottomSheet
import jmapps.supplicationsfromquran.presentation.ui.main.MainAdapter
import jmapps.supplicationsfromquran.presentation.ui.main.MainModel
import jmapps.supplicationsfromquran.presentation.ui.settings.BottomSheetSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MainContract.MainView,
    View.OnClickListener, CompoundButton.OnCheckedChangeListener, MainAdapter.PlayItem,
    MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, MainAdapter.EventCopy,
    MainAdapter.EventShare, BookmarkContract.ChapterView, MainAdapter.EventBookmark,
    SharedPreferences.OnSharedPreferenceChangeListener, SettingsContract.SettingsView {

    private lateinit var binding: ActivityMainBinding
    private var database: SQLiteDatabase? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var mainContentList: MutableList<MainModel>
    private lateinit var mainAdapter: MainAdapter

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var bookmarkPresenter: BookmarkPresenter

    private var player: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var trackIndex: Int = 0

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        LockOrientation(this).lock()

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        mainPresenterImpl = MainPresenterImpl(this, this)
        bookmarkPresenter = BookmarkPresenter(this, database!!)

        initMainContent()

        binding.btnPrevious.setOnClickListener(this)
        binding.tbPlayPause.setOnCheckedChangeListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.sbAudioProgress.setOnSeekBarChangeListener(this)
        binding.tbFollowing.setOnCheckedChangeListener(this)
        binding.tbLoop.setOnCheckedChangeListener(this)
        binding.btnBookmarks.setOnClickListener(this)

        recyclerViewBackgroundColor()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> mainPresenterImpl.getSettings()

            R.id.action_download_audios -> mainPresenterImpl.getDownloadAudios()

            R.id.action_about_us -> mainPresenterImpl.getAboutUs()

            R.id.action_rate -> mainPresenterImpl.rateApp()

            R.id.action_share -> mainPresenterImpl.shareAppLink()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recyclerViewBackgroundColor()
    }

    override fun onDestroy() {
        super.onDestroy()
        database?.close()
        clear()
    }

    override fun initMainContent() {
        database = DatabaseOpenHelper(this).readableDatabase
        mainContentList = DatabaseLists(this).getContentList
        val verticalLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMainContent.layoutManager = verticalLayout
        mainAdapter = MainAdapter(this, preferences, mainContentList, this, this, this, this)
        rvMainContent.adapter = mainAdapter
    }

    override fun bookmark(state: Boolean, position: Int) {
        bookmarkPresenter.getFavorite(state, position)
    }

    override fun copy(contentArabic: String, contentTranslation: String) {
        mainPresenterImpl.copyContent(contentArabic, contentTranslation)
        Toast.makeText(this, R.string.action_copied, Toast.LENGTH_SHORT).show()
    }

    override fun share(contentArabic: String, contentTranslation: String) {
        mainPresenterImpl.shareContent(contentArabic, contentTranslation)
    }

    override fun saveFavorite(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    override fun saveMessage(state: Boolean) {
        if (state) {
            Toast.makeText(this, getString(R.string.action_bookmark_added), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.action_bookmark_removed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showException(e: Exception) {
        Toast.makeText(this, "${getString(R.string.action_error)} $e", Toast.LENGTH_SHORT).show()
    }

    override fun getSettings() {
        val settings = BottomSheetSettings()
        settings.show(supportFragmentManager, "settings")
    }

    override fun getAboutUs() {
        val aboutUs = BottomSheetAboutUs()
        aboutUs.show(supportFragmentManager, "about_us")
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.tbPlayPause -> {
                if (isChecked) {
                    if (player == null) {
                        initPlayer(trackIndex)
                        player?.start()
                    } else {
                        mainAdapter.onItemSelected(trackIndex)
                        currentAudioProgress()
                        player?.start()
                    }
                } else {
                    mainAdapter.onItemSelected(-1)
                    player?.pause()
                }
            }
            R.id.tbLoop -> {
                if (isChecked) {
                    if (tbFollowing.isChecked) {
                        tbFollowing.isChecked = false
                    }
                    Toast.makeText(this, R.string.action_loop_on, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.action_loop_off, Toast.LENGTH_SHORT).show()
                }
                player?.isLooping = isChecked
            }
            R.id.tbFollowing -> {
                if (isChecked) {
                    if (tbLoop.isChecked) {
                        tbLoop.isChecked = false
                    }
                    Toast.makeText(this, R.string.action_following_play_on, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.action_following_play_off, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPrevious -> {
                if (trackIndex > 0) {
                    trackIndex--
                    initPlayer(trackIndex)
                    player?.start()
                    tbPlayPause.isChecked = true
                    rvMainContent.smoothScrollToPosition(trackIndex)
                }
            }
            R.id.btnNext -> {
                if (trackIndex < mainContentList.size - 1) {
                    trackIndex++
                    initPlayer(trackIndex)
                    player?.start()
                    tbPlayPause.isChecked = true
                    rvMainContent.smoothScrollToPosition(trackIndex)
                }
            }
            R.id.btnBookmarks -> {
                val bookmarkBottomSheet = BookmarkBottomSheet()
                bookmarkBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
                bookmarkBottomSheet.show(supportFragmentManager, "bookmarks")
                clear()
                mainAdapter.onItemSelected(-1)
                tbPlayPause.isChecked = false
                tbLoop.isChecked = false
                tbFollowing.isChecked = false
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player?.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun initPlayer(index: Int) {
        clear()
        val resId: Int? = resources?.getIdentifier(mainContentList[index].strNameAudio,
            "raw", "jmapps.supplicationsfromquran")
        player = MediaPlayer.create(this, resId!!)
        mainAdapter.onItemSelected(index)
        currentAudioProgress()
        player?.setOnCompletionListener(this)
    }

    override fun playItem(position: Int) {
        trackIndex = position
        tbPlayPause.isChecked = position != -1
        initPlayer(trackIndex)
        player?.start()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (tbLoop.isChecked) {
            player?.isLooping = true
        } else {
            player?.isLooping = false
            if (tbFollowing.isChecked) {
                if (trackIndex < mainContentList.size - 1) {
                    trackIndex++
                    initPlayer(trackIndex)
                    rvMainContent.smoothScrollToPosition(trackIndex)
                    player?.start()
                } else {
                    trackIndex = 0
                    rvMainContent.smoothScrollToPosition(trackIndex)
                    mainAdapter.onItemSelected(-1)
                    tbPlayPause.isChecked = false
                    tbFollowing.isChecked = false
                    handler.removeCallbacks(runnable)
                    sbAudioProgress?.progress = 0
                    player = null
                }
            } else {
                tbPlayPause.isChecked = false
                mainAdapter.onItemSelected(-1)
                handler.removeCallbacks(runnable)
                sbAudioProgress?.progress = 0
            }
        }
    }

    private fun currentAudioProgress() {
        sbAudioProgress?.max = player?.seconds!!
        runnable = Runnable {
            sbAudioProgress?.progress = player?.currentSeconds!!
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun clear() {
        if (player != null) {
            handler.removeCallbacks(runnable)
            sbAudioProgress?.progress = 0
            player?.stop()
            player?.release()
            player = null
        }
    }

    private val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }

    private val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
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
        rvMainContent.setBackgroundColor(backgroundColor)
    }

    override fun textArabicSize(textArabicSize: Float) {}

    override fun textTranslationSize(textTranslationSize: Float) {}
}