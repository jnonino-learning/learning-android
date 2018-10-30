package com.nonino.dailyselfie.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.nonino.dailyselfie.MainActivity;

public class SelfieReminder {

	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;

	private static SelfieReminder mInstance;

	private SelfieReminder() {
		mAlarmManager = (AlarmManager) MainActivity.mContext
				.getSystemService(Context.ALARM_SERVICE);
	}

	public static SelfieReminder getInstance() {
		if (mInstance == null) {
			mInstance = new SelfieReminder();
		}
		return mInstance;
	}

	public PendingIntent getMNotificationReceiverPendingIntent() {
		return mNotificationReceiverPendingIntent;
	}

	public void setAlarm(String entry, long interval) {

		long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(MainActivity.mContext,
				ReminderNotificationReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				MainActivity.mContext, 0, mNotificationReceiverIntent, 0);

		mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ INITIAL_ALARM_DELAY, mNotificationReceiverPendingIntent);

		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + interval, interval,
				mNotificationReceiverPendingIntent);

		Toast.makeText(MainActivity.mContext, "Next reminder in " + entry,
				Toast.LENGTH_LONG).show();
	}

	public void cancelAlarm(PendingIntent alarmIntent) {
		mAlarmManager.cancel(alarmIntent);
	}

}
