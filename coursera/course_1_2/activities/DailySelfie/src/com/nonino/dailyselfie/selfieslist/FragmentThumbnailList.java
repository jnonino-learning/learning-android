package com.nonino.dailyselfie.selfieslist;

import java.io.File;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.nonino.dailyselfie.R;
import com.nonino.dailyselfie.db.DatabaseOpenHelper;
import com.nonino.dailyselfie.db.SelfieItem;
import com.nonino.dailyselfie.db.ThumbnailCursorAdapter;
import com.nonino.dailyselfie.db.ThumbnailCursorAdapter.ViewHolder;

public class FragmentThumbnailList extends ListFragment
		implements
			OnItemClickListener,
			OnItemLongClickListener {

	private ThumbnailCursorAdapter mAdapter;

	private ThumbnailFragmentCallback mCallbacks;
	private DatabaseOpenHelper mDBOpenHelper;

	public FragmentThumbnailList() {

	}

	public FragmentThumbnailList(ThumbnailFragmentCallback callback) {
		this.mCallbacks = callback;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDBOpenHelper = new DatabaseOpenHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		final View view = inflater.inflate(R.layout.fragment_thumbnail_list,
				container, false);

		Cursor c = getNewCursor();
		mAdapter = new ThumbnailCursorAdapter(getActivity(), c, true);
		setListAdapter(mAdapter);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
	}
	public void addNewSelfie(SelfieItem item) {
		ContentValues values = new ContentValues();

		values.put(DatabaseOpenHelper.TITLE, item.getImageTitle());
		values.put(DatabaseOpenHelper.PATH, item.getFullSizePicturePath());

		mDBOpenHelper.getWritableDatabase().insert(
				DatabaseOpenHelper.TABLE_NAME, null, values);

		mAdapter.swapCursor(getNewCursor());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Cursor cursor = (Cursor) mAdapter.getItem(position);

		int titleIndex = cursor.getColumnIndex(DatabaseOpenHelper.TITLE);
		int pathIndex = cursor.getColumnIndex(DatabaseOpenHelper.PATH);

		SelfieItem item = new SelfieItem(cursor.getString(titleIndex),
				cursor.getString(pathIndex));

		mCallbacks.openFullImageFragment(item.getFullSizePicturePath());
	}

	private Cursor getNewCursor() {
		return mDBOpenHelper.getWritableDatabase().query(
				DatabaseOpenHelper.TABLE_NAME, DatabaseOpenHelper.columns,
				null, new String[]{}, null, null, null);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.alert_delete_message)
				.setCancelable(false)
				.setPositiveButton(R.string.alert_delete_confirm,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								deleteSelfie(view, position);
								Toast.makeText(getActivity(),
										R.string.alert_delete_ack,
										Toast.LENGTH_LONG).show();
							}
						})
				.setNegativeButton(R.string.alert_delete_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();

		return true;
	}

	private void deleteSelfie(View view, int position) {
		Cursor cursor = (Cursor) mAdapter.getItem(position);

		int titleIndex = cursor.getColumnIndex(DatabaseOpenHelper.TITLE);
		int pathIndex = cursor.getColumnIndex(DatabaseOpenHelper.PATH);

		SelfieItem item = new SelfieItem(cursor.getString(titleIndex),
				cursor.getString(pathIndex));
		File file = new File(item.getFullSizePicturePath());
		boolean deleted = file.delete();

		if (deleted) {
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			String selfieToDelete = viewHolder.thumbnailTitle.getText()
					.toString();

			mDBOpenHelper.getWritableDatabase().delete(
					DatabaseOpenHelper.TABLE_NAME,
					DatabaseOpenHelper.TITLE + "=?",
					new String[]{selfieToDelete});

			mAdapter.swapCursor(getNewCursor());
		}
	}

}
