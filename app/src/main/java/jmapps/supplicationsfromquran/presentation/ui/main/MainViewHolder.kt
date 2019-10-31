package jmapps.supplicationsfromquran.presentation.ui.main

import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.supplicationsfromquran.R
import jmapps.supplicationsfromquran.presentation.mvp.setting.SettingsContract
import jmapps.supplicationsfromquran.presentation.mvp.setting.SettingsPresenterImpl

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    SettingsContract.SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

    private val cvContentItem: CardView = itemView.findViewById(R.id.cvContentItem)
    val tvContentArabic: TextView = itemView.findViewById(R.id.tvContentArabic)
    val tvContentTranslation: TextView = itemView.findViewById(R.id.tvContentTranslation)
    val tvContentName: TextView = itemView.findViewById(R.id.tvContentName)

    val btnPlayPause: Button = itemView.findViewById(R.id.btnPlayPause)
    private val btnCopy: Button = itemView.findViewById(R.id.btnCopy)
    private val btnShare: Button = itemView.findViewById(R.id.btnShare)

    private var settingsPresenterImpl: SettingsPresenterImpl = SettingsPresenterImpl(this)
    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)

    init {
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)

        backgroundMode()
        textSizeValues()
        stateTranslation()
    }

    fun findPlayItem(playItem: MainAdapter.PlayItem, position: Int) {
        itemView.setOnClickListener {
            playItem.playItem(position)
        }
    }

    fun findButtons(findButtons: MainAdapter.FindButtons) {
        findButtons.findButtons(btnCopy, btnShare)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        backgroundMode()
        textSizeValues()
        stateTranslation()
    }

    override fun colorMode(backgroundColor: Int, textArabicColor: Int, textTranslationColor: Int) {
        cvContentItem.setBackgroundColor(backgroundColor)
        tvContentArabic.setTextColor(textArabicColor)
        tvContentTranslation.setTextColor(textTranslationColor)
    }

    override fun textArabicSize(textArabicSize: Float) {
        tvContentArabic.textSize = textArabicSize
    }

    override fun textTranslationSize(textTranslationSize: Float) {
        tvContentTranslation.textSize = textTranslationSize
    }

    private fun backgroundMode() {
        val whiteMode = preferences.getBoolean("key_white_state", true)
        val sepiaMode = preferences.getBoolean("key_sepia_state", false)
        val nightMode = preferences.getBoolean("key_night_mode_state", false)

        when (true) {

            whiteMode -> settingsPresenterImpl.backgroundMode(1)

            sepiaMode -> settingsPresenterImpl.backgroundMode(2)

            nightMode -> settingsPresenterImpl.backgroundMode(3)
        }
    }

    private fun textSizeValues() {

        when (itemView.context.resources.configuration.smallestScreenWidthDp) {

            in 0..599 -> {
                val textArabicSize = preferences.getInt("key_text_arabic_size_progress", 2)
                val textTranslationSize =
                    preferences.getInt("key_text_translation_size_progress", 2)
                settingsPresenterImpl.textArabicSizeMode(textArabicSize)
                settingsPresenterImpl.textTranslationSizeMode(textTranslationSize)
            }

            600 -> {
                val textArabicSize = preferences.getInt("key_text_arabic_size_progress", 3)
                val textTranslationSize =
                    preferences.getInt("key_text_translation_size_progress", 3)
                settingsPresenterImpl.textArabicSizeMode(textArabicSize)
                settingsPresenterImpl.textTranslationSizeMode(textTranslationSize)
            }

            720 -> {
                val textArabicSize = preferences.getInt("key_text_arabic_size_progress", 4)
                val textTranslationSize =
                    preferences.getInt("key_text_translation_size_progress", 4)
                settingsPresenterImpl.textArabicSizeMode(textArabicSize)
                settingsPresenterImpl.textTranslationSizeMode(textTranslationSize)
            }
        }
    }

    private fun stateTranslation() {
        val stateTranslation = preferences.getBoolean("key_show_translation_text", true)

        if (stateTranslation) {
            tvContentTranslation.visibility = View.VISIBLE
        } else {
            tvContentTranslation.visibility = View.GONE
        }
    }
}