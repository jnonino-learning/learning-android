package org.coursera.modernartui;

import android.content.Context;
import android.graphics.Color;

public class ColorManager {

    private static ColorManager instance;

    private Context context;

    private int boxOneOneStartColor;
    private int boxOneTwoStartColor;
    private int boxTwoOneStartColor;
    private int boxTwoTwoStartColor;
    private int boxTwoThreeStartColor;

    private float[] boxOneOneSteps = new float[3];
    private float[] boxOneTwoSteps = new float[3];
    private float[] boxTwoOneSteps = new float[3];
    private float[] boxTwoTwoSteps = new float[3];
    private float[] boxTwoThreeSteps = new float[3];

    public static ColorManager getInstance(Context context) {
	if (instance == null) {
	    instance = new ColorManager(context);
	}
	return instance;
    }

    private ColorManager(Context context) {
	this.context = context;

	boxOneOneStartColor = this.context.getResources().getColor(
		R.color.block_one_one_start_color);
	int colorFinal = this.context.getResources().getColor(
		R.color.block_one_one_end_color);
	boxOneOneSteps[0] = getRedStep(boxOneOneStartColor, colorFinal);
	boxOneOneSteps[1] = getGreenStep(boxOneOneStartColor, colorFinal);
	boxOneOneSteps[2] = getBlueStep(boxOneOneStartColor, colorFinal);

	boxOneTwoStartColor = this.context.getResources().getColor(
		R.color.block_one_two_start_color);
	colorFinal = this.context.getResources().getColor(
		R.color.block_one_two_end_color);
	boxOneTwoSteps[0] = getRedStep(boxOneTwoStartColor, colorFinal);
	boxOneTwoSteps[1] = getGreenStep(boxOneTwoStartColor, colorFinal);
	boxOneTwoSteps[2] = getBlueStep(boxOneTwoStartColor, colorFinal);

	boxTwoOneStartColor = this.context.getResources().getColor(
		R.color.block_two_one_start_color);
	colorFinal = this.context.getResources().getColor(
		R.color.block_two_one_end_color);
	boxTwoOneSteps[0] = getRedStep(boxTwoOneStartColor, colorFinal);
	boxTwoOneSteps[1] = getGreenStep(boxTwoOneStartColor, colorFinal);
	boxTwoOneSteps[2] = getBlueStep(boxTwoOneStartColor, colorFinal);

	boxTwoTwoStartColor = this.context.getResources().getColor(
		R.color.block_two_two_start_color);
	colorFinal = this.context.getResources().getColor(
		R.color.block_two_two_end_color);
	boxTwoTwoSteps[0] = getRedStep(boxTwoTwoStartColor, colorFinal);
	boxTwoTwoSteps[1] = getGreenStep(boxTwoTwoStartColor, colorFinal);
	boxTwoTwoSteps[2] = getBlueStep(boxTwoTwoStartColor, colorFinal);

	boxTwoThreeStartColor = this.context.getResources().getColor(
		R.color.block_two_three_start_color);
	colorFinal = this.context.getResources().getColor(
		R.color.block_two_three_end_color);
	boxTwoThreeSteps[0] = getRedStep(boxTwoThreeStartColor, colorFinal);
	boxTwoThreeSteps[1] = getGreenStep(boxTwoThreeStartColor, colorFinal);
	boxTwoThreeSteps[2] = getBlueStep(boxTwoThreeStartColor, colorFinal);
    }

    private float getRedStep(int startColor, int endColor) {
	int start = Color.red(startColor);
	int end = Color.red(endColor);
	return getStep(start, end);
    }

    private float getGreenStep(int startColor, int endColor) {
	int start = Color.green(startColor);
	int end = Color.green(endColor);
	return getStep(start, end);
    }

    private float getBlueStep(int startColor, int endColor) {
	int start = Color.blue(startColor);
	int end = Color.blue(endColor);
	return getStep(start, end);
    }

    private float getStep(int start, int end) {
	float delta = end - start;
	float step = delta / MainActivity.SEEK_BAR_MAX_VALUE;
	return step;
    }

    public int getBoxOneOneNewColor(int progress) {
	return getNewColor(this.boxOneOneStartColor, this.boxOneOneSteps,
		progress);
    }

    public int getBoxOneTwoNewColor(int progress) {
	return getNewColor(this.boxOneTwoStartColor, this.boxOneTwoSteps,
		progress);
    }

    public int getBoxTwoOneNewColor(int progress) {
	return getNewColor(this.boxTwoOneStartColor, this.boxTwoOneSteps,
		progress);
    }

    public int getBoxTwoTwoNewColor(int progress) {
	return getNewColor(this.boxTwoTwoStartColor, this.boxTwoTwoSteps,
		progress);
    }

    public int getBoxTwoThreeNewColor(int progress) {
	return getNewColor(this.boxTwoThreeStartColor, this.boxTwoThreeSteps,
		progress);
    }

    private int getNewColor(int initialColor, float[] steps, int progress) {
	int initialRed = Color.red(initialColor);
	float stepRed = steps[0];
	float redDelta = stepRed * progress;
	int newRed = (int) (initialRed + redDelta);

	int initialGreen = Color.green(initialColor);
	float stepGreen = steps[1];
	float greenDelta = stepGreen * progress;
	int newGreen = (int) (initialGreen + greenDelta);

	int initialBlue = Color.blue(initialColor);
	float stepBlue = steps[2];
	float blueDelta = stepBlue * progress;
	int newBlue = (int) (initialBlue + blueDelta);

	return Color.rgb(newRed, newGreen, newBlue);
    }
}
