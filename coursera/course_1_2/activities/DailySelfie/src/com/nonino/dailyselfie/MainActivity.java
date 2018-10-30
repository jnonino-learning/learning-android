package com.nonino.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nonino.dailyselfie.db.SelfieItem;
import com.nonino.dailyselfie.fullSizeImage.FullImageFragment;
import com.nonino.dailyselfie.preferences.UserPreferenceFragment;
import com.nonino.dailyselfie.selfieslist.FragmentThumbnailList;
import com.nonino.dailyselfie.selfieslist.ThumbnailFragmentCallback;

public class MainActivity extends Activity implements ThumbnailFragmentCallback {

	private static final String TAG = MainActivity.class.getSimpleName();
	static final int REQUEST_IMAGE_CAPTURE = 1;

	public static Context mContext;

	private static long SelfieReminderInterval = 0;

	private FragmentThumbnailList fragment;
	private static String mLastTakenSelfiePath;
	private static String mLastTakenSelfieTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			fragment = new FragmentThumbnailList(this);
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}

		mContext = getApplicationContext();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			UserPreferenceFragment fragment = new UserPreferenceFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			return true;
		} else if (id == R.id.action_camera) {
			takePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			SelfieItem item = new SelfieItem(mLastTakenSelfieTitle,
					mLastTakenSelfiePath);
			addPictureToGallery(mLastTakenSelfiePath);
			fragment.addNewSelfie(item);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void takePictureIntent() {
		File photoFile = null;
		try {
			photoFile = createImageFile();
		} catch (IOException ex) {
			Log.e(TAG, "Error creating photoFile");
			ex.printStackTrace();
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}
	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ")
				.format(new Date());
		String imageFileName = timeStamp;
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);

		mLastTakenSelfieTitle = imageFileName;
		mLastTakenSelfiePath = image.getAbsolutePath();
		return image;
	}

	private void addPictureToGallery(String imagePath) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(imagePath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	@Override
	public void openFullImageFragment(String fullImagePath) {
		FullImageFragment fragment = new FullImageFragment();
		Bundle args = new Bundle();
		args.putString(FullImageFragment.IMAGE_PATH, fullImagePath);
		fragment.setArguments(args);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.container, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public static long getSelfieReminderInterval() {
		return SelfieReminderInterval;
	}

	public static void setSelfieReminderInterval(long selfieReminderInterval) {
		SelfieReminderInterval = selfieReminderInterval;
	}
}
