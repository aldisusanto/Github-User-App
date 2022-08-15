package com.android.submission2.pref

import android.content.Context

class ReminderPreferences(context: Context) {
    companion object {
        const val PREFS_NAME = "com.android.submission2.prefs"
        const val PREFS_TIME_REMINDER_DEFAULT = "09:00"
        private const val EXTRA_REMINDER_SET = "extra_reminder_set"
        private const val EXTRA_REMINDER_TIME = "extra_reminder_time"
    }

    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isAlarmAlreadySet(): Boolean = pref.getBoolean(EXTRA_REMINDER_SET, false)

    fun updateAlarmSetup(state: Boolean, time: String?) {
        pref.edit().apply {
            putBoolean(EXTRA_REMINDER_SET, state)
            if (state) {
                putString(EXTRA_REMINDER_TIME, time ?: "")
            } else {
                remove(EXTRA_REMINDER_TIME)
            }
            apply()
        }
    }

    fun getTimeOfActiveReminder(): String? = pref.getString(
        EXTRA_REMINDER_TIME,
        PREFS_TIME_REMINDER_DEFAULT
    )
}