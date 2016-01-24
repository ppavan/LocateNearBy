
package com.example.locatenearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	LocationManager mLocationMngr;
	LocationListener mLocListener;
	String placeTypeSelected;
	int radius = 0;
	double latitude = 0, longitude = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationMngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SeekBar sb = (SeekBar) findViewById(R.id.seekBar1);
		sb.setMax(50000);
		final TextView tvRadius = (TextView) findViewById(R.id.textViewx2);
		ImageView menuImg = (ImageView) findViewById(R.id.imageView1);
		Button searchButton = (Button) findViewById(R.id.button1);

		menuImg.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final String[] placeTypes = { "airport", "atm", "bank", "cafe",
						"parking", "food", "school" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("Place Types");
				builder.setItems(placeTypes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								placeTypeSelected = placeTypes[which];
								Log.d("typeselected", placeTypeSelected);
							}
						});
				AlertDialog alert = builder.create();
				alert.show();

				return false;
			}
		});

		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvRadius.setText("Radius: " + progress + "m");
				radius = progress;
			}
		});

		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mLocationMngr
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setTitle("GPS Not enabled")
							.setMessage(
									"Would you like to enable GPS settings?")
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													Settings.ACTION_LOCATION_SOURCE_SETTINGS);
											startActivity(intent);
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
											finish();
										}
									});
					AlertDialog alert = builder.show();
				} else {
					mLocListener = new LocationListener() {

						@Override
						public void onStatusChanged(String provider,
								int status, Bundle extras) {
						}

						@Override
						public void onProviderEnabled(String provider) {
						}

						@Override
						public void onProviderDisabled(String provider) {
						}

						@Override
						public void onLocationChanged(Location location) {
							//currentLoc = location;
							Log.d("demo", location.getLatitude() + ", "
									+ location.getLongitude());
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							String apiUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
							String details = latitude + "," + longitude
									+ "&radius=" + radius + "&types="
									+ placeTypeSelected + "&key="
									+ "AIzaSyDw3Lr4XsiL9-cuNy9ocom4w7QbfLkpiYY";
									//+ "AIzaSyAvq7VNXrgMpBg1p3aTZ6JQnxxBfFaFJ2Y";
							String requestUrl = apiUrl + details;
							Log.d("url", requestUrl);
							Intent intent = new Intent(MainActivity.this, MapActivity.class);
							intent.putExtra("ParseURL", requestUrl);
							intent.putExtra("currentLat", latitude);
							intent.putExtra("currentLng", longitude);
							intent.putExtra("PlaceType", placeTypeSelected );
							startActivity(intent);
						}
					};
					mLocationMngr.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 0, 10, mLocListener);					

				}
			}
		});
	}

}
