package com.androideverde.android.airporttrain.london.ui;

import java.io.IOException;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.androideverde.android.airporttrain.london.R;
import com.androideverde.android.airporttrain.london.util.AssetSQLiteHelper;

public class TrainListActivity extends SherlockListActivity {
	
	private String airport;
	private String station;
	private int airportId;
	private String stationId;
	private String airportStationId;
	private int today;
	private String day;
	private Cursor cursor;
	SimpleCursorAdapter adapter;
	ListView lv;
	private AssetSQLiteHelper helper;
	
	/**
	 * ActionBarSherlock-related code
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subDate = menu.addSubMenu("Date");
        subDate.add(R.string.abar_menu_week);
        subDate.add(R.string.abar_menu_sat);
        subDate.add(R.string.abar_menu_sun);
        MenuItem subDateItem = subDate.getItem();
		subDateItem.setIcon(R.drawable.ic_menu_today);
		subDateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add("Flip").setIcon(R.drawable.ic_menu_revert).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		CharSequence id = item.getTitle();
		if (id.equals(getString(R.string.abar_menu_week))) {
			// set the date to weekday
			day = "week";
			this.setTitle(R.string.abar_title_week);
			// get the data from the database and attach it to the adapter and listview
			cursor = helper.getCursorTrainTimes(airportId, stationId, day);
			startManagingCursor(cursor);
			adapter = new SimpleCursorAdapter(this, R.layout.listrow, cursor, new String[] { "departTime", "arriveTime", "company" }, new int[] { R.id.from_time, R.id.to_time, R.id.img_logo });
			adapter.setViewBinder(new LogoViewBinder());
			setListAdapter(adapter);
		} else if (id.equals(getString(R.string.abar_menu_sat))) {
			// set the date to saturday
			day = "sat";
			this.setTitle(R.string.abar_title_sat);
			// get the data from the database and attach it to the adapter and listview
			cursor = helper.getCursorTrainTimes(airportId, stationId, day);
			startManagingCursor(cursor);
			adapter = new SimpleCursorAdapter(this, R.layout.listrow, cursor, new String[] { "departTime", "arriveTime", "company" }, new int[] { R.id.from_time, R.id.to_time, R.id.img_logo });
			adapter.setViewBinder(new LogoViewBinder());
			setListAdapter(adapter);
		} else if (id.equals(getString(R.string.abar_menu_sun))) {
			// set the date to sunday
			day = "sun";
			this.setTitle(R.string.abar_title_sun);
			// get the data from the database and attach it to the adapter and listview
			cursor = helper.getCursorTrainTimes(airportId, stationId, day);
			startManagingCursor(cursor);
			adapter = new SimpleCursorAdapter(this, R.layout.listrow, cursor, new String[] { "departTime", "arriveTime", "company" }, new int[] { R.id.from_time, R.id.to_time, R.id.img_logo });
			adapter.setViewBinder(new LogoViewBinder());
			setListAdapter(adapter);
		} else if (id.equals("Flip")) {
			// change direction
			changeDirection();
		}
		return false;
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.train_list);
		lv = (ListView) findViewById(android.R.id.list);

		// receive intent extras sent from StationListActivity
		airport = getIntent().getStringExtra("AIRPORT");
		station = getIntent().getStringExtra("STATION");

		// check what type of day is today
		today = new GregorianCalendar().get(GregorianCalendar.DAY_OF_WEEK);
		if (today == 1) {
			// It's Sunday
			day = "sun";
			this.setTitle(R.string.abar_title_sun);
		} else if (today == 7) {
			// It's Saturday
			day = "sat";
			this.setTitle(R.string.abar_title_sat);
		} else {
			// It's a weekday
			day = "week";
			this.setTitle(R.string.abar_title_week);
		}
		
		// get the index for the airport
		String[] airportArray = getResources().getStringArray(R.array.airports);
		int i = 0;
		for (String s : airportArray) {
			if (s.contains(airport)) {
				airportId = i; 
			}
			i++;
		}
		Log.i("TrainListActivity", "Airport: " + airportId);

		// get the train station code for the airport station
		String[] airportStationArray = getResources().getStringArray(R.array.airport_stations);
		String as = airportStationArray[airportId];
		airportStationId = as.substring(as.length() - 4, as.length() - 1);

		// get the train station code for the train station
		String[] stationArray = getResources().getStringArray(R.array.stations_all);
		for (String s : stationArray) {
			if (s.contains(station)) {
				stationId = s.substring(s.length() - 4, s.length() - 1); 
			}
		}
		Log.i("TrainListActivity", "Station: " + stationId);
		
		// connect to the database
		try {
			helper = new AssetSQLiteHelper(this);
			helper.createdatabase();
			helper.opendatabase();
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("TrainListActivity", "Unable to create database.");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("TrainListActivity", "Unable to open database.");
		}
		Log.i("TrainListActivity", helper.getCounts() + " items!");

		// get the data from the database and attach it to the adapter and listview
		cursor = loadData();
		startManagingCursor(cursor);
		adapter = new SimpleCursorAdapter(this, R.layout.listrow, cursor, new String[] { "departTime", "arriveTime", "company" }, new int[] { R.id.from_time, R.id.to_time, R.id.img_logo });
		adapter.setViewBinder(new LogoViewBinder());
		setListAdapter(adapter);
	}

	private Cursor loadData() {
		// arrange graphical direction indication
		ImageView imgFrom = (ImageView) findViewById(R.id.img_from);
		ImageView imgTo = (ImageView) findViewById(R.id.img_to);
		TextView txtFrom = (TextView) findViewById(R.id.txt_from);
		TextView txtTo = (TextView) findViewById(R.id.txt_to);
		if (airportId < 10) {
			// from Airport to Station
			imgFrom.setImageResource(R.drawable.airport);
			imgTo.setImageResource(R.drawable.train);
			txtFrom.setText(airport);
			txtTo.setText(station);
		} else {
			// from Station to Airport
			imgFrom.setImageResource(R.drawable.train);
			imgTo.setImageResource(R.drawable.airport);
			txtFrom.setText(station);
			txtTo.setText(airport);
		}
		// fetch the data from the database
		return helper.getCursorTrainTimes(airportId, stationId, day);			
	}

	private void changeDirection() {
		// handle change in direction
		// airportId below 10 (0 = Heathrow
		//                     1 = Gatwick
		//                     2 = Luton
		//                     3 = Stansted
		//                     4 = City
		//                     5 = Southend)
		// means direction is from airport to station. + 10 means it's from station to airport
		if (airportId < 10) {
			airportId += 10;
		} else {
			airportId -= 10;
		}
		Log.i("TrainListActivity", "Airport: " + airportId);
		cursor = loadData();
		Log.i("TrainListActivity", "Cursor: " + cursor.getCount());
		startManagingCursor(cursor);
		adapter = new SimpleCursorAdapter(this, R.layout.listrow, cursor, new String[] { "departTime", "arriveTime", "company" }, new int[] { R.id.from_time, R.id.to_time, R.id.img_logo });
		adapter.setViewBinder(new LogoViewBinder());
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Intent i = new Intent(this, RouteDetailActivity.class);
		cursor.moveToPosition(position);
		i.putExtra("COMPANY", cursor.getString(8)); // get the company for the selected row
		if (airportId < 10) {
			i.putExtra("ORIGIN", airportStationId);
			i.putExtra("DESTINATION", stationId);
		} else {
			i.putExtra("ORIGIN", stationId);
			i.putExtra("DESTINATION", airportStationId);			
		}
		i.putExtra("DEPART_TIME", cursor.getString(6));
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		helper.close();
		super.onDestroy();
	}
	
	private class LogoViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View v, Cursor c, int columnIndex) {
			if (v.getId() == R.id.img_logo) {
				ImageView iv = (ImageView) v;
				String company = c.getString(columnIndex);
				if (company.equals("FCC")) {
					iv.setImageResource(R.drawable.fcc_logo);
				} else if (company.equals("SOUTHERN")) {
					iv.setImageResource(R.drawable.southern_logo);
				} else if (company.equals("GTW_EXPRESS")) {
					iv.setImageResource(R.drawable.gtwexpress_logo);
				} else if (company.equals("EM")) {
					iv.setImageResource(R.drawable.eastmidlands_logo);
				} else if (company.equals("SSD_EXPRESS")) {
					iv.setImageResource(R.drawable.ssdexpress_logo);
				} else if (company.equals("GA")) {
					iv.setImageResource(R.drawable.greateranglia_logo);
				} else if (company.equals("HXX_EXPRESS")) {
					iv.setImageResource(R.drawable.hxxexpress_logo);
				} else if (company.equals("H_CONNECT")) {
					iv.setImageResource(R.drawable.heathrowconnect_logo);
				}
			} else {
				TextView tv = (TextView) v;
				tv.setText(c.getString(columnIndex));
			}
			return true;
		}
		
	}
}
