package jmapps.supplicationsfromquran

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration


class LockOrientation(private val act: Activity) {

    @SuppressLint("InlinedApi", "ObsoleteSdkInt")
    fun lock() {
        when (act.resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.FROYO) {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                val rotation = act.windowManager.defaultDisplay.rotation
                if (rotation == android.view.Surface.ROTATION_90 || rotation == android.view.Surface.ROTATION_180) {
                    act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                } else {
                    act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
            Configuration.ORIENTATION_LANDSCAPE -> if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.FROYO) {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                val rotation = act.windowManager.defaultDisplay.rotation
                if (rotation == android.view.Surface.ROTATION_0 || rotation == android.view.Surface.ROTATION_90) {
                    act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                }
            }
        }
    }
}