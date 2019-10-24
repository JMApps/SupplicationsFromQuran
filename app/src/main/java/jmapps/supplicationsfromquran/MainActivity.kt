package jmapps.supplicationsfromquran

import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.supplicationsfromquran.data.database.DatabaseLists
import jmapps.supplicationsfromquran.data.database.DatabaseOpenHelper
import jmapps.supplicationsfromquran.presentation.mvp.main.MainContract
import jmapps.supplicationsfromquran.presentation.mvp.main.MainPresenterImpl
import jmapps.supplicationsfromquran.presentation.ui.about.BottomSheetAboutUs
import jmapps.supplicationsfromquran.presentation.ui.main.MainAdapter
import jmapps.supplicationsfromquran.presentation.ui.main.MainModel
import jmapps.supplicationsfromquran.presentation.ui.settings.BottomSheetSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MainContract.MainView, MainAdapter.PlayPause,
    MainAdapter.AudioProgress, MainAdapter.LoopOnOff, MainAdapter.CopyContent,
    MainAdapter.ShareContent {

    private lateinit var database: SQLiteDatabase

    private lateinit var mainContentList: MutableList<MainModel>
    private lateinit var mainAdapter: MainAdapter

    private lateinit var mainPresenterImpl: MainPresenterImpl

    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainPresenterImpl = MainPresenterImpl(this, this)

        openDatabase()
        initMainContent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_settings -> mainPresenterImpl.getSettings()

            R.id.action_about_us -> mainPresenterImpl.getAboutUs()

            R.id.action_rate -> mainPresenterImpl.rateApp()

            R.id.action_share -> mainPresenterImpl.shareAppLink()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        closeDatabase()
    }

    override fun openDatabase() {
        database = DatabaseOpenHelper(this).readableDatabase
    }

    override fun closeDatabase() {
        database.close()
    }

    override fun initMainContent() {
        mainContentList = DatabaseLists(this).getContentList
        val verticalLayout = LinearLayoutManager(this)
        rvMainContent.layoutManager = verticalLayout
        mainAdapter = MainAdapter(mainContentList, this, this, this, this, this)
        rvMainContent.adapter = mainAdapter
    }

    override fun getSettings() {
        BottomSheetSettings().show(supportFragmentManager, "settings")
    }

    override fun getAboutUs() {
        BottomSheetAboutUs().show(supportFragmentManager, "about_us")
    }

    override fun playPause(state: Boolean, position: Int) {
        val resId: Int? = resources?.getIdentifier(
            mainContentList[position].strNameAudio, "raw", "jmapps.supplicationsfromquran")
        player = MediaPlayer.create(this, resId!!)
        if (state) {
            player?.start()
        } else {
            player?.pause()
        }
    }

    override fun audioProgress(progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player?.seekTo(progress * 1000)
        }
    }

    override fun loopOnOff(state: Boolean) {
        Toast.makeText(this, "Loop", Toast.LENGTH_LONG).show()
    }

    override fun copyContent() {
        Toast.makeText(this, "Copy", Toast.LENGTH_LONG).show()
    }

    override fun shareContent() {
        Toast.makeText(this, "Share", Toast.LENGTH_LONG).show()
    }

    private fun clear() {
        player?.stop()
        player?.release()
    }
}