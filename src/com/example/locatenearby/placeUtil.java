
package com.example.locatenearby;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class placeUtil {
	static public class placeJSONParser {
		static ArrayList<Place> parsePlace(String in) throws JSONException {
			ArrayList<Place> placeList = new ArrayList<Place>();
			JSONObject object = new JSONObject(in);
			Log.d("JSON", in);
			
			JSONArray placesJSONArray = object.getJSONArray("results");
			for (int i = 0; i < placesJSONArray.length(); i++) {
				Place place = new Place();
				JSONObject IthplaceObj = placesJSONArray.getJSONObject(i);
				JSONObject geometryObj = IthplaceObj.getJSONObject("geometry");
				JSONObject locationObj = geometryObj.getJSONObject("location");
				place.setLatitude(locationObj.getDouble("lat"));
				place.setLongitute(locationObj.getDouble("lng"));
				place.setName(IthplaceObj.getString("name"));
				place.setUrl_icon(IthplaceObj.getString("icon"));
				try{
					JSONObject openhrsobj= IthplaceObj.getJSONObject("opening_hours");
					place.setIsOpenNow(openhrsobj.getBoolean("open_now"));
				}
				catch(JSONException ex)
				{
					place.setIsOpenNow(true);
				}
				finally{
					placeList.add(place);
					Log.d("place"+i, place.toString());
				}
				
			}
			
			return placeList;
		}
	}
}
