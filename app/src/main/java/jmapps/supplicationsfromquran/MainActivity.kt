package jmapps.supplicationsfromquran

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class MainActivity : AppCompatActivity(), MainContract.MainView {

    private lateinit var database: SQLiteDatabase
    private lateinit var mainContentList: MutableList<MainModel>
    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainPresenterImpl: MainPresenterImpl

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
        mainAdapter = MainAdapter(mainContentList)
        rvMainContent.adapter = mainAdapter
    }

    override fun getSettings() {
        BottomSheetSettings().show(supportFragmentManager, "settings")
    }

    override fun getAboutUs() {
        BottomSheetAboutUs().show(supportFragmentManager, "about_us")
    }
}