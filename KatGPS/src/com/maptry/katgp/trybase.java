package com.maptry.katgp;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class trybase {
	 // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME1 = "mapdefiner.db";
 
    // Contacts table name
    private static final String DATABASE_TABLE = "saveit";
   
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LATTITUDE = "lattitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_DESCRIPTION1 = "description";
    private static final String KEY_IMAGEVIEW1 = "imageview";
    
    private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
    
    private static class DbHelper extends SQLiteOpenHelper{
		public DbHelper(Context context) {
			super(context,DATABASE_NAME1,null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("databaseDebug","OnCreate Method");
		// TODO Auto-generated method stub
        db.execSQL( "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_LATTITUDE + " TEXT, "+ KEY_LONGITUDE + " TEXT, "
                + KEY_DESCRIPTION1 + " TEXT, " + KEY_IMAGEVIEW1 + " Text);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.d("databaseDebug", "OnUpdate");
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
 
        // Create tables again
        onCreate(db);
	}
	}
	public trybase(Context c){
		ourContext = c;
	}
	public trybase open() throws SQLException{
		Log.d("databaseDebug","Open db");
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		Log.d("databaseDebug","Close db");
		ourHelper.close();
	}
	//Adding the detail
	public void addDetail(String lattitude, String longitude, String detail, String imageview) {
		Log.d("DatabaseDebug","addLocation");
		 
		 
		    ContentValues values = new ContentValues();
		   
		    values.put(KEY_LATTITUDE, lattitude); // Lattitude
		    values.put(KEY_LONGITUDE, longitude); // Longitude
		    values.put(KEY_DESCRIPTION1, detail); //  Detail
		    values.put(KEY_IMAGEVIEW1,imageview);
		    // Inserting Row
		     ourDatabase.insert(DATABASE_TABLE, null, values);
		     
	}
	public String getDetail(String lattit) throws SQLException{
		// TODO Auto-generated method stub
		 
	    Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
	            KEY_LATTITUDE, KEY_LONGITUDE, KEY_DESCRIPTION1, KEY_IMAGEVIEW1}, KEY_LATTITUDE + "=?",
	            new String[] { String.valueOf(lattit) }, null, null, null, null);
	   
	    if (cursor != null){
	        cursor.moveToFirst();
	        String namee = cursor.getString(3);
	        return namee;
	        }
	    return null;
	}
	//delete a detail
			public void deleteDetail(String lattit) throws SQLException{
				// TODO Auto-generated method stub
				ourDatabase.delete(DATABASE_TABLE, KEY_LATTITUDE + "=" + lattit, null);	

	}
			public String getImage(String lattit) throws SQLException{
				// TODO Auto-generated method stub
				 
			    Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
			            KEY_LATTITUDE, KEY_LONGITUDE, KEY_DESCRIPTION1, KEY_IMAGEVIEW1}, KEY_LATTITUDE + "=?",
			            new String[] { String.valueOf(lattit) }, null, null, null, null);
			   
			    if (cursor != null){
			        cursor.moveToFirst();
			        String namee = cursor.getString(4);
			        return namee;
			        }
			    return null;
			}
			
	
}


