package jmapps.supplicationsfromquran.presentation.mvp.main

interface MainContract {

    interface MainView {

        fun openDatabase()

        fun closeDatabase()

        fun initMainContent()

        fun getSettings()

        fun getAboutUs()
    }

    interface MainPresenter {

        fun openDatabase()

        fun closeDatabase()

        fun initMainContent()

        fun getSettings()

        fun getAboutUs()

        fun rateApp()

        fun shareAppLink()
    }
}