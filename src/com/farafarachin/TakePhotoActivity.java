package com.farafarachin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TakePhotoActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_take_photo);

		dispatchTakePictureIntent();

		final Button button = (Button) findViewById(R.id.saveButton);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Bitmap bm =
				mergePhotos();

				// saveImage(bm);
				
				button.setVisibility(View.GONE);
				
				Bitmap bitmap = takeScreenshot();
				
				saveImage(bitmap);
				
				button.setVisibility(View.VISIBLE);
			}
		});
	}

	public Bitmap takeScreenshot() {
		View rootView = findViewById(android.R.id.content).getRootView();
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}

	public void saveBitmap(Bitmap bitmap) {
		File imagePath = new File(Environment.getExternalStorageDirectory()
				+ "/screenshot.png");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e("GREC", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("GREC", e.getMessage(), e);
		}
	}

	private void mergePhotos() {
		//
		// get the bitmap of the photo taked
		//

		final ImageView photoImageView = (ImageView) findViewById(R.id.takedPhotoImageView);

		Bitmap photoImageBitmap = ((BitmapDrawable) photoImageView
				.getDrawable()).getBitmap();

		//
		// get the image of the avatar
		//

		final ImageView avatarImageView = (ImageView) findViewById(R.id.imageAvatarView);

		Bitmap avatarImageBitmap = ((BitmapDrawable) avatarImageView
				.getDrawable()).getBitmap();

		//
		// get the background
		//

		final ImageView backgroundImageView = (ImageView) findViewById(R.id.imageBackgroundView);

		Bitmap backgroundImageBitmap = ((BitmapDrawable) backgroundImageView
				.getDrawable()).getBitmap();

		// use the canvas to combine them.
		// Start with the first in the constructor..

		Bitmap backgroundMutableBitmap = backgroundImageBitmap.copy(
				Bitmap.Config.ARGB_8888, true);

		Canvas comboImage = new Canvas(backgroundMutableBitmap);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(photoImageBitmap,
				photoImageView.getMeasuredWidth(),
				photoImageView.getMeasuredHeight(), false);

		// Then draw the second on top of that
		comboImage.drawBitmap(resizedBitmap, 20f, 20f, null);

		comboImage.drawBitmap(avatarImageBitmap, 0f, 0f, null);

		//
		//
		//

		OutputStream os = null;
		try {

			String folder = Environment.getExternalStorageDirectory()
					+ File.separator + "farachin" + File.separator;

			String imageName = "img2.png";

			File folderFile = new File(folder);

			if (!folderFile.exists()) {
				folderFile.mkdirs();
			}

			os = new FileOutputStream(folder + imageName);

			backgroundMutableBitmap.compress(CompressFormat.PNG, 100, os);

			// os.flush();
			//
			// os.close();

		} catch (IOException e) {
			Log.e("combineImages", "problem combining images", e);
		}

	}

	private void saveImage(Bitmap bm) {
		OutputStream fOut = null;

		Uri outputFileUri;

		try {

			File root = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "farachin" + File.separator);

			root.mkdirs();

			File sdImageMainDirectory = new File(root, "faceimage.jpg");

			outputFileUri = Uri.fromFile(sdImageMainDirectory);

			fOut = new FileOutputStream(sdImageMainDirectory);

		} catch (Exception e) {

			Toast.makeText(TakePhotoActivity.this,
					"Error occured. Please try again later.",
					Toast.LENGTH_SHORT).show();
		}

		try {
			bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
		}
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");

			final ImageView photoImageView = (ImageView) findViewById(R.id.takedPhotoImageView);

			photoImageView.setImageBitmap(imageBitmap);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_photo, menu);
		return true;
	}

}
