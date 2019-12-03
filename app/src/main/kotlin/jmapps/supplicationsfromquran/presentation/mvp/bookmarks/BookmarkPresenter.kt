package jmapps.supplicationsfromquran.presentation.mvp.bookmarks

import android.database.sqlite.SQLiteDatabase

class BookmarkPresenter(_bookmarkView: BookmarkContract.ChapterView, database: SQLiteDatabase) :
    BookmarkContract.ChapterPresenter {

    private var bookmarkView: BookmarkContract.ChapterView = _bookmarkView
    private var bookmarkModel: BookmarkContract.ChapterModel = BookmarkModel(database, _bookmarkView)

    override fun getFavorite(state: Boolean, supplicationPosition: Int) {
        bookmarkModel.setFavorite(state, supplicationPosition)
        bookmarkView.saveMessage(state)
    }
}