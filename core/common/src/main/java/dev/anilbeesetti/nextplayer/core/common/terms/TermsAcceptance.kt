package dev.anilbeesetti.nextplayer.core.common.terms

import android.content.Context

/**
 * Remembers whether the user accepted the Terms & Conditions.
 *
 * This lives in its own small preferences file, not in the DataStore settings: it can be read
 * instantly (the settings load a moment after launch, which would flash the terms screen at
 * people who already accepted), and "reset settings" does not wipe it.
 *
 * Raise [CURRENT_VERSION] when the terms change to ask everyone to accept them again.
 */
object TermsAcceptance {

    const val CURRENT_VERSION = 1

    private const val PREFS_NAME = "kage_terms"
    private const val KEY_ACCEPTED_VERSION = "accepted_version"

    fun isAccepted(context: Context): Boolean = prefs(context).getInt(KEY_ACCEPTED_VERSION, 0) >= CURRENT_VERSION

    fun accept(context: Context) {
        prefs(context).edit().putInt(KEY_ACCEPTED_VERSION, CURRENT_VERSION).apply()
    }

    private fun prefs(context: Context) = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
