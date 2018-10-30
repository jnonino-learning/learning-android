package com.nonino.dailyselfie.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.nonino.dailyselfie.R;
import com.nonino.dailyselfie.reminder.SelfieReminder;

public class UserPreferenceFragment extends PreferenceFragment
		implements
			OnSharedPreferenceChangeListener {

	public static final String USERNAME_PREFERENCE_KEY = "username";
	public static final String ALARM_CHECKBOX_KEY = "alarm_time_checkbox_preference";
	public static final String ALARM_LIST_KEY = "alarm_time_list_preference";

	// TODO Create and get default value
	public static final String USERNAME_PREFERENCE_DEFAULT_VALUE = "";

	// TODO Create and get default value
	public static final String REMINDER_INTERVAL_DEFAULT_VALUE = "";

	private Preference mUserNamePreference;
	private CheckBoxPreference mReminderCheckboxPreference;
	private ListPreference mReminderListPreference;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.user_preferences);

		mUserNamePreference = (Preference) getPreferenceManager()
				.findPreference(USERNAME_PREFERENCE_KEY);

		mReminderCheckboxPreference = (CheckBoxPreference) getPreferenceManager()
				.findPreference(ALARM_CHECKBOX_KEY);

		mReminderListPreference = (ListPreference) getPreferenceManager()
				.findPreference(ALARM_LIST_KEY);

		// Get SharedPreferences object managed by the PreferenceManager for
		// this Fragment
		prefs = getPreferenceManager().getSharedPreferences();

		// Register a listener on the SharedPreferences object
		prefs.registerOnSharedPreferenceChangeListener(this);

		// Invoke callback manually to display the current settings values
		String userName = prefs.getString(USERNAME_PREFERENCE_KEY,
				USERNAME_PREFERENCE_DEFAULT_VALUE);
		mUserNamePreference.setSummary(userName);

		String entry = "";
		CharSequence selectedEntry = mReminderListPreference.getEntry();
		if (selectedEntry != null) {
			entry = selectedEntry.toString();
			mReminderListPreference.setSummary("Every " + entry);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equalsIgnoreCase(USERNAME_PREFERENCE_KEY)) {
			String userName = sharedPreferences.getString(
					USERNAME_PREFERENCE_KEY, USERNAME_PREFERENCE_DEFAULT_VALUE);
			mUserNamePreference.setSummary(userName);
		} else if (key.equalsIgnoreCase(ALARM_CHECKBOX_KEY)) {
			boolean checked = mReminderCheckboxPreference.isChecked();
			if (checked) {
				this.onSharedPreferenceChanged(prefs, ALARM_LIST_KEY);
			} else {
				SelfieReminder.getInstance().cancelAlarm(
						SelfieReminder.getInstance()
								.getMNotificationReceiverPendingIntent());
			}
			mReminderListPreference.setEnabled(checked);

		} else if (key.equalsIgnoreCase(ALARM_LIST_KEY)) {
			String entry = "";
			CharSequence selectedEntry = mReminderListPreference.getEntry();
			if (selectedEntry != null) {
				entry = selectedEntry.toString();
			}
			mReminderListPreference.setSummary("Every " + entry);

			String intervalString = prefs.getString(
					UserPreferenceFragment.ALARM_LIST_KEY,
					UserPreferenceFragment.REMINDER_INTERVAL_DEFAULT_VALUE);

			long interval = Long.parseLong(intervalString, 10) * 1000L;

			SelfieReminder.getInstance().setAlarm(entry, interval);
		}
	}
}
