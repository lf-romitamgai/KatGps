package com.maptry.katgp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AboutUs extends Activity{

	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		
		btn = (Button) findViewById(R.id.btEmail);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                 String emailaddress[] = {"rom_amgai@hotmail.com"};
				
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "User Feedback");
				emailIntent.setType("plain/text");
				startActivity(emailIntent);
			}
		});
		
		
		
		
	}
	
	

}
