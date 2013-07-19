package com.androideverde.android.airporttrain.london.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.androideverde.android.airporttrain.london.R;

public class RouteDetailActivity extends SherlockActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routedetail);
        
		String company = getIntent().getStringExtra("COMPANY");
		TextView title = (TextView) findViewById(R.id.txt_company);
		ImageView logo = (ImageView) findViewById(R.id.img_company);
		TextView description = (TextView) findViewById(R.id.txt_route_description);
		description.setMovementMethod(LinkMovementMethod.getInstance()); // needed to make links in description clickable
		// http://ojp.nationalrail.co.uk/service/timesandfares/<ORIGIN_STATION>/<DESTINATION_STATION>/<DATE>/<TIME>/dep
		// ORIGIN_STATION and DESTINATION_STATION are 3-letter codes for the station
		// DATE: today|tomorrow|DDMMYY
		// TIME: HHMM
		String origin = getIntent().getStringExtra("ORIGIN");
		String dest = getIntent().getStringExtra("DESTINATION");
		String deptime = getIntent().getStringExtra("DEPART_TIME");
		if (deptime != null) {
			deptime = deptime.replace(":", "");
		}
		String desc_URL = "http://ojp.nationalrail.co.uk/service/timesandfares/" + origin + "/" + dest + "/today/" + deptime + "/dep";
		String desc_text = getString(R.string.detail_description) + " <a href=\"" + desc_URL + "\">" + desc_URL + "</a>.";
		description.setText(Html.fromHtml(desc_text));
		
		if (company.equals("DLR")) {
			title.setText(getString(R.string.DLR_company));
			logo.setImageResource(R.drawable.dlr_logo);
			description.setText(Html.fromHtml(getString(R.string.DLR_desc)));
		} else if (company.equals("tube")) {
			title.setText(getString(R.string.tube_company));			
			logo.setImageResource(R.drawable.tube_logo);
			description.setText(Html.fromHtml(getString(R.string.tube_desc)));
		} else if (company.equals("FCC")) {
			title.setText(getString(R.string.FCC));			
			logo.setImageResource(R.drawable.fcc_logo);
		} else if (company.equals("SOUTHERN")) {
			title.setText(getString(R.string.SOUTHERN));			
			logo.setImageResource(R.drawable.southern_logo);
		} else if (company.equals("EM")) {
			title.setText(getString(R.string.EM));			
			logo.setImageResource(R.drawable.eastmidlands_logo);
		} else if (company.equals("GTW_EXPRESS")) {
			title.setText(getString(R.string.GTW_EXPRESS));			
			logo.setImageResource(R.drawable.gtwexpress_logo);
		} else if (company.equals("GA")) {
			title.setText(getString(R.string.GA));			
			logo.setImageResource(R.drawable.greateranglia_logo);
		} else if (company.equals("SSD_EXPRESS")) {
			title.setText(getString(R.string.SSD_EXPRESS));			
			logo.setImageResource(R.drawable.ssdexpress_logo);
		} else if (company.equals("HXX_EXPRESS")) {
			title.setText(getString(R.string.HXX_EXPRESS));			
			logo.setImageResource(R.drawable.hxxexpress_logo);
		} else if (company.equals("H_CONNECT")) {
			title.setText(getString(R.string.H_CONNECT));			
			logo.setImageResource(R.drawable.heathrowconnect_logo);
		}
    }
}
