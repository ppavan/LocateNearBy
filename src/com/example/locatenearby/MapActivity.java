package com.example.locatenearby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

import org.json.JSONException;



import com.example.homework8.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.squareup.picasso.Picasso;

public class MapActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	GoogleMap gMap;
	ArrayList<Place> data;
	Double currentLat, currentLng;
	int i;
	Place placei;
	RuntimeExceptionDao<Place, String> keysDao;
	DatabaseHelper dbHelper1;
	// int index;
	String PlaceType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		String requestUrl = getIntent().getExtras().getString("ParseURL");
		currentLat = getIntent().getExtras().getDouble("currentLat");
		currentLng = getIntent().getExtras().getDouble("currentLng");
		PlaceType= getIntent().getExtras().getString("PlaceType");
		if (isConnectedOnline()) {
			//new GetPlacesAsyncTask(MapActivity.this).execute(requestUrl);
			new GetPlacesAsync().execute(requestUrl);
		} else {
			Toast.makeText(MapActivity.this, "No Network Connection",
					Toast.LENGTH_LONG).show();
		}

	}

	/*public void setMarkers(ArrayList<Place> placeList) {
		data = new ArrayList<Place>();
		data = placeList;
		Log.d("data", data.toString());
		Log.d("datasize", data.size()+" ");
		gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				currentLat, currentLng), 10));

		for (i = 0; i < data.size(); i++) {
			// index=i;
			//placei = data.get(i);
			gMap.addMarker(
					new MarkerOptions().position(
							new LatLng(data.get(i).getLatitude(), data.get(i)
									.getLongitute())).title(
							data.get(i).getName()));//.showInfoWindow();
			gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
				
				@Override
				public View getInfoWindow(Marker marker) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public View getInfoContents(Marker marker) {
					View v = getLayoutInflater().inflate(R.layout.markercustom,
							null);
					ImageView icon = (ImageView) v.findViewById(R.id.imageView1);
					TextView place_name = (TextView) v.findViewById(R.id.textViewx1);
					TextView place_type = (TextView) v.findViewById(R.id.textViewx2);
					Log.d("demo", i+ " "+ i);
					Log.d("demo", data.get(i).getName());
					place_name.setText(data.get(i).getName());
					place_type.setText(data.get(i).getType());
					return v;
				}
			});
			
			gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker marker) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					

				}
			});

		
			gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker marker) {
										return false;
				}
			});
			

			
			 * MarkerOptions markerOptions = new MarkerOptions() .position(new
			 * LatLng(data.get(i).getLatitude(), data.get(i) .getLongitute()));
			 * Marker marker = gMap.addMarker(markerOptions);
			 * marker.showInfoWindow();
			 
			
			 *  Marker marker = gMap.addMarker(markerOptions);  
			 *                 // Showing InfoWindow on the GoogleMap
			 *                 marker.showInfoWindow();
			 

		}

	}
*/	
	private class GetPlacesAsync extends AsyncTask<String, Void, ArrayList<Place>> {
		/*MapActivity activity;
		ProgressDialog progressDialog; 

		public GetPlacesAsyncTask(MapActivity activity) {
			this.activity = activity;
		}
*/		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Place> doInBackground(String... params) {
			URL url;
			try {
				url = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = reader.readLine();

					while (line != null) {
						sb.append(line);
						line = reader.readLine();
					}
					
					return placeUtil.placeJSONParser.parsePlace(sb.toString());
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) {
			if (result != null) {
				data = result;
				Log.d("data", data.toString());
				Log.d("datasize", data.size()+" ");
				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						currentLat, currentLng), 10));

				for (i = 0; i < data.size()-1; i++) {
					//final int index=i;
					placei = data.get(i);
					gMap.addMarker(
							new MarkerOptions().position(
									new LatLng(data.get(i).getLatitude(), data.get(i)
											.getLongitute())).title(
									data.get(i).getName()));//.showInfoWindow();
					
				}
				gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
					
					@Override
					public View getInfoWindow(Marker marker) {
						
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public View getInfoContents(Marker marker) {
						View v = getLayoutInflater().inflate(R.layout.markercustom,
								null);
						ImageView icon = (ImageView) v.findViewById(R.id.imageView1);
						TextView place_name = (TextView) v.findViewById(R.id.textViewx1);
						TextView place_type = (TextView) v.findViewById(R.id.textViewx2);
						//Log.d("demo", index+ " "+ i);
						//Log.d("demo", data.get(index).getName());
						String MID = marker.getId().substring(1);
						int index= Integer.parseInt(MID);
						Picasso.with(MapActivity.this)
						.load(data.get(index).getUrl_icon()).into(icon);
						place_name.setText(data.get(index).getName());
						//place_type.setText(data.get(index).getType());
						//place_name.setText(marker.getTitle());
						if(data.get(index).getIsOpenNow()){
							place_name.setTextColor(Color.RED);
						}
						
						return v;
					}
				});
				gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick(Marker marker) {
						// TODO Auto-generated method stub
						String MID= marker.getId().substring(1);
						int id= Integer.parseInt(MID);
						dbHelper1 = OpenHelperManager.getHelper(MapActivity.this,
								DatabaseHelper.class);
						keysDao = dbHelper1.getKeysRuntimeExceptionDao();
						
						keysDao.create(new Place(data.get(id).getName(), PlaceType, data.get(id).getUrl_icon(), data.get(id).getIsOpenNow(), data.get(id).getLongitute(),data.get(id).getLatitude()));
						

						if (dbHelper1 != null) {
							dbHelper1.close();
							dbHelper1 = null;
						}
					}
				});
			} else {
				Toast.makeText(MapActivity.this, "No value Found!", Toast.LENGTH_LONG)
						.show();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favourite, menu);
		return true;
	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("inmenu", "inmenu");
		Intent intent = new Intent(MapActivity.this, FavoritesActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
	private boolean isConnectedOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
}
