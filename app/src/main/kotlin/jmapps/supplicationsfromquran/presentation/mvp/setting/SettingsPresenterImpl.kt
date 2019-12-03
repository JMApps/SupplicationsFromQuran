package jmapps.supplicationsfromquran.presentation.mvp.setting

import android.graphics.Color

class SettingsPresenterImpl(private val settingsView: SettingsContract.SettingsView?) :
    SettingsContract.SettingsPresenter {

    override fun backgroundMode(numberMode: Int) {
        when (numberMode) {

            1 -> {
                settingsView?.colorMode(
                    Color.argb(255, 244, 244, 244),
                    Color.argb(255, 87, 87, 87),
                    Color.argb(255, 87, 87, 87))
            }

            2 -> {
                settingsView?.colorMode(
                    Color.argb(255, 242, 238, 167),
                    Color.argb(255, 112, 112, 112),
                    Color.argb(255, 112, 112, 112))
            }

            3 -> {
                settingsView?.colorMode(
                    Color.argb(255, 44, 44, 44),
                    Color.argb(255, 184, 184, 184),
                    Color.argb(255, 184, 184, 184))
            }
        }
    }

    override fun textArabicSizeMode(numberArabicMode: Int) {
        when (numberArabicMode) {

            0 -> settingsView?.textArabicSize(16f)

            1 -> settingsView?.textArabicSize(18f)

            2 -> settingsView?.textArabicSize(20f)

            3 -> settingsView?.textArabicSize(22f)

            4 -> settingsView?.textArabicSize(26f)

            5 -> settingsView?.textArabicSize(32f)
        }
    }

    override fun textTranslationSizeMode(numberTranslationMode: Int) {
        when (numberTranslationMode) {

            0 -> settingsView?.textTranslationSize(14f)

            1 -> settingsView?.textTranslationSize(16f)

            2 -> settingsView?.textTranslationSize(18f)

            3 -> settingsView?.textTranslationSize(20f)

            4 -> settingsView?.textTranslationSize(24f)

            5 -> settingsView?.textTranslationSize(30f)
        }
    }
}