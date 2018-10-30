package com.nonino.dailyselfie.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.nonino.dailyselfie.MainActivity;
import com.nonino.dailyselfie.R;
import com.nonino.dailyselfie.preferences.UserPreferenceFragment;

public class ReminderNotificationReceiver extends BroadcastReceiver {

	private static final int MY_NOTIFICATION_ID = 0;

	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;

	private Uri soundURI = RingtoneManager
			.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);;
	private final long[] mVibratePattern = {0, 200, 200, 300};

	@Override
	public void onReceive(Context context, Intent intent) {
		// The Intent to be used when the user clicks on the Notification View
		mNotificationIntent = new Intent(context, MainActivity.class);

		// The PendingIntent that wraps the underlying Intent
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		// Build the Notification
		Notification.Builder notificationBuilder = new Notification.Builder(
				context);

		CharSequence ticker = context.getResources().getString(
				R.string.selfie_alarm_notification_content_text);
		notificationBuilder.setTicker(ticker);

		CharSequence title = context.getResources().getString(
				R.string.selfie_alarm_notification_title);
		notificationBuilder.setContentTitle(title);

		String notificationText = "";
		String userName = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getString(
						UserPreferenceFragment.USERNAME_PREFERENCE_KEY,
						UserPreferenceFragment.USERNAME_PREFERENCE_DEFAULT_VALUE);

		CharSequence contentText = context.getResources().getString(
				R.string.selfie_alarm_notification_content_text);

		if (!userName.equalsIgnoreCase("")) {
			notificationText.concat(userName);
			notificationText.concat(", ");

		}
		notificationText.concat(contentText.toString());
		notificationBuilder.setContentText(notificationText);

		notificationBuilder.setContentIntent(mContentIntent);

		notificationBuilder.setSmallIcon(android.R.drawable.ic_menu_camera);

		notificationBuilder.setSound(soundURI);

		notificationBuilder.setVibrate(mVibratePattern);

		notificationBuilder.setAutoCancel(true);

		// Get the NotificationManager
		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNM.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
	}

}
