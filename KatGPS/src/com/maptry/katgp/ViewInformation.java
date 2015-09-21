package com.maptry.katgp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewInformation extends Activity implements OnClickListener{
	TextView txtName, txtDetail;
	Button btnDone,btnDel,btnGo;
	ImageView img;
	int i=0;
	String gotlatti="null ayo";
	String gotlongi="Long null ayo";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewinformation);
		txtName = (TextView) findViewById(R.id.tvName);
		txtDetail = (TextView) findViewById(R.id.tvDetail);
		btnDone = (Button) findViewById(R.id.btDone);
		btnDel = (Button) findViewById(R.id.btDel);
		btnGo = (Button) findViewById(R.id.btnGoTo);
		img = (ImageView) findViewById(R.id.ivImage);
		
		btnDone.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnGo.setOnClickListener(this);
		

		Bundle gotBasket = getIntent().getExtras();
		
		if(gotBasket!=null){
			
		gotlatti = gotBasket.getString("LatKey");
		gotlongi = gotBasket.getString("LonKey");
		}
		
		try{
			SqlDatabase enter = new SqlDatabase(ViewInformation.this);
		
		enter.open();
		
		String name = enter.getName(gotlatti);
		enter.close();
		trybase dataDB = new trybase(ViewInformation.this);
		dataDB.open();
		String detail = dataDB.getDetail(gotlatti);
		String imgLoc = dataDB.getImage(gotlatti);
		dataDB.close();
		txtName.setText(name.replace('_', ' '));
		txtDetail.setText(detail.replace('_', ' '));
		img.setImageBitmap(BitmapFactory.decodeFile(imgLoc));
		
		}catch(Exception e){
			txtName.setText("Data is not set for this location");
			txtDetail.setText("Data is not set for this location");
			i=1;
		}}catch(Exception e){
			Log.d("Error", "Error in View Information");
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btDel:
			try{
				if (i!=1){
				final CharSequence features[] = new CharSequence[] { "Yes","No" };

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						ViewInformation.this);
				alertDialog.setTitle("Are You Sure?").setItems(features,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (which == 0) {
									trybase delbase = new trybase(ViewInformation.this);
									delbase.open();
									delbase.deleteDetail(gotlatti);
									delbase.close();
									SqlDatabase delsqlDatabaseName = new SqlDatabase(ViewInformation.this);
									delsqlDatabaseName.open();
									delsqlDatabaseName.deleteName(gotlatti);
									delsqlDatabaseName.close();
									txtName.setText("Data is not set for this location");
									txtDetail.setText("Data is not set for this location");
									refresh();
									Intent i = new Intent(ViewInformation.this, MainActivity.class);
									startActivity(i);
								}
								if (which == 1) {
									refresh();
								}
							}
				});
				alertDialog.create().show();
				
		}
			}catch(Exception e){
				Log.d("Error", "Error in view information 3");
			}
			break;
		case R.id.btDone:
			try{
			refresh();
			Intent i = new Intent(ViewInformation.this, MainActivity.class);
			startActivity(i);
			}catch(Exception e){
				Log.d("Error", "Error in view Information 2");
			}
			break;
			
		case R.id.btnGoTo:
			try{
				
			if(gotlatti !=null){
				Bundle latbasket = new Bundle();
				Bundle longbasket = new Bundle();
				latbasket.putString("LattKey", gotlatti);
				longbasket.putString("Lonnkey", gotlongi);
				Intent a = new Intent(ViewInformation.this,
						MainActivity.class);
				a.putExtras(latbasket);
				a.putExtras(longbasket);
				startActivity(a);
			}
			}catch(Exception e)
			{
				Log.d("Error", "Error in view Information class 5");
				}
			break;
			}
		
		
	}
	@SuppressLint("NewApi")
	public void refresh(){
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
		}
	

}
