package com.nonino.dailyselfie.db;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SelfieItem {

	private static final int THUMBNAIL_SIZE = 128;

	Bitmap thumbnail;
	String fullSizePicturePath;
	String imageTitle;

	public SelfieItem(String imageTitle, String fullSizePicturePath) {
		this.imageTitle = imageTitle;
		this.thumbnail = getPreview(fullSizePicturePath);
		this.fullSizePicturePath = fullSizePicturePath;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public String getFullSizePicturePath() {
		return fullSizePicturePath;
	}

	public String getImageTitle() {
		return imageTitle;
	}

	private Bitmap getPreview(String path) {
		File image = new File(path);
		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(image.getPath(), bounds);
		if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
			return null;
		int originalSize = (bounds.outHeight > bounds.outWidth)
				? bounds.outHeight
				: bounds.outWidth;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = originalSize / THUMBNAIL_SIZE;
		return BitmapFactory.decodeFile(image.getPath(), opts);
	}

}
