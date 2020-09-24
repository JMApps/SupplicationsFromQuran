package jmapps.supplicationsfromquran.presentation.mvp.main

import android.content.ClipData
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.text.Html
import jmapps.supplicationsfromquran.R

class MainPresenterImpl(
    private val mainView: MainContract.MainView?,
    private val context: Context?) : MainContract.MainPresenter {

    private val linkDescription = context?.getString(R.string.app_name)
    private val downloadLink = "https://drive.google.com/uc?export=download&id=1zZg1yIMe7bKPrkPRndeeyGxEO0r0r0XL"
    private val linkApp = "https://play.google.com/store/apps/details?id=jmapps.supplicationsfromquran"

    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun openDatabase() {
        mainView?.openDatabase()
    }

    override fun closeDatabase() {
        mainView?.closeDatabase()
    }

    override fun initMainContent() {
        mainView?.initMainContent()
    }

    override fun copyContent(contentArabic: String, contentTranslation: String) {
        myClipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        myClip = newPlainText("", Html.fromHtml("$contentArabic<p/>$contentTranslation"))
        myClipboard?.setPrimaryClip(myClip!!)
    }

    override fun shareContent(contentArabic: String, contentTranslation: String) {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("$contentArabic<p/>$contentTranslation"))
        context?.startActivity(shareLink)
    }

    override fun getSettings() {
        mainView?.getSettings()
    }

    override fun getDownloadAudios() {
        val downloadAudios = Intent(Intent.ACTION_VIEW)
        downloadAudios.data = Uri.parse(downloadLink)
        context?.startActivity(downloadAudios)
    }

    override fun getAboutUs() {
        mainView?.getAboutUs()
    }

    override fun rateApp() {
        val rate = Intent(Intent.ACTION_VIEW)
        rate.data = Uri.parse(linkApp)
        context?.startActivity(rate)
    }

    override fun shareAppLink() {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, "$linkDescription\n$linkApp")
        context?.startActivity(shareLink)
    }
}