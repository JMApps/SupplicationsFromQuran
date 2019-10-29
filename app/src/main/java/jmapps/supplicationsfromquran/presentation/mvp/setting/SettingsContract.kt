package jmapps.supplicationsfromquran.presentation.mvp.setting

interface SettingsContract {

    interface SettingsView {

        fun colorMode(backgroundColor: Int, textArabicColor: Int, textTranslationColor: Int)

        fun textSize(textArabicSize: Float, textTranslationSize: Float)

    }

    interface SettingsPresenter {

        fun backgroundMode(numberMode: Int)

        fun textSizeMode(numberMode: Int)
    }
}