package jmapps.supplicationsfromquran.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.supplicationsfromquran.R
import kotlinx.android.synthetic.main.bottomsheet_settings.*
import kotlinx.android.synthetic.main.bottomsheet_settings.view.*

class BottomSheetSettings : BottomSheetDialogFragment(), RadioGroup.OnCheckedChangeListener,
    SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var rootSettings: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.bottomsheet_settings, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        rootSettings.rbWhiteMode.isChecked = preferences.getBoolean("key_white_state", true)
        rootSettings.rbSepiaMode.isChecked = preferences.getBoolean("key_sepia_state", false)
        rootSettings.rbNightMode.isChecked = preferences.getBoolean("key_night_mode_state", false)

        when (resources.configuration.smallestScreenWidthDp) {

            in 0..599 -> {
                rootSettings.sbTextSizeAyahs.progress = preferences.getInt("key_text_arabic_size_progress", 2)
                rootSettings.sbTextSizeTranslations.progress = preferences.getInt("key_text_translation_size_progress", 2)
            }

            in 600..719 -> {
                rootSettings.sbTextSizeAyahs.progress = preferences.getInt("key_text_arabic_size_progress", 3)
                rootSettings.sbTextSizeTranslations.progress = preferences.getInt("key_text_translation_size_progress", 3)
            }

            in 720..1080 -> {
                rootSettings.sbTextSizeAyahs.progress = preferences.getInt("key_text_arabic_size_progress", 4)
                rootSettings.sbTextSizeTranslations.progress = preferences.getInt("key_text_translation_size_progress", 4)
            }
        }

        rootSettings.swAyahTranslation.isChecked = preferences.getBoolean("key_show_translation_text", true)

        rootSettings.rgModeBackground.setOnCheckedChangeListener(this)
        rootSettings.sbTextSizeAyahs.setOnSeekBarChangeListener(this)
        rootSettings.sbTextSizeTranslations.setOnSeekBarChangeListener(this)
        rootSettings.swAyahTranslation.setOnCheckedChangeListener(this)

        return rootSettings
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (group?.id) {

            R.id.rgModeBackground -> {
                editor.putBoolean("key_white_state", rbWhiteMode.isChecked).apply()
                editor.putBoolean("key_sepia_state", rbSepiaMode.isChecked).apply()
                editor.putBoolean("key_night_mode_state", rbNightMode.isChecked).apply()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.sbTextSizeAyahs -> editor.putInt("key_text_arabic_size_progress", progress).apply()

            R.id.sbTextSizeTranslations -> editor.putInt("key_text_translation_size_progress", progress).apply()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        editor.putBoolean("key_show_translation_text", isChecked).apply()
    }
}