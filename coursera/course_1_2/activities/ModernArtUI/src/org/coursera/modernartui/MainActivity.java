package org.coursera.modernartui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends FragmentActivity {

    public static final int SEEK_BAR_MAX_VALUE = 100;

    private SeekBar seekBar;

    private LinearLayout boxOneOne;
    private LinearLayout boxOneTwo;
    private LinearLayout boxTwoOne;
    private LinearLayout boxTwoTwo;
    private LinearLayout boxTwoThree;

    private ColorManager colorManagerInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	colorManagerInstance = ColorManager
		.getInstance(getApplicationContext());

	boxOneOne = (LinearLayout) findViewById(R.id.boxOneOne);
	boxOneTwo = (LinearLayout) findViewById(R.id.boxOneTwo);
	boxTwoOne = (LinearLayout) findViewById(R.id.boxTwoOne);
	boxTwoTwo = (LinearLayout) findViewById(R.id.boxTwoTwo);
	boxTwoThree = (LinearLayout) findViewById(R.id.boxTwoThree);

	seekBar = (SeekBar) findViewById(R.id.seekBar1);
	seekBar.setMax(SEEK_BAR_MAX_VALUE);

	seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar) {
	    }

	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar) {
	    }

	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress,
		    boolean fromUser) {
		int color = colorManagerInstance.getBoxOneOneNewColor(progress);
		boxOneOne.setBackgroundColor(color);

		color = colorManagerInstance.getBoxOneTwoNewColor(progress);
		boxOneTwo.setBackgroundColor(color);

		color = colorManagerInstance.getBoxTwoOneNewColor(progress);
		boxTwoOne.setBackgroundColor(color);

		color = colorManagerInstance.getBoxTwoTwoNewColor(progress);
		boxTwoTwo.setBackgroundColor(color);

		color = colorManagerInstance.getBoxTwoThreeNewColor(progress);
		boxTwoThree.setBackgroundColor(color);
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int id = item.getItemId();
	if (id == R.id.action_more_information) {
	    DialogFragment moreInformationDialog = new MoreInformationDialog();
	    moreInformationDialog.show(getSupportFragmentManager(),
		    "MoreInfoDialog");
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }
}
