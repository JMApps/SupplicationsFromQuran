package jmapps.supplicationsfromquran.presentation.mvp.bookmarks

interface BookmarkContract {

    interface ChapterModel {
        fun setFavorite(state: Boolean, supplicationPosition: Int)
    }

    interface ChapterView {
        fun saveFavorite(keyFavorite: String, state: Boolean)
        fun saveMessage(state: Boolean)
        fun showException(e: Exception)
    }

    interface ChapterPresenter {
        fun getFavorite(state: Boolean, supplicationPosition: Int)
    }
}