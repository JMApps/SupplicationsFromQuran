package jmapps.supplicationsfromquran

import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

class MainActivity : AppCompatActivity(), MainContract.MainView, MainAdapter.FindButtons,
    View.OnClickListener, CompoundButton.OnCheckedChangeListener, MainAdapter.PlayItem,
    MediaPlayer.OnCompletionListener {

    private lateinit var database: SQLiteDatabase

    private lateinit var mainContentList: MutableList<MainModel>
    private lateinit var mainAdapter: MainAdapter

    private lateinit var mainPresenterImpl: MainPresenterImpl

    private var player: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var trackIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainPresenterImpl = MainPresenterImpl(this, this)

        openDatabase()
        initMainContent()

        btnPrevious.setOnClickListener(this)
        tbPlayPause.setOnCheckedChangeListener(this)
        btnNext.setOnClickListener(this)
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

    override fun onResume() {
        super.onResume()
        clear()
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
        mainAdapter = MainAdapter(mainContentList, this, this)
        rvMainContent.adapter = mainAdapter
    }

    override fun getSettings() {
        val settings = BottomSheetSettings()
        settings.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleFull)
        settings.show(supportFragmentManager, "settings")
    }

    override fun getAboutUs() {
        BottomSheetAboutUs().show(supportFragmentManager, "about_us")
    }

    override fun findButtons(btnCopy: Button, btnShare: Button) {
        btnCopy.setOnClickListener(this)
        btnShare.setOnClickListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            if (player == null) {
                initPlayer(trackIndex)
                player?.start()
            } else {
                player?.start()
            }
            mainAdapter.onItemSelected(trackIndex)
            player?.setOnCompletionListener(this)
        } else {
            mainAdapter.onItemSelected(-1)
            if (player?.isPlaying!!) {
                player?.pause()
            }
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (trackIndex < mainContentList.size - 1) {
            trackIndex++
            initPlayer(trackIndex)
            mainAdapter.onItemSelected(trackIndex)
            rvMainContent.smoothScrollToPosition(trackIndex)
            player?.start()
            player?.setOnCompletionListener(this)
        } else {
            trackIndex = 0
            rvMainContent.smoothScrollToPosition(trackIndex)
            mainAdapter.onItemSelected(-1)
            tbPlayPause.isChecked = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnCopy -> {
                Toast.makeText(this, "Copy", Toast.LENGTH_LONG).show()
            }

            R.id.btnShare -> {
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show()
            }

            R.id.btnPrevious -> {
                if (trackIndex > 0) {
                    trackIndex--
                    mainAdapter.onItemSelected(trackIndex)
                    tbPlayPause.isChecked = true
                    rvMainContent.smoothScrollToPosition(trackIndex)
                    initPlayer(trackIndex)
                    player?.start()
                    player?.setOnCompletionListener {
                        tbPlayPause.isChecked = false
                        mainAdapter.onItemSelected(-1)
                    }
                }
            }

            R.id.btnNext -> {
                if (trackIndex < mainContentList.size - 1) {
                    trackIndex++
                    mainAdapter.onItemSelected(trackIndex)
                    tbPlayPause.isChecked = true
                    rvMainContent.smoothScrollToPosition(trackIndex)
                    initPlayer(trackIndex)
                    player?.start()
                    player?.setOnCompletionListener {
                        tbPlayPause.isChecked = false
                        mainAdapter.onItemSelected(-1)
                    }
                }
            }
        }
    }

    override fun playItem(position: Int) {
        trackIndex = position
        mainAdapter.onItemSelected(position)
        tbPlayPause.isChecked = position != -1
        initPlayer(trackIndex)
        player?.start()
        player?.setOnCompletionListener {
            tbPlayPause.isChecked = false
            mainAdapter.onItemSelected(-1)
        }

    }

    private fun initPlayer(index: Int) {
        clear()
        val resId: Int? = resources?.getIdentifier(
            mainContentList[index].strNameAudio, "raw", "jmapps.supplicationsfromquran"
        )
        player = MediaPlayer.create(this, resId!!)
    }

    private fun clear() {
        player?.stop()
        player?.release()
    }
}