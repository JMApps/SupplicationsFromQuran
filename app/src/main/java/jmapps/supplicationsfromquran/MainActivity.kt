package jmapps.supplicationsfromquran

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.supplicationsfromquran.data.database.DatabaseLists
import jmapps.supplicationsfromquran.data.database.DatabaseOpenHelper
import jmapps.supplicationsfromquran.presentation.ui.main.MainAdapter
import jmapps.supplicationsfromquran.presentation.ui.main.MainModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private lateinit var mainContentList: MutableList<MainModel>
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        openDatabase()
        initMainContent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeDatabase()
    }

    private fun initMainContent() {
        mainContentList = DatabaseLists(this).getContentList
        val verticalLayout = LinearLayoutManager(this)
        rvMainContent.layoutManager = verticalLayout
        mainAdapter = MainAdapter(mainContentList)
        rvMainContent.adapter = mainAdapter
    }

    private fun openDatabase() {
        database = DatabaseOpenHelper(this).readableDatabase
    }

    private fun closeDatabase() {
        database?.close()
    }
}