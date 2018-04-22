package io.github.rajdeep1008.apkwizard.extras

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by rajdeep1008 on 22/04/18.
 */
class AppPreferences(context: Context) {

    init {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var prefEditor = sharedPreferences.edit()
    }

}