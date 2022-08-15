package com.android.submission2.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import com.android.submission2.R
import com.android.submission2.databinding.ActivitySettingBinding
import com.android.submission2.pref.ReminderPreferences
import com.android.submission2.receiver.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    companion object {
        private const val TIME_PICKER_TAG = "TimePickerOnce"
    }

    private lateinit var binding: ActivitySettingBinding
    private lateinit var receiver: AlarmReceiver
    private lateinit var reminder: ReminderPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.apply {
            title = getString(R.string.string_action_bar_setting)
            setDisplayHomeAsUpEnabled(true)
        }
        receiver = AlarmReceiver()
        reminder = ReminderPreferences(this)

        
        binding.apply {

            labelSettingLanguageDefault.text = Locale.getDefault().displayLanguage
            labelSettingLanguageDefault.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            switchSettingsReminder.isChecked = reminder.isAlarmAlreadySet()
            switchSettingsReminder.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val time = binding.labelSettingSetTimeLabel.text.toString()
                    receiver.setRepeatingAlarm(
                        this@SettingActivity,
                        time,
                        resources.getString(R.string.string_receiver_message)
                    )
                } else {
                    receiver.cancelAlarm(this@SettingActivity)
                }
            }

            labelSettingSetTimeLabel.text = reminder.getTimeOfActiveReminder()
            labelSettingSetTimeLabel.setOnClickListener {
                changeTimeReminder()
            }

            labelSettingTimeReminder.setOnClickListener {
                changeTimeReminder()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.labelSettingSetTimeLabel.text = dateFormat.format(calendar.time)
    }


    private fun changeTimeReminder() {
        TimePickerFragment().show(supportFragmentManager, TIME_PICKER_TAG)
    }
}