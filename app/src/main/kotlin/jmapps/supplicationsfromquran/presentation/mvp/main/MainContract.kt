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

        fun copyContent(contentArabic: String, contentTranslation: String)

        fun shareContent(contentArabic: String, contentTranslation: String)

        fun getSettings()

        fun getDownloadAudios()

        fun getAboutUs()

        fun rateApp()

        fun shareAppLink()
    }
}