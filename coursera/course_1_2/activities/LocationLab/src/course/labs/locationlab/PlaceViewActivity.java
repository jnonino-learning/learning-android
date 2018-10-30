package course.labs.locationlab;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PlaceViewActivity extends ListActivity implements LocationListener {
	private static final long FIVE_MINS = 5 * 60 * 1000;
	private static final String TAG = "Lab-Location";

	// Set to false if you don't have network access
	public static boolean sHasNetwork = false;

	private Location mLastLocationReading;
	private PlaceViewAdapter mAdapter;
	private LocationManager mLocationManager;
	private boolean mMockLocationOn = false;

	// default minimum time between new readings
	private long mMinTime = 5000;

	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;

	// A fake location provider used for testing
	private MockLocationProvider mMockLocationProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the app's user interface. This class is a ListActivity,
		// so it has its own ListView. ListView's adapter should be a
		// PlaceViewAdapter

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ListView placesListView = getListView();

		// DONE - add a footerView to the ListView
		// You can use footer_view.xml to define the footer
		final View footerView = getLayoutInflater().inflate(
				R.layout.footer_view, null);

		// DONE - footerView must respond to user clicks, handling 3 cases:
		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "Entered footerView.OnClickListener.onClick()");

				// There is no current location - response is up to you. One
				// good
				// solution is to disable the footerView until you have acquired
				// a
				// location.
				if (mLastLocationReading == null) {
					footerView.setEnabled(false);
					return;
				} else {
					footerView.setEnabled(true);
				}

				// There is a current location, but the user has already
				// acquired a
				// PlaceBadge for this location. In this case issue a Toast
				// message with the text -
				// "You already have this location badge."
				// Use the PlaceRecord class' intersects() method to determine
				// whether
				// a PlaceBadge already exists for a given location.
				for (int i = 0; i < mAdapter.getCount(); i++) {
					PlaceRecord adapterItem = (PlaceRecord) mAdapter.getItem(i);
					if (adapterItem.intersects(mLastLocationReading)) {
						Toast.makeText(getApplicationContext(),
								R.string.duplicate_location_string,
								Toast.LENGTH_LONG).show();
						return;
					}
				}

				// There is a current location for which the user does not
				// already have
				// a PlaceBadge. In this case download the information needed to
				// make a new
				// PlaceBadge.
				PlaceDownloaderTask downloader = new PlaceDownloaderTask(
						PlaceViewActivity.this, sHasNetwork);
				downloader.execute(mLastLocationReading);
			}
		});

		placesListView.addFooterView(footerView);
		mAdapter = new PlaceViewAdapter(getApplicationContext());
		setListAdapter(mAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

		startMockLocationManager();

		// DONE - Check NETWORK_PROVIDER for an existing location reading.
		// Only keep this last reading if it is fresh - less than 5 minutes old
		Location location = mLocationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null && ageInMilliseconds(location) < FIVE_MINS) {
			mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		// DONE - register to receive location updates from NETWORK_PROVIDER
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance, this);
	}

	@Override
	protected void onPause() {
		// DONE - unregister for location updates
		mLocationManager.removeUpdates(this);

		shutdownMockLocationManager();
		super.onPause();
	}

	// Callback method used by PlaceDownloaderTask
	public void addNewPlace(PlaceRecord place) {
		Log.i(TAG, "Entered addNewPlace()");

		// DONE - Attempt to add place to the adapter, considering the following
		// cases

		// The place is null. In this case issue a Toast message with the text
		// "PlaceBadge could not be acquired"
		// Do not add the PlaceBadge to the adapter
		if (place == null) {
			Toast.makeText(getApplicationContext(),
					"PlaceBadge could not be acquired", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// A PlaceBadge for this location already exists. In this case issue a
		// Toast message
		// with the text - "You already have this location badge." Use the
		// PlaceRecord
		// class' intersects() method to determine whether a PlaceBadge already
		// exists
		// for a given location. Do not add the PlaceBadge to the adapter
		for (int i = 0; i < mAdapter.getCount(); i++) {
			PlaceRecord adapterItem = (PlaceRecord) mAdapter.getItem(i);
			if (place.intersects(adapterItem.getLocation())) {
				Toast.makeText(getApplicationContext(),
						R.string.duplicate_location_string, Toast.LENGTH_LONG)
						.show();
				return;
			}
		}

		// The place has no country name. In this case issue a Toast message
		// with the text - "There is no country at this location".
		// Do not add the PlaceBadge to the adapter
		if (place.getCountryName() == null || place.getCountryName().isEmpty()) {
			Toast.makeText(getApplicationContext(), R.string.no_country_string,
					Toast.LENGTH_LONG).show();
			return;
		}

		// Otherwise - add the PlaceBadge to the adapter
		mAdapter.add(place);
	}

	// LocationListener methods
	@Override
	public void onLocationChanged(Location currentLocation) {

		// DONE - Update last location considering the following cases.

		// 1) If there is no last location, set the last location to the current
		// location.
		if (mLastLocationReading == null) {
			mLastLocationReading = currentLocation;
		}
		// 2) If the current location is older than the last location, ignore
		// the current location
		else if (ageInMilliseconds(currentLocation) > ageInMilliseconds(mLastLocationReading)) {

		}
		// 3) If the current location is newer than the last locations, keep the
		// current location.
		else if (ageInMilliseconds(currentLocation) <= ageInMilliseconds(mLastLocationReading)) {
			mLastLocationReading = currentLocation;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// not implemented
	}

	@Override
	public void onProviderEnabled(String provider) {
		// not implemented
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// not implemented
	}

	// Returns age of location in milliseconds
	private long ageInMilliseconds(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.delete_badges :
				mAdapter.removeAllViews();
				return true;
			case R.id.place_one :
				mMockLocationProvider.pushLocation(37.422, -122.084);
				return true;
			case R.id.place_no_country :
				mMockLocationProvider.pushLocation(0, 0);
				return true;
			case R.id.place_two :
				mMockLocationProvider.pushLocation(38.996667, -76.9275);
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	private void shutdownMockLocationManager() {
		if (mMockLocationOn) {
			mMockLocationProvider.shutdown();
		}
	}

	private void startMockLocationManager() {
		if (!mMockLocationOn) {
			mMockLocationProvider = new MockLocationProvider(
					LocationManager.NETWORK_PROVIDER, this);
		}
	}
}
