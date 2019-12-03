package jmapps.supplicationsfromquran.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.supplicationsfromquran.presentation.ui.main.MainModel

class DatabaseLists(private val context: Context?) {

    private lateinit var database: SQLiteDatabase

    val getContentList: MutableList<MainModel>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_contents",
                null,
                null,
                null,
                null,
                null,
                null
            )

            val contentList = ArrayList<MainModel>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val contents = MainModel(
                        cursor.getString(cursor.getColumnIndex("ContentAyah")),
                        cursor.getString(cursor.getColumnIndex("ContentTranslation")),
                        cursor.getString(cursor.getColumnIndex("NameAudio")),
                        cursor.getString(cursor.getColumnIndex("NameDua")))
                    contentList.add(contents)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return contentList
        }
}