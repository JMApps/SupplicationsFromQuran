package jmapps.supplicationsfromquran.presentation.mvp.bookmarks

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class BookmarkModel(
    private val database: SQLiteDatabase?,
    private val bookmarkView: BookmarkContract.ChapterView?) : BookmarkContract.ChapterModel {

    override fun setFavorite(state: Boolean, supplicationPosition: Int) {
        try {
            val favorite = ContentValues()
            favorite.put("Favorite", state)

            database?.update(
                "Table_of_contents",
                favorite,
                "_id = ?",
                arrayOf("$supplicationPosition"))

            bookmarkView?.saveFavorite("key_bookmark_supplication_$supplicationPosition", state)
        } catch (e: Exception) {
            bookmarkView?.showException(e)
        }
    }
}