package com.nonino.dailyselfie.fullSizeImage;

import java.io.File;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nonino.dailyselfie.R;

public class FullImageFragment extends Fragment {

	public static final String IMAGE_PATH = "image_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final View view = inflater.inflate(R.layout.full_image_fragment,
				container, false);
		String path = getArguments().getString(IMAGE_PATH);
		File imgFile = new File(path);
		if (imgFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			ImageView myImage = (ImageView) view
					.findViewById(R.id.full_image_container);
			myImage.setImageBitmap(myBitmap);
		}
		return view;
	}
}
