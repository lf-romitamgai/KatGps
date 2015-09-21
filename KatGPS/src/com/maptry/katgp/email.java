package com.maptry.katgp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class email extends Activity{
	Button email;
	EditText mail;
	String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		mail = (EditText) findViewById(R.id.etMail);
		email = (Button) findViewById(R.id.btEmail);
		name = mail.getText().toString();
		email.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String emailmessage[] = {name};
				String emailaddress[] = {"rom_amgai@hotmail.com"};
				
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "User Feedback");
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailmessage);
				startActivity(emailIntent);
				
			}
		});
		
		}
	@Override
	protected void onPause(){
		super.onPause();
		finish();
	}
	
}
