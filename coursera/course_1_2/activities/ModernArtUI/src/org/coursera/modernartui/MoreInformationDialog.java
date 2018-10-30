package org.coursera.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class MoreInformationDialog extends DialogFragment {

    protected String URL = "http://www.moma.org";
    protected CharSequence CHOOSER_TEXT;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

	CHOOSER_TEXT = getString(R.string.dialog_load_website_text, URL);

	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	LayoutInflater inflater = getActivity().getLayoutInflater();

	View view = inflater.inflate(R.layout.dialog_more_information, null);
	builder.setView(view);

	builder.setPositiveButton(R.string.dialog_visit_moma_button,
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			Intent baseIntent = null;
			Uri uri = Uri.parse(URL);
			baseIntent = new Intent(Intent.ACTION_VIEW, uri);
			Intent chooserIntent = null;
			chooserIntent = Intent.createChooser(baseIntent,
				CHOOSER_TEXT);
			if (baseIntent.resolveActivity(getActivity()
				.getPackageManager()) != null) {
			    startActivity(chooserIntent);
			}
		    }
		});

	builder.setNegativeButton(R.string.dialog_not_now_button,
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// Cancel
		    }
		});

	AlertDialog alert = builder.create();
	alert.show();

	// Formatting positive button
	Button positiveButton = alert
		.getButton(DialogInterface.BUTTON_POSITIVE);
	positiveButton
		.setBackgroundColor(getResources().getColor(R.color.dialog_color));
	positiveButton.setTextColor(getResources().getColor(R.color.dialog_text_color));

	// Formatting negative button
	Button negativeButton = alert
		.getButton(DialogInterface.BUTTON_NEGATIVE);
	negativeButton
		.setBackgroundColor(getResources().getColor(R.color.dialog_color));
	negativeButton.setTextColor(getResources().getColor(R.color.dialog_text_color));

	return alert;
    }
}
