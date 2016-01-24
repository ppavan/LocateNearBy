
package com.example.locatenearby;
import java.util.List;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.squareup.picasso.Picasso;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoritesActivity extends OrmLiteBaseActivity<DatabaseHelper>  {
	RuntimeExceptionDao<Place, String> keysDao;
	GoogleMap gMap;
	List<Place> data;	
	DatabaseHelper dbHelper1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		keysDao = getHelper().getKeysRuntimeExceptionDao();
		data = keysDao.queryForAll();
		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.get(data.size()-1).getLatitude(), data.get(data.size()-1)
				.getLongitute()), 10));
		
		for (int i = 0; i < data.size()-1; i++) {
			//final int index=i;
			//placei = data.get(i);
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
				Picasso.with(FavoritesActivity.this)
				.load(data.get(index).getUrl_icon()).into(icon);
				place_name.setText(data.get(index).getName());
				place_type.setText(data.get(index).getType());
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
				String MID= marker.getId().substring(1);
				int id= Integer.parseInt(MID);
				keysDao.delete(data.get(id));
				marker.remove();
			}
		});
	}
}
