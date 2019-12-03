package jmapps.supplicationsfromquran.presentation.mvp.settings

interface SettingsContract {

    interface SettingsView {

        fun colorMode(backgroundColor: Int, textArabicColor: Int, textTranslationColor: Int)

        fun textArabicSize(textArabicSize: Float)

        fun textTranslationSize(textTranslationSize: Float)
    }

    interface SettingsPresenter {

        fun backgroundMode(numberMode: Int)

        fun textArabicSizeMode(numberArabicMode: Int)

        fun textTranslationSizeMode(numberTranslationMode: Int)
    }
}