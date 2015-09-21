package com.maptry.katgp;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.ResourceProxy;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

@SuppressLint("ShowToast") public class MainActivity extends Activity implements LocationListener {
	// initializing the mapview

	MapView mapView;

	// private MapController mapController;

	ArrayList<OverlayItem> anotherOverlayItemArray;
	ItemizedIconOverlay<OverlayItem> iconOverlay;
	GeoPoint myLocation, ktmtry, myLocation2, ktmtry2;
	Button btn, smsBtn, recBtn, btnSrc;
	LocationManager locationManager;
	LocationListener locationListener;
	ToggleButton toggle, tracebtn;
	double latitude, longitude, recLat, recLong;
	TextView txtLat, wifiChk;
	Location location;
	IMapController mapController;
	AutoCompleteTextView etSearch;
	MapEventsReceiver mReceive;
	int toggleOn = 0, tracevalue = 0;
	RoadManager roadManager = new OSRMRoadManager();
	ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
		// contents from the main

		setContentView(R.layout.activity_main);

		// connecting with the xml layout id

		mapView = (MapView) findViewById(R.id.mapView);

		// to check the Wifi and 3G state
		ConnectivityManager manager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
		Boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		Boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();

		if (isWifi || is3g) {
			mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);

		} else {
			mapView.setTileSource(new XYTileSource("MapQuest",

			ResourceProxy.string.mapquest_osm, 13, 18, 256, ".jpg",
					new String[] { "http://otile1.mqcdn.com/tiles/1.0.0/map/",
							"http://otile2.mqcdn.com/tiles/1.0.0/map/",
							"http://otile3.mqcdn.com/tiles/1.0.0/map/",
							"http://otile4.mqcdn.com/tiles/1.0.0/map/" }));

			double north = 27.766491, east = 85.481863, south = 27.603540, west = 85.246686;
			BoundingBoxE6 bbox = new BoundingBoxE6(north, east, south, west);
			mapView.setScrollableAreaLimit(bbox);

		}

		// location service/ current location

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (isGPSEnabled) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		} else {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("GPS settings");
			alertDialog
					.setMessage("GPS is not enabled. Do you want to go to settings menu?");
			alertDialog.setPositiveButton("Settings",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});

			alertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alertDialog.show();
		}

		// for zooming and multitouch controls

		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		mapView.setUseDataConnection(false);
		mapController = mapView.getController();
		mapController.setZoom(12);
		GeoPoint kathmandu = new GeoPoint(27.7, 85.32);
		mapController.setCenter(kathmandu);
		myLocation = new GeoPoint(27.7, 85.32);
		ktmtry = new GeoPoint(27.7, 85.32);
		mapController.setCenter(ktmtry);

		// to set a marker at particular position
		Marker startMarker = new Marker(mapView);
		startMarker.setPosition(kathmandu);
		startMarker.setTitle("Start point");
		
		Drawable icon = getResources().getDrawable(R.drawable.marker);
		mapView.getOverlays().clear();
 
		startMarker.setImage(icon);
		startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		// mapView.getOverlays().add(startMarker);
		mapView.invalidate();

		/*
		 * This code only makes way for superman //to make the waypoints Marker
		 * startMarker1 = new Marker(mapView); RoadManager roadManager = new
		 * OSRMRoadManager(); ArrayList<GeoPoint> waypoints = new
		 * ArrayList<GeoPoint>(); waypoints.add(ktmtry);
		 * 
		 * GeoPoint endPoint = new GeoPoint(27.703991, 85.322635);
		 * startMarker1.setPosition(endPoint);
		 * startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		 * mapView.getOverlays().add(startMarker1); waypoints.add(endPoint);
		 * 
		 * 
		 * Road road = roadManager.getRoad(waypoints); Polyline roadOverlay =
		 * RoadManager.buildRoadOverlay(road, this); if (road.mStatus !=
		 * Road.STATUS_OK){ Toast.makeText(MainActivity.this,
		 * "Road Manager Not Working", Toast.LENGTH_LONG).show(); Log.d("Error",
		 * ""+road.mStatus); } else{ Toast.makeText(MainActivity.this,
		 * "Road Manager Working", Toast.LENGTH_LONG).show(); }
		 * mapView.getOverlays().add(roadOverlay); mapView.invalidate();
		 */

		// for searching
		SqlDatabase db = new SqlDatabase(getApplicationContext());

		db.open();

		List<String> lables = db.getAllLabels();
		db.close();
		// to convert the comma format to normal string format

		etSearch = (AutoCompleteTextView) findViewById(R.id.searchLocation);
		// Create the adapter and set it to the AutoCompleteTextView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lables);

		etSearch.setAdapter(adapter);
		btnSrc = (Button) findViewById(R.id.btnSearch);
		btnSrc.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					String name = etSearch.getText().toString().toLowerCase();
					name = name.replace(' ', '_');
					Log.d("On Main", "Starting addLocation");

					SqlDatabase db = new SqlDatabase(MainActivity.this);
					db.open();
					String latt = db.getLattitude(name);
					String longg = db.getLongitude(name);
					db.close();
					double latti = Double.parseDouble(latt);
					double longit = Double.parseDouble(longg);

					mapView.getOverlays().clear();
					meroMarkerOverlay();
					GeoPoint databasePoint = new GeoPoint(latti, longit);
					setMarker(databasePoint);

				} catch (Exception e) {

					Toast.makeText(MainActivity.this, "Not Found",
							Toast.LENGTH_SHORT).show();

				}

			}

		});
		// toggle button for location
		toggle = (ToggleButton) findViewById(R.id.here);
		toggle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (toggle.isChecked()) {
					mapController.animateTo(myLocation);
					toggleOn = 1;
					tracebtn.setChecked(false);
					tracevalue = 0;

				} else {
					toggleOn = 0;

				}
			}
		});

		// toggle button for trace
		try {
			tracebtn = (ToggleButton) findViewById(R.id.trace);
			tracebtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (tracebtn.isChecked()) {
						mapView.getOverlays().clear();
						meroMarkerOverlay();
						mapController.setZoom(18);
						mapController.animateTo(myLocation);

						tracevalue = 1;
						toggleOn = 0;
						toggle.setChecked(false);
						waypoints.add(myLocation2);
					} else {
						tracevalue = 0;

					}
				}
			});
		} catch (Exception e) {
			Toast.makeText(this, "Set GPS to Trace", Toast.LENGTH_SHORT).show();
		}

		// POSITION TRACKING continue

		// for events in maps
		mReceive = new MapEventsReceiver() {

			@Override
			public boolean singleTapConfirmedHelper(GeoPoint p) {
				// Toast.makeText( null,
				// "Tap on ("+p.getLatitude()+","+p.getLongitude()+")",
				// Toast.LENGTH_SHORT).show();
				// mapView.invalidate();
				Log.d("debug", "Single tap helper");
				return false;
			}

			@Override
			public boolean longPressHelper(GeoPoint p) {
				/*
				 * mapView.invalidate(); Marker tekup = new Marker(mapView);
				 * GeoPoint User = new
				 * GeoPoint(p.getLatitude(),p.getLongitude());
				 * tekup.setPosition(User);
				 * tekup.setTitle("You Set Marker Here");
				 * tekup.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
				 * mapView.getOverlays().add(tekup); mapView.invalidate();
				 */
				Log.d("debug", "LongPressHelper");

				setMarker(p);
				mapView.invalidate();
				return false;
			}

		};
		String gotlatti, gotlongi;
		Double latti, longi;
		Bundle gotBasket = getIntent().getExtras();
		
		if (gotBasket != null) {

			gotlatti = gotBasket.getString("LattKey");
			gotlongi = gotBasket.getString("Lonnkey");
			latti = Double.parseDouble(gotlatti);
			longi = Double.parseDouble(gotlongi);
			GeoPoint g = new GeoPoint(latti, longi);
			setMarker(g);

		}
		// markerEvent();
		meroMarkerOverlay();

		// to bound the offline map

		mapView.invalidate();
		}catch(Exception e){
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
		}

	}

	// to set marker method
	private void setMarker(GeoPoint p) {

		final GeoPoint User = new GeoPoint(p.getLatitude(), p.getLongitude());
		mapController.animateTo(User);
		// LOCATION PIN ICON

		anotherOverlayItemArray = new ArrayList<OverlayItem>();
		OverlayItem Oitem = new OverlayItem("KTM2", "KTM2", User);
		Drawable newMarker = this.getResources().getDrawable(R.drawable.marker);
		Oitem.setMarker(newMarker);
		anotherOverlayItemArray.add(Oitem);

		OnItemGestureListener<OverlayItem> myOnItemGestureListener = new OnItemGestureListener<OverlayItem>() {

			// when the marker is long pressed, it gives the location
			// information

			@Override
			public boolean onItemLongPress(int arg0, OverlayItem item) {
				// TODO Auto-generated method stub
				final CharSequence features[] = new CharSequence[] { "Save",
						"Send", "View" };

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						MainActivity.this);
				alertDialog.setTitle("Options").setItems(features,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (which == 0) {
									double lattit = User.getLatitude();
									double longit = User.getLongitude();

									String lat = String.valueOf(lattit);
									String lon = String.valueOf(longit);
									Bundle latbasket = new Bundle();
									Bundle longbasket = new Bundle();
									latbasket.putString("LatKey", lat);
									longbasket.putString("Lonkey", lon);
									Intent a = new Intent(MainActivity.this,
											SaveInformation.class);
									a.putExtras(latbasket);
									a.putExtras(longbasket);
									startActivity(a);
								}
								if (which == 1) {
									Log.i("Send SMS", "");

									Intent smsIntent = new Intent(
											Intent.ACTION_VIEW);
									smsIntent.setData(Uri.parse("smsto:"));
									smsIntent
											.setType("vnd.android-dir/mms-sms");
									smsIntent.putExtra("address",
											new String(""));

									smsIntent.putExtra("sms_body",
											"KatGPS " + User.getLatitude()
													+ " " + User.getLongitude());
									try {
										startActivity(smsIntent);
										Log.i("Finished sending SMS...", "");
									} catch (android.content.ActivityNotFoundException ex) {
										Toast.makeText(
												MainActivity.this,
												"SMS faild, please try again later.",
												Toast.LENGTH_SHORT).show();
									}
								}

								if (which == 2) {
									double lattit = User.getLatitude();
									double longit = User.getLongitude();

									String lat = String.valueOf(lattit);
									String lon = String.valueOf(longit);
									Bundle gbasket = new Bundle();
									Bundle ggbasket = new Bundle();
									gbasket.putString("LatKey", lat);
									ggbasket.putString("LonKey", lon);
									try {
										Intent b = new Intent(
												MainActivity.this,
												ViewInformation.class);
										b.putExtras(gbasket);
										b.putExtras(ggbasket);
										startActivity(b);
									} catch (Exception e) {
										Toast.makeText(MainActivity.this,
												"intent ma error aayo",
												Toast.LENGTH_LONG).show();
									}
								}

							}
						});
				alertDialog.create().show();

				return true;
			}

			// when the marker is clicked, it zooms to the given Geopoint at
			// zoom level 18

			@Override
			public boolean onItemSingleTapUp(int index, OverlayItem item) {
				Log.d("Marker tap",
						"This is a onItemSingleTapUp method inside setMarker method");
				return true;
			}
		};

		ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(
				this, anotherOverlayItemArray, myOnItemGestureListener);
		mapView.getOverlays().add(anotherItemizedIconOverlay);
		mapView.invalidate();

		Log.d("debug", "Set Marker Method");
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.receive:
			if (SmsReceiver.latt() == 0 && SmsReceiver.longg() == 0) {
				String receiveFail = "Coordinates NOT received";
				Toast.makeText(this, receiveFail, Toast.LENGTH_LONG).show();

			} else {
				GeoPoint naya = new GeoPoint(SmsReceiver.latt(),
						SmsReceiver.longg());
				mapController.animateTo(naya);
				setMarker(naya);
			}
			break;

		case R.id.aboutUs:
			try {
				Log.d("Intent Debug", "Opening intent");
				Intent i = new Intent("com.maptry.katgp.ABOUTUS");
				startActivity(i);
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, "error to open",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.ClearUp:
			mapView.getOverlays().clear();
			waypoints.clear();

			// to re-add the given event as overlay
			Toast.makeText(this, "Long Press On Map To Set Markers",
					Toast.LENGTH_LONG).show();
			// markerEvent();
			meroMarkerOverlay();
			mapView.invalidate();
			break;

		case R.id.sMessage:
			// for sending sms

			Log.i("Send SMS", "");

			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setData(Uri.parse("smsto:"));
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", new String(""));

			smsIntent.putExtra("sms_body", "KatGPS " + latitude + " "
					+ longitude);
			try {
				startActivity(smsIntent);
				Log.i("Finished sending SMS...", "");
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(MainActivity.this,
						"SMS faild, please try again later.",
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.sDatabase:
			try {
				Log.d("Intent Debug", "Opening intent");
				Intent i = new Intent("com.maptry.katgp.MAPSQLVIEW");
				startActivity(i);
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, "error to view database",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.myPlaces:
			try {
				Log.d("Intent Debug", "Opening intent");
				Intent i = new Intent("com.maptry.katgp.MYPLACES");
				startActivity(i);
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, "error to view places",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		return false;
	}

	// to add the marker overlay
	public void meroMarkerOverlay() {
		MapEventsOverlay OverlayEventos = new MapEventsOverlay(
				getBaseContext(), mReceive);
		mapView.getOverlays().add(OverlayEventos);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this); // GPS use gareko
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		myLocation = new GeoPoint(latitude, longitude);
		myLocation2 = new GeoPoint(latitude, longitude);

		if (toggleOn == 1) {
			mapView.getOverlays().clear();

			hawa(myLocation);
		} else if (tracevalue == 1) {
			myLocation2 = myLocation;
			waypoints.add(myLocation);
			Road road = roadManager.getRoad(waypoints);
			Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
			mapView.getOverlays().add(roadOverlay);
			mapView.invalidate();

			// trace(myLocation);
		} else if (toggleOn == 0) {
			meroMarkerOverlay();
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("Latitude", "status");

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitude", "enable");

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitude", "disable");
	}

	public void hawa(final GeoPoint User) {
		anotherOverlayItemArray = new ArrayList<OverlayItem>();
		OverlayItem Oitem = new OverlayItem("KTM2", "KTM2", User);
		Drawable newMarker = this.getResources().getDrawable(R.drawable.marker);
		Oitem.setMarker(newMarker);
		anotherOverlayItemArray.add(Oitem);

		OnItemGestureListener<OverlayItem> myOnItemGestureListener = new OnItemGestureListener<OverlayItem>() {

			// when the marker is long pressed, it gives the location
			// information

			@Override
			public boolean onItemLongPress(int arg0, OverlayItem item) {
				// TODO Auto-generated method stub
				final CharSequence features[] = new CharSequence[] { "Save",
						"Send", "View" };

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						MainActivity.this);
				alertDialog.setTitle("Options").setItems(features,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (which == 0) {
									double lattit = User.getLatitude();
									double longit = User.getLongitude();

									String lat = String.valueOf(lattit);
									String lon = String.valueOf(longit);
									Bundle latbasket = new Bundle();
									Bundle longbasket = new Bundle();
									latbasket.putString("LatKey", lat);
									longbasket.putString("Lonkey", lon);
									Intent a = new Intent(MainActivity.this,
											SaveInformation.class);
									a.putExtras(latbasket);
									a.putExtras(longbasket);
									startActivity(a);
								}
								if (which == 1) {
									Log.i("Send SMS", "");

									Intent smsIntent = new Intent(
											Intent.ACTION_VIEW);
									smsIntent.setData(Uri.parse("smsto:"));
									smsIntent
											.setType("vnd.android-dir/mms-sms");
									smsIntent.putExtra("address",
											new String(""));

									smsIntent.putExtra(
											"sms_body",
											"KatGPS " + User.getLatitude()
													+ " "
													+ User.getLongitude());
									try {
										startActivity(smsIntent);
										finish();
										Log.i("Finished sending SMS...", "");
									} catch (android.content.ActivityNotFoundException ex) {
										Toast.makeText(
												MainActivity.this,
												"SMS faild, please try again later.",
												Toast.LENGTH_SHORT).show();
									}
								}

								if (which == 2) {
									Intent a = new Intent(MainActivity.this,
											ViewInformation.class);
									// a.putExtras(latbasket);
									// a.putExtras(longbasket);
									startActivity(a);
								}

							}
						});
				alertDialog.create().show();

				return true;
			}

			// when the marker is clicked, it zooms to the given Geopoint at
			// zoom level 18

			@Override
			public boolean onItemSingleTapUp(int index, OverlayItem item) {
				Log.d("Marker tap",
						"This is a onItemSingleTapUp method inside setMarker method");
				return true;
			}
		};

		ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(
				this, anotherOverlayItemArray, myOnItemGestureListener);
		mapView.getOverlays().add(anotherItemizedIconOverlay);

		// meroMarkerOverlay();

		mapView.invalidate();

		// meroMarkerOverlay();
	}

}
