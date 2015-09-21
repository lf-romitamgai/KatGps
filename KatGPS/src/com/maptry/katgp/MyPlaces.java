package com.maptry.katgp;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyPlaces extends android.app.ListActivity implements OnItemLongClickListener {
	/** Called when the activity is first created. */

	private SqlDatabase mDbAdapter;
	private static String[] rows;
	String data;
	private ArrayList<String> nameList = null;
	private String[] commandArray = new String[] { "Delete" };
	private int clickItem;
	private static final String newLine = System.getProperty("line.separator");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myplaces);
		nameList = new ArrayList<String>();
		mDbAdapter = new SqlDatabase(this);
		mDbAdapter.open();
		try {
			SqlDatabase info = new SqlDatabase(this);
			info.open();
			Log.d("In my Place Intent", "Database Opened");
			data = info.getMyPlace();
			info.close();
			mDbAdapter.close();
			rows = data.split("[" + newLine + "]");
			for (int i = 0; i < rows.length; i++) {
				nameList.add(rows[i]);
			}
			ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
					R.layout.row, nameList);
			setListAdapter(ad);
		} catch (Exception e) {
			Toast.makeText(this, "Error in My Places", Toast.LENGTH_SHORT)
					.show();
		}}catch(Exception e){
			Log.d("Error", "Error in my places");
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		try{
		super.onListItemClick(l, v, position, id);
		String getLatti, getLongi;
		Double latti,longi;
		clickItem = position;
		// clickItem=Integer.parseInt(idList.get(position));
		String clickItems = nameList.get(clickItem);
		try {
			SqlDatabase info = new SqlDatabase(MyPlaces.this);
			info.open();
			getLatti = info.getLattitude(clickItems);
			getLongi = info.getLongitude(clickItems);
			info.close();
			latti = Double.parseDouble(getLatti);
			longi = Double.parseDouble(getLongi);
			if(getLatti == null){
				trybase delbase = new trybase(MyPlaces.this);
				delbase.open();
				delbase.deleteDetail(getLatti);
				delbase.close();
				SqlDatabase delsqlDatabaseName = new SqlDatabase(MyPlaces.this);
				delsqlDatabaseName.open();
				delsqlDatabaseName.deleteName(getLatti);
				delsqlDatabaseName.close();
				refresh();
			}
			else{
			Bundle latbasket = new Bundle();
			Bundle longbasket = new Bundle();
			latbasket.putString("LatKey", getLatti);
			longbasket.putString("Lonkey", getLongi);
			Intent a = new Intent(MyPlaces.this,
					ViewInformation.class);
			a.putExtras(latbasket);
			a.putExtras(longbasket);
			startActivity(a);
			}
			
		} catch (Exception e) {
			Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT)
					.show();
		}
		}catch(Exception e){
			Log.d("Error", "Error in my places 2");
		}

	}

	

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		// TODO Auto-generated method stub
		showDialog();
		return true;
		
	}
	private void showDialog() {
		try{
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Single Choice");
		builder.setItems(commandArray, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try{
				String getLatti;
				String clickItems = nameList.get(clickItem);
				SqlDatabase info = new SqlDatabase(MyPlaces.this);
				info.open();
				getLatti = info.getLattitude(clickItems);
				
				info.close();
				trybase delbase = new trybase(MyPlaces.this);
				delbase.open();
				delbase.deleteDetail(getLatti);
				delbase.close();
				SqlDatabase delsqlDatabaseName = new SqlDatabase(MyPlaces.this);
				delsqlDatabaseName.open();
				delsqlDatabaseName.deleteName(getLatti);
				delsqlDatabaseName.close();
				refresh();
				}catch(Exception e){
					Log.d("Error", "Error in my places 7");
				}
			}
		});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		}catch(Exception e){
			Log.d("Error", "Error in my places 4");
		}
	}
	@SuppressLint("NewApi")
	public void refresh(){
		try{
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
		}catch(Exception e){
			Log.d("Error", "Error in my places 5");
		}
		}
	

}
