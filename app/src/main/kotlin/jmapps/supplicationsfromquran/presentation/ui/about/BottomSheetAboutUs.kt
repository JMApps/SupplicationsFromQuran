package jmapps.supplicationsfromquran.presentation.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.supplicationsfromquran.R

class BottomSheetAboutUs : BottomSheetDialogFragment() {

    private lateinit var rootAboutUs: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootAboutUs = inflater.inflate(R.layout.bottomsheet_about_us, container, false)

        return rootAboutUs
    }
}