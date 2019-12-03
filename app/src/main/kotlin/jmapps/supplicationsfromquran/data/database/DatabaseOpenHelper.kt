package jmapps.supplicationsfromquran.data.database

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import jmapps.supplicationsfromquran.R

private var databaseVersion: Int = 5

class DatabaseOpenHelper(context: Context?) :
    SQLiteAssetHelper(context, context?.getString(R.string.database_name), null, databaseVersion) {
    init {
        setForcedUpgrade(databaseVersion)
    }
}