package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceFragment

class PrefsMenuActivity : Activity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefs_menu)
    }

    class RSSPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferencias)
        }
    }
}