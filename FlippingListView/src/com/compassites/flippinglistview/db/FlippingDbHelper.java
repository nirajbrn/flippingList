

package com.compassites.flippinglistview.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FlippingDbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = FlippingDbHelper.class.getSimpleName();

    private final String FLIPPING_DB_PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";

    private final String FLIPPING_DB_INTEGER_TYPE = "INTEGER";

    private final String FLIPPING_DB_NAME_TYPE = "VARCHAR(40)";

    private static FlippingDbHelper mInstance = null;

    public static synchronized FlippingDbHelper getInstance(Context aContext) {
    	
        if (mInstance == null) {
            mInstance = new FlippingDbHelper(aContext);
        }
        return mInstance;
    }

    private FlippingDbHelper(Context context) {
        super(context, FlippingDb.DATABASE_NAME, null, FlippingDb.DATABASE_VERSION);
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.v(TAG, "onCreate(): creating tables");
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeTables(db);
    }
    
    
    /**
     * Create tables
     * 
     * @param db db instance
     */
    public void createTables(SQLiteDatabase db) {
        db.beginTransaction();
       
        try {
        	
            db.execSQL("CREATE TABLE IF NOT EXISTS " + FlippingDb.CITY_LIST_TABLE_NAME + " ("
            		
                    + FlippingDb.CityList._ID + " " + FLIPPING_DB_PRIMARY_KEY_TYPE + ","
                    + FlippingDb.CityList.STATE_NAME + " " + FLIPPING_DB_NAME_TYPE + ","
                    + FlippingDb.CityList.CITY_NAME + " " + FLIPPING_DB_NAME_TYPE +  ");");
            
        } catch (SQLiteException e) {
            Log.e(TAG, "Error executing SQL(CITY_LIST_TABLE_NAME)");
            e.printStackTrace();
            return;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * context db table upgrade
     * 
     * @param db db instance
     */
    public void upgradeTables(SQLiteDatabase db) {
  
        db.execSQL("DROP TABLE IF EXISTS " + FlippingDb.CITY_LIST_TABLE_NAME);

        createTables(db);
    }
}
