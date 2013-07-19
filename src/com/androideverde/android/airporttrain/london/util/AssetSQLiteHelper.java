package com.androideverde.android.airporttrain.london.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class provides all the interaction with the internal database, including the creation
 * from the file in the dataspace and all the queries.
 * 
 * @author Jordi
 */
public class AssetSQLiteHelper extends SQLiteOpenHelper {
	private Context context;
	private static String TAG = "AssetSQLiteHelper";

	// DB is stored in the dataspace of our install, not in SD or anywhere else
	private String DB_PATH = "/data/data/com.androideverde.android.airporttrain.london/databases/";
	//private String DB_PATH = mycontext.getApplicationContext().getPackageName() + "/databases/";
	private static String DB_NAME = "timetable.db";
	public SQLiteDatabase db;
	private static int DB_VERSION = 1;

	// all the columns in our table
	private String[] columns = { "_id", "direction", "day", "trainId",
			"stopId", "stopText", "departTime", "arriveTime", "company" };

	
	public AssetSQLiteHelper(Context context) throws IOException {
	    super(context, DB_NAME, null, DB_VERSION);
	    this.context = context;
	}

	/**
	 * Creates the database from the assets file if no database exists.
	 * @throws IOException
	 */
	public void createdatabase() throws IOException {
	    boolean dbexist = checkdatabase();
	    if(dbexist)
	    {
	        //System.out.println(" Database exists.");
	    }
	    else{
	        this.getReadableDatabase();
	        try{
	            copydatabase();
	        }
	        catch(IOException e){
	            throw new Error("Error copying database");
	        }
	    }
	}

	/**
	 * Removes the existing database.
	 * @return Success of the removal action
	 */
	public boolean removedatabase() {
		boolean dbexist = checkdatabase();
		if(dbexist) {
			return context.deleteDatabase(DB_NAME);
		} else {
			//db not present
			return false;
		}
	}
	
	/**
	 * Checks if the database exists.
	 * @return true if database exists
	 */
	private boolean checkdatabase() {
	    boolean checkdb = false;
	    try{
	        String myPath = DB_PATH + DB_NAME;
	        File dbfile = new File(myPath);
	        checkdb = dbfile.exists();
	    }
	    catch(SQLiteException e){
	        Log.i(TAG, "Check: Database doesn't exist");
	    }
	    return checkdb;
	}
	
	/**
	 * Copies database file from assets into app dataspace
	 * @throws IOException
	 */
	private void copydatabase() throws IOException {

	    //Open your local db as the input stream
	    InputStream myinput = context.getAssets().open(DB_NAME);

	    // Path to the just created empty db
	    String outfilename = DB_PATH + DB_NAME;

	    //Open the empty db as the output stream
	    OutputStream myoutput = new FileOutputStream(outfilename);

	    // transfer byte to inputfile to outputfile
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = myinput.read(buffer))>0)
	    {
	        myoutput.write(buffer,0,length);
	    }

	    //Close the streams
	    myoutput.flush();
	    myoutput.close();
	    myinput.close();
	}

	/**
	 * Opens the database.
	 * @throws SQLException
	 */
	public void opendatabase() throws SQLException {
	    //Open the database
	    String mypath = DB_PATH + DB_NAME;
	    db = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * Closes the database if open.
	 */
	public synchronized void close() {
	    if(db != null){
	        db.close();
	    }
	    super.close();
	}

	/**
	 * Empty but forced to be present because of abstract SQLiteOpenHelper.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	/**
	 * Empty but forced to be present because of abstract SQLiteOpenHelper.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	// Database query methods
	
	/**
	 * Queries database for number of elements.
	 * @return Number of rows in database
	 */
	public int getCounts() {
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM timetable", null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}
	
	/**
	 * Queries database for full data row.
	 * 
	 * @param direction Indicates both airport and direction (1 = from Gatwick, 11 = to Gatwick, 2 = from Luton, 12 = to Luton, ...) 
	 * @param station Station code that is origin or destination depending on direction
	 * @param day Either "week", "sat" or "sun" to indicate if it´s a weekday, Saturday or Sunday
	 * 
	 * @return A cursor containing the matching rows
	 */
	public Cursor getCursorTrainTimes(int direction, String station, String day) {
		Cursor cursor = db.query("timetable", columns, "direction = " + direction + " AND stopId = '" + station + "' AND day = '" + day + "'", null, null, null, "departTime");
		return cursor;
	}
	
//	/**
//	 * Queries database specifically for data from Gatwick. It's identical as calling getCursorTrainTimes() with direction = 1.
//	 * 
//	 * @return A cursor containing the matching rows
//	 */
//	public Cursor getCursorTrainTimesFromGatwick() {
//		Cursor cursor = db.query("timetable", columns, "direction = 1", null, null, null, null);
//		return cursor;
//	}
//	
//	public Cursor getCursorTrainTimesToGatwick() {
//		Cursor cursor = db.query("timetable", columns, "direction = 2", null, null, null, null);
//		return cursor;
//	}
}
