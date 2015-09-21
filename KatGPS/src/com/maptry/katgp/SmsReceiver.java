package com.maptry.katgp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
 
public class SmsReceiver extends BroadcastReceiver
{
	 static double lattitude=0;
	static double longitude=0;
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";  
        String latt = "";
        String hahah = "Coordinates Received";
       
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
           
                	
    //        for (int i=0; i<=9; i++){
      //          msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
        //        str +=msgs[i].getMessageBody().toString();
          //      str +="\n"; 
        //}    
          //  if(new String("9841122609").equals(str)){
            for (int i=0; i<msgs.length; i++){
	                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	                latt +=msgs[i].getMessageBody().toString();
	                latt +="\n"; 
	        }
         
            //---display the new SMS message---
            
            String[] strSplit = latt.split(" ");
           // Toast.makeText(context, strSplit[0], Toast.LENGTH_LONG).show();
            str= strSplit[0];
            if(new String("KatGPS").equals(str)){
            	Toast.makeText(context, hahah, Toast.LENGTH_LONG).show();
            	
            lattitude = Double.parseDouble(strSplit[1]);
            longitude = Double.parseDouble(strSplit[2]);
            }
            else
            {
            
            	lattitude = 27.7;
            	longitude = 85.32;
            }
            //IMapController mapController = null;;
           // GeoPoint aalu = new GeoPoint(lattitude,longitude);
            //mapController.animateTo(aalu);
        }                         
    }
public static double latt(){
	return lattitude;
}
public static double longg(){
	return longitude;
}
}