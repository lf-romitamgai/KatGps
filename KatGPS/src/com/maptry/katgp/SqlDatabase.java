package com.maptry.katgp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlDatabase{
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "mapManager.db";
 
    // Contacts table name
    private static final String DATABASE_TABLE = "searchit";
   
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "place";
    private static final String KEY_LATTITUDE = "lattitude";
    private static final String KEY_LONGITUDE = "longitude";
   
    
    private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{
		public DbHelper(Context context) {
			super(context,DATABASE_NAME,null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("databaseDebug","OnCreate Method");
		// TODO Auto-generated method stub
        db.execSQL( "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_LATTITUDE + " TEXT, " + KEY_LONGITUDE + " Text);");
		
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
	public SqlDatabase(Context c){
		ourContext = c;
	}
	public SqlDatabase open() throws SQLException{
		Log.d("databaseDebug","Open db");
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		Log.d("databaseDebug","Close db");
		ourHelper.close();
	}

	// Adding new Location
	public void addLocation(String name, String lattitude, String longitude) {
		Log.d("DatabaseDebug","addLocation");
		 
		 
		    ContentValues values = new ContentValues();
		    values.put(KEY_NAME, name); //  Name
		    values.put(KEY_LATTITUDE, lattitude); // Lattitude
		    values.put(KEY_LONGITUDE, longitude); // Longitude
		 
		    // Inserting Row
		     ourDatabase.insert(DATABASE_TABLE, null, values);
	}
	
	// Getting lattitude
	public String getLattitude(String place) throws SQLException{
		 
	    Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
	            KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE }, KEY_NAME + "=?",
	            new String[] { String.valueOf(place) }, null, null, null, null);
	   
	    if (cursor != null){
	        cursor.moveToFirst();
	        String latt = cursor.getString(2);
	        return latt;
	        }
	    return null;
	}
	// Getting longitude
		public String getLongitude(String place) throws SQLException{
			
			 
		    Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
		            KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE }, KEY_NAME + "=?",
		            new String[] { String.valueOf(place) }, null, null, null, null);
		   
		    if (cursor != null){
		        cursor.moveToFirst();
		        String longi = cursor.getString(3);
		        return longi;
		        }
		    return null;
		}
		//to view in a new intent
		public String getData() {
			// TODO Auto-generated method stub
			String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE};
			Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
			String result = "";
			
			int idd = c.getColumnIndex(KEY_ID);
			int iName = c.getColumnIndex(KEY_NAME);
			int iLatt = c.getColumnIndex(KEY_LATTITUDE);
			int iLong = c.getColumnIndex(KEY_LONGITUDE);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				result = result + c.getString(idd) + "\t" + c.getString(iName).replace('_', ' ') + "\t" + c.getString(iLatt) + "\t" + c.getString(iLong) + "\n";
				
			}
			return result;
		}
		// Name list searching function
				 public List<String> getAllLabels(){
				        
					 List<String> labels = new ArrayList<String>();
				        
					 String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE};
						Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
						
						
						
						if(c.moveToFirst()){
							do{
								String tempStr = c.getString(1);
								tempStr = tempStr.replace('_', ' ');
								 labels.add(tempStr);
				            } while (c.moveToNext());
				        
							
						}
						return labels;
				    }
		//delete a entry
		public void deleteEntry(long lRow1) throws SQLException{
			// TODO Auto-generated method stub
			ourDatabase.delete(DATABASE_TABLE, KEY_ID + "=" + lRow1, null);
			String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE};
			Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
			

}
		public void updateEntry(long lRow, String mName, String lattitude, String longitude) throws SQLException{
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, mName);
			values.put(KEY_LATTITUDE, lattitude); // Lattitude
		    values.put(KEY_LONGITUDE, longitude); // Longitude
			ourDatabase.update(DATABASE_TABLE, values, KEY_ID + "=" + lRow, null);
		}
		public void addAll(String name, String lattitude, String longitude){

			 
		    ContentValues values = new ContentValues();
		    values.put(KEY_NAME, name); //  Name
		    values.put(KEY_LATTITUDE, lattitude); // Lattitude
		    values.put(KEY_LONGITUDE, longitude); // Longitude
		    
		    // Inserting Row
		     ourDatabase.insert(DATABASE_TABLE, null, values);
		}
		public String getName(String lattit) throws SQLException{
			// TODO Auto-generated method stub
			 
		    Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
		            KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE }, KEY_LATTITUDE + "=?",
		            new String[] { String.valueOf(lattit) }, null, null, null, null);
		   
		    if (cursor != null){
		        cursor.moveToFirst();
		        String namee = cursor.getString(1);
		        return namee;
		        }
		    return null;
		}
	
		public String getDetail(String lattit) throws SQLException{
			// TODO Auto-generated method stub
			 
		    Cursor cursor = ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
		            KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE }, KEY_LATTITUDE + "=?",
		            new String[] { String.valueOf(lattit) }, null, null, null, null);
		   
		    if (cursor != null){
		        cursor.moveToFirst();
		        String namee = cursor.getString(4).replace('_', ' ');
		        return namee;
		        }
		    return null;
		}		
		//delete a name
				public void deleteName(String lattit) throws SQLException{
					// TODO Auto-generated method stub
					ourDatabase.delete(DATABASE_TABLE, KEY_LATTITUDE + "=" + lattit, null);
					

		}
			
				//to add name in myPlaces
				public String getMyPlace() {
					// TODO Auto-generated method stub
					String[] columns = new String[]{KEY_ID, KEY_NAME, KEY_LATTITUDE, KEY_LONGITUDE};
					Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
					String result = "";
					
					
					int iName = c.getColumnIndex(KEY_NAME);
					
					for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
						result = result + c.getString(iName).replace('_', ' ') +  "\n";
						
					}
					return result;
				}
					
					 
				 
}
