package com.example.locatenearby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GetPlacesAsyncTask extends AsyncTask<String, Void, ArrayList<Place>> {
	MapActivity activity;
	ProgressDialog progressDialog; 

	public GetPlacesAsyncTask(MapActivity activity) {
		this.activity = activity;
	}
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
			//activity.setMarkers(result);
		} else {
			Toast.makeText(activity, "No value Found!", Toast.LENGTH_LONG)
					.show();
		}
		super.onPostExecute(result);
	}

}
