package com.androideverde.android.airporttrain.london.ui;

import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.androideverde.android.airporttrain.london.R;
import com.androideverde.android.airporttrain.london.util.AssetSQLiteHelper;

public class MainActivity extends SherlockActivity implements OnClickListener {
	
	private int version = 4; // increment here when db has been updated instead of usual DB_VERSION
	private AssetSQLiteHelper helper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_new);
        setContentView(R.layout.main_dashboard);
        
        // find views and assign listeners for all buttons
        ImageView logo = (ImageView) findViewById(R.id.imgLogo);
        logo.setOnClickListener(this);
        Button btnHeathrow = (Button) findViewById(R.id.btnHeathrow);
        btnHeathrow.setOnClickListener(this);
        Button btnGatwick = (Button) findViewById(R.id.btnGatwick);
        btnGatwick.setOnClickListener(this);
        Button btnStansted = (Button) findViewById(R.id.btnStansted);
        btnStansted.setOnClickListener(this);
        Button btnLuton = (Button) findViewById(R.id.btnLuton);
        btnLuton.setOnClickListener(this);
        Button btnCity = (Button) findViewById(R.id.btnCity);
        btnCity.setOnClickListener(this);
        Button btnSouthend = (Button) findViewById(R.id.btnSouthend);
        btnSouthend.setOnClickListener(this);

        // shared preferences to decide if we need to update the db
        SharedPreferences prefs = getSharedPreferences("PREFS", 0);
        int dataversion = prefs.getInt("DataVersion", 0);
        Log.i("MainActivity","Prefs version: " + dataversion + ". Current: " + version);
        if (dataversion < version) {
        	// data needs updating
			try {
				helper = new AssetSQLiteHelper(this);
				helper.removedatabase();
				helper.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// store the new value in shared prefs
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("DataVersion", version);
			editor.commit();
        }
        
    }

	@Override
	public void onClick(View v) {
		// depending on the airport selected
		int airport = v.getId();
		if (airport == R.id.btnCity) {
			// no station list, only one option available
			// show detailed info
			Intent i = new Intent(this, RouteDetailActivity.class);
				i.putExtra("COMPANY", "DLR");
				startActivity(i);
		} else if (airport == R.id.btnSouthend) {
			// no station list, only one option available
			// show timetable for Liverpool Street Station
			Intent i = new Intent(this, TrainListActivity.class);
			i.putExtra("AIRPORT", "Southend");
			i.putExtra("STATION", getString(R.string.LST));
			startActivity(i);
		} else if (airport == R.id.btnStansted) {
			// no station list, only one option available
			// show timetable for Liverpool Street Station
			Intent i = new Intent(this, TrainListActivity.class);
			i.putExtra("AIRPORT", "Stansted");
			i.putExtra("STATION", getString(R.string.LST));
			startActivity(i);
		} else if (airport == R.id.imgLogo) {
			// show the about window
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		} else {
			// show the station list
			Intent i = new Intent(this, StationListActivity.class);
			if (airport == R.id.btnHeathrow) {
				i.putExtra("AIRPORT", "Heathrow");				
			} else if (airport == R.id.btnGatwick) {
				i.putExtra("AIRPORT", "Gatwick");
			} else if (airport == R.id.btnLuton) {
				i.putExtra("AIRPORT", "Luton");
			}
			startActivity(i);
		}
	}
}