package com.compassites.flippinglistview;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

public class FlippingApplication extends Application {

	private static final String TAG = FlippingApplication.class.getSimpleName();

	private static FlippingApplication mFlipAppInstance = null;

	public static FlippingApplication getInstance() {

		if (mFlipAppInstance == null) {
			mFlipAppInstance = new FlippingApplication();
		}

		return mFlipAppInstance;
	}

	@Override
	public void onCreate() {

		super.onCreate();

		Log.d(TAG, "onCreate() called");

		mFlipAppInstance = this;
		
		intialize();
		
	}

	public void intialize(){
		
		Log.d(TAG, "intialize() called");
		
	}
	
}
