package com.androideverde.android.airporttrain.london.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.androideverde.android.airporttrain.london.R;

public class StationListActivity extends SherlockListActivity {

	private String airport;
	private String[] data;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_list);
		
		airport = getIntent().getStringExtra("AIRPORT");
		this.setTitle(getString(R.string.stationlist_title, airport));
		
		data = getListData(airport);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
		setListAdapter(adapter);
	}
	
	private String[] getListData(String airport) {
		if (airport.equals("Heathrow")) {
			data = getResources().getStringArray(R.array.stations_heathrow);
		} else if (airport.equals("Gatwick")) {
			data = getResources().getStringArray(R.array.stations_gatwick);
		} else if (airport.equals("Luton")) {
			data = getResources().getStringArray(R.array.stations_luton);			
		}
		return data;
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		if (airport.equals("Heathrow") && position == 0) {
			// Piccadilly tube line selected
			Intent i = new Intent(this, RouteDetailActivity.class);
			i.putExtra("COMPANY", "tube");
			startActivity(i);
		} else {
			Intent i = new Intent(this, TrainListActivity.class);
			i.putExtra("AIRPORT", airport);
			i.putExtra("STATION", data[position]);
			startActivity(i);
		}
	}

}
