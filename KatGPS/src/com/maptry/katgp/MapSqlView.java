package com.maptry.katgp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MapSqlView extends Activity implements OnClickListener {
	EditText sqlID, sqlName, sqlLatt, sqlLong;
	Button sqlDelete, sqlUpdate, sqlAdd;
	TextView tv;
	String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try{super.onCreate(savedInstanceState);
		setContentView(R.layout.mapsqlview);
		tv = (TextView) findViewById(R.id.tvSQLinfo);
		}catch(Exception e){
			Toast.makeText(this, "on create error", Toast.LENGTH_LONG).show();
		}
		SqlDatabase info = new SqlDatabase(this);
		info.open();
		Log.d("In Another Intent", "Database Opened");
		 data = info.getData();
		info.close();
		
		tv.setText(data);
		
		sqlDelete = (Button) findViewById(R.id.bSQLdelete);
		sqlUpdate = (Button) findViewById(R.id.bUpdate);
		sqlAdd = (Button) findViewById(R.id.bAdd);

		sqlID = (EditText) findViewById(R.id.etSQLdl);
		sqlName = (EditText) findViewById(R.id.etName);
		sqlLatt = (EditText) findViewById(R.id.etLatt);
		sqlLong = (EditText) findViewById(R.id.etLong);

		sqlDelete.setOnClickListener(this);
		sqlUpdate.setOnClickListener(this);
		sqlAdd.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.bSQLdelete:
			try {
				String sRow1 = sqlID.getText().toString();
				long sRR = Long.parseLong(sRow1);
				SqlDatabase ex1 = new SqlDatabase(this);
				ex1.open();
				ex1.deleteEntry(sRR);
				ex1.close();
				Dialog d = new Dialog(this);
				d.setTitle("Done");
				d.show();
			} catch (Exception e) {
				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("No info for this id");
				TextView tv = new TextView(this);
				tv.setText("Enter a number");
				d.setContentView(tv);
				d.show();
			}
			refresh();
			break;

		case R.id.bUpdate:

			try {
				String name = sqlName.getText().toString();
				String latt = sqlLatt.getText().toString();
				String longg = sqlLong.getText().toString();
				String sRow = sqlID.getText().toString();
				long roww = Long.parseLong(sRow);

				SqlDatabase entry = new SqlDatabase(MapSqlView.this);
				entry.open();
				entry.updateEntry(roww, name.replace(' ', '_'), latt, longg);
				entry.close();
			} catch (Exception e) {

				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("No info for this id");
				d.show();
			}
			refresh();
			break;

		case R.id.bAdd:
			boolean didItWork = true;
			try {
				String name = sqlName.getText().toString();
				
				String latt = sqlLatt.getText().toString();
				String longg = sqlLong.getText().toString();
				SqlDatabase enter = new SqlDatabase(MapSqlView.this);
				enter.open();
				Log.d("On Main", "Starting addLocation");
				enter.addLocation(name.replace(' ', '_'), latt, longg);
				enter.close();
			} catch (Exception e) {
				didItWork = false;
				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("Failed, Missing Something");
				d.show();

			} finally {
				if (didItWork) {
					Dialog d = new Dialog(this);
					d.setTitle("Success");
					d.show();
				}
				refresh();
			}
			break;

		}

	}

	public void refresh() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	



	
}
