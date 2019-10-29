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
                    Color.argb(255, 87, 87, 87)
                )
            }

            2 -> {
                settingsView?.colorMode(
                    Color.argb(255, 242, 238, 167),
                    Color.argb(255, 112, 112, 112),
                    Color.argb(255, 112, 112, 112)
                )
            }

            3 -> {
                settingsView?.colorMode(
                    Color.argb(255, 44, 44, 44),
                    Color.argb(255, 184, 184, 184),
                    Color.argb(255, 184, 184, 184)
                )
            }
        }
    }

    override fun textSizeMode(numberMode: Int) {
        when (numberMode) {

            0 -> settingsView?.textSize(14f, 16f)

            1 -> settingsView?.textSize(16f, 18f)

            2 -> settingsView?.textSize(18f, 20f)

            3 -> settingsView?.textSize(20f, 22f)

            4 -> settingsView?.textSize(24f, 26f)

            5 -> settingsView?.textSize(30f, 32f)
        }
    }
}