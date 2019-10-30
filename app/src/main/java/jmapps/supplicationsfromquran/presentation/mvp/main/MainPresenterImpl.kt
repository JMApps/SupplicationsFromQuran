package jmapps.supplicationsfromquran.presentation.mvp.main

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import jmapps.supplicationsfromquran.R

class MainPresenterImpl(
    private val mainView: MainContract.MainView?,
    private val context: Context?) : MainContract.MainPresenter {

    private val linkDescription = context?.getString(R.string.app_name)
    private val linkApp = "https://play.google.com/store/apps/details?id=jmapps.supplicationsfromquran"

    override fun openDatabase() {
        mainView?.openDatabase()
    }

    override fun closeDatabase() {
        mainView?.closeDatabase()
    }

    override fun initMainContent() {
        mainView?.initMainContent()
    }

    override fun getSettings() {
        mainView?.getSettings()
    }

    override fun getAboutUs() {
        mainView?.getAboutUs()
    }

    override fun rateApp() {
        val rate = Intent(Intent.ACTION_VIEW)
        rate.data = linkApp.toUri()
        context?.startActivity(rate)
    }

    override fun shareAppLink() {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, "$linkDescription\n$linkApp")
        context?.startActivity(shareLink)
    }
}