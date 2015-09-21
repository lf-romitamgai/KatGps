package com.maptry.katgp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewInformationClass extends Activity{
	TextView txtName, txtDetail;
	ImageView photo;
	Button btnDone;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewinformation);
		txtName = (TextView) findViewById(R.id.tvName);
		txtDetail = (TextView) findViewById(R.id.tvDetail);
		photo = (ImageView) findViewById(R.id.imageView1);
		btnDone = (Button) findViewById(R.id.btDone);
		
		String gotlatti="null ayo";
		String gotlongi="Long null ayo";
		Bundle gotBasket = getIntent().getExtras();
		
		if(gotBasket!=null){
			
		gotlatti = gotBasket.getString("LatKey");
		gotlongi = gotBasket.getString("Lonkey");
		}
		
		
	}
	

}
