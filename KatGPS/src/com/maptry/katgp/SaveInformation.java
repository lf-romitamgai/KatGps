package com.maptry.katgp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SaveInformation extends Activity implements OnClickListener{
	EditText name, detail;
	Button save,cancel,btnCam;
	final int MEDIA_TYPE_IMAGE = 2;
	private static final int CameraRequest=100;
	ImageView photoImage = null;
	 String meFile;
	 String timeStamp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savedinformation);
		
		save = (Button) findViewById(R.id.btSave);
		cancel= (Button) findViewById(R.id.btCancel);
		btnCam = (Button) findViewById(R.id.button1);
		
		name = (EditText) findViewById(R.id.etName);
		detail = (EditText) findViewById(R.id.etDetail);
	
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		btnCam.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btSave: 
			String gotlatti="null ayo";
			String gotlongi="Long null ayo";
			Bundle gotBasket = getIntent().getExtras();
			
			if(gotBasket!=null){
				
			gotlatti = gotBasket.getString("LatKey");
			gotlongi = gotBasket.getString("Lonkey");
			}
			
			
			
			try {
				String name1 = name.getText().toString();
				String detail1 = detail.getText().toString();
			SqlDatabase enter = new SqlDatabase(SaveInformation.this);
			enter.open();
			Log.d("On Main", "Starting addLocation");
			enter.addAll(name1.replace(' ', '_'),gotlatti,gotlongi);
			enter.close();
		
			 File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "KatGPS");
			 meFile = (mediaStorageDir.getPath() + File.separator +
			            "IMG_"+ timeStamp + ".jpg");
			String imageLocation= meFile;
			trybase detDB = new trybase(SaveInformation.this);
			detDB.open();
			detDB.addDetail(gotlatti, gotlongi, detail1.replace(' ', '_'),imageLocation);
			detDB.close();
			}catch(Exception e){
				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("Failed, Missing Something");
				d.show();
				
			}finally{
				refresh();
			Toast.makeText(SaveInformation.this, "Saved", Toast.LENGTH_LONG).show();
			Intent i = new Intent(SaveInformation.this, MainActivity.class);
			startActivity(i);
			
			}
			
			break;
		case R.id.btCancel:
			Intent i = new Intent(SaveInformation.this,MainActivity.class);
			startActivity(i);
			Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.button1: {
			Log.d("Camera Button Pressed", "enter Successful");
			
			int TakePicture=0;
			Camera camera;
			Uri fileUri = null;
			
		
			Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			 fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(cameraIntent,CameraRequest);
		}
		}
		
	} 
	private Uri getOutputMediaFileUri(int type) {
		// TODO Auto-generated method stub
		Log.d("getOutputMediaFileUri", "getOutputMedia function done");
		return Uri.fromFile(getOutputMediaFile(type));
	}

			public File getOutputMediaFile(int type) {
				// TODO Auto-generated method stub
				//creating a file
				Log.d("File getOutputMediaFile", "enterd function getOutputMediaFile");
				 File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
			              Environment.DIRECTORY_PICTURES), "KatGPS");
				 if (! mediaStorageDir.exists()){
				        if (! mediaStorageDir.mkdirs()){
				            Log.d("MyCameraApp", "failed to create directory");
				            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
				            return null;
				        }
				 }
				     // Create a media file name
				         timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				        File mediaFile;
				        if (type == MEDIA_TYPE_IMAGE){
				        	meFile = (mediaStorageDir.getPath() + File.separator +
						            "IMG_"+ timeStamp + ".jpg");
				            mediaFile = new File(meFile);
				            
				        
				        } else {
				            return null;
				        }
				 
				        return mediaFile;
				 				 }
			/*@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) 
			{
			    super.onActivityResult(requestCode, resultCode, data);
			    try{if( requestCode == CameraRequest)
			    {   
			        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			        ImageView image =(ImageView) findViewById(R.id.imageView1);
			        image.setImageBitmap(thumbnail);
			    }
			    
			    else 
			    {
			        Toast.makeText(this, "Picture NOt taken", Toast.LENGTH_LONG).show();
			    }}catch(Exception e){
			    	 Toast.makeText(this, "Image Error", Toast.LENGTH_LONG).show();
			    }

			}*/
			
	public void refresh(){
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
		}
	

}
