package com.nonino.dailyselfie.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonino.dailyselfie.R;

public class ThumbnailCursorAdapter extends CursorAdapter {

	public ThumbnailCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	public ThumbnailCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View convertView = inflater
				.inflate(R.layout.selfie_item, parent, false);
		ImageView thumbnailIcon = (ImageView) convertView
				.findViewById(R.id.thumbnail_icon);
		TextView thumbnailTitle = (TextView) convertView
				.findViewById(R.id.thumbnail_title);
		convertView.setTag(new ViewHolder(thumbnailIcon, thumbnailTitle));
		return convertView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		ImageView thumbnailIcon = viewHolder.thumbnailIcon;
		TextView thumbnailTitle = viewHolder.thumbnailTitle;
		int titleIndex = cursor.getColumnIndex(DatabaseOpenHelper.TITLE);
		int pathIndex = cursor.getColumnIndex(DatabaseOpenHelper.PATH);
		SelfieItem item = new SelfieItem(cursor.getString(titleIndex),
				cursor.getString(pathIndex));
		thumbnailIcon.setImageBitmap(item.getThumbnail());
		thumbnailTitle.setText(item.getImageTitle());
	}

	public static class ViewHolder {
		public final ImageView thumbnailIcon;
		public final TextView thumbnailTitle;

		public ViewHolder(ImageView icon, TextView title) {
			this.thumbnailIcon = icon;
			this.thumbnailTitle = title;
		}
	}
}
