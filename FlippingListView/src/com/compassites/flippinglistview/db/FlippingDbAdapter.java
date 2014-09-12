
package com.compassites.flippinglistview.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.compassites.flippinglistview.data.ListInfo.ListData;


public class FlippingDbAdapter {

	private static final String TAG = FlippingDbAdapter.class.getSimpleName();

	private static FlippingDbAdapter sInstance = null;
	
	private FlippingDbHelper mOpenHelper = null;

	private SQLiteDatabase mdb = null;
	
	private static Context mContext;

	private FlippingDbAdapter(Context context) {

		mContext = context;
		
		mOpenHelper = FlippingDbHelper.getInstance(mContext);
		mdb = mOpenHelper.getWritableDatabase();
	}

	public static FlippingDbAdapter getInstance(Context context){

		if(sInstance == null){
			sInstance = new FlippingDbAdapter(context);
		}

		return sInstance;
	}

	public Cursor getCitynStateList(){

		Log.d(TAG, "getCitynStateList():");
		

		Cursor listCursor = mdb.query(FlippingDb.CITY_LIST_TABLE_NAME, null, null, null, null, null, null);
		
		if (listCursor != null) {
			Log.d(TAG, "getCitynStateList(): cursor count="+listCursor.getCount());
		}
		
		return listCursor;
	}

	public void insertAllCitynStateList(ListData cityArray[]){

		
		Log.d(TAG, "insertAllCitynStateList() called cityArray="+cityArray);
		
		if (cityArray != null && cityArray.length > 0) {
		
			int len = cityArray.length;
			 //excluding self from member array. 
			
			
			for (int i = 0; i < len; i++) {
				
				ContentValues cityListValues = new ContentValues();
				
				String cityName = cityArray[i].city;
				String stateName = cityArray[i].state;

				cityListValues.put(FlippingDb.CityList.CITY_NAME, cityName); 
				cityListValues.put(FlippingDb.CityList.STATE_NAME, stateName);
				mdb.insert(FlippingDb.CITY_LIST_TABLE_NAME, null, cityListValues);
				
			}
			
		}
		
	}
}
