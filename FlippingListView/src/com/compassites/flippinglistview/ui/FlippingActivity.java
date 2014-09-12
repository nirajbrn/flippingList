package com.compassites.flippinglistview.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.compassites.flippinglistview.R;
import com.compassites.flippinglistview.data.ListInfo;
import com.compassites.flippinglistview.data.ListInfo.ListData;
import com.compassites.flippinglistview.db.FlippingDbAdapter;
import com.compassites.flippinglistview.utility.Rotate3dAnimation;

public class FlippingActivity extends Activity implements LoaderCallbacks<List<ListData>>{
	static String TAG = FlippingActivity.class.getSimpleName();
	
	Context aContext = null;
    ListView mListView = null;
    private MyArrayAdapter mParseAdapter;
    ArrayList<ListData> mCityList; // list of data  to be added
    ArrayList<ListData> mtempList;
    ViewHolder holder = null;
    private boolean flipFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_flipping);
		
		aContext = FlippingActivity.this; 
		mCityList = new ArrayList<ListData>();
        mtempList = new ArrayList<ListData>();
        mListView = (ListView)findViewById(android.R.id.list);
        mParseAdapter = new MyArrayAdapter(aContext, null);
        mListView.setAdapter(mParseAdapter);
        getLoaderManager().initLoader(0, null, FlippingActivity.this);
        
        
        Button addBtn = (Button)findViewById(R.id.add_item);
        addBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				flipFlag = false;
				if(mtempList != null && mtempList.size() != 0){
					ListData selectedUser = mtempList.get(0);
					mCityList.add(selectedUser);
					mtempList.remove(selectedUser);
					Log.d(TAG, "addBtn onClick(): mUserList.size() = "+mCityList.size()+" mtempList.size() = "+mtempList.size());
					mParseAdapter.setData(mCityList);
				}
				
			}
		});
        
        Button flipBtn = (Button)findViewById(R.id.flip_item);
        flipBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				flipFlag = true;
				AnimationSet set = new AnimationSet(true);

		        Animation animation = new AlphaAnimation(0.0f, 1.0f);
		        animation.setDuration(80);
		        set.addAnimation(animation);

		        animation = new TranslateAnimation(
		            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
		            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
		        );
		        animation.setDuration(500);
		        set.addAnimation(applyRotation(0,0,360));

		        LayoutAnimationController controller =
		                new LayoutAnimationController(set, 0.5f);
		        controller.setDelay(1.0f);
		        mListView.setLayoutAnimation(controller);
				mParseAdapter.setData(mCityList);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flipping, menu);
		return true;
	}

	private class MyArrayAdapter extends BaseAdapter{

        Context mContext;
        List<ListData> mListDataList;
        private LayoutInflater mInflater = null;
       
        public MyArrayAdapter(Context mContext, ArrayList<ListData> data){
           
            this.mContext = mContext;
            this.mListDataList = data;
            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
       
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mListDataList != null ? mListDataList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
       
        public void setData(List<ListData> data) {
            mListDataList = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.city_list_item, null);
                holder.city_name = (TextView)convertView.findViewById(R.id.city_name);
                holder.state_name = (TextView)convertView.findViewById(R.id.state_name);
                holder.delete_btn = (ImageButton)convertView.findViewById(R.id.delete_icon);
                convertView.setTag(holder);
               
            }else {
                holder = (ViewHolder)convertView.getTag();
               
            }
            
            if(flipFlag){
        		holder.city_name.setVisibility(View.GONE);
        		holder.state_name.setVisibility(View.VISIBLE);
        	}else{
        		holder.city_name.setVisibility(View.VISIBLE);
        		holder.state_name.setVisibility(View.GONE);
        	}
            
            ListData data = mListDataList.get(position);
           
            holder.city_name.setText(data.city);
            holder.state_name.setText(data.state);
            
            final int pos = position;
            
            holder.delete_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DeleteItem(pos);
					
				}
			});
           
            return convertView;
        }
       
        public void DeleteItem(int position){
            Log.d(TAG, "DeleteItem(): before mListDataList.size() = "+mListDataList.size()+"position : "+position);
           
            //mListDataList.remove(getItem(position));
           
            mListDataList.remove(position);
           
            Log.d(TAG, "DeleteItem(): after mListDataList.size() = "+mListDataList.size());
            notifyDataSetChanged();
        }
       
    }
	
	 class ViewHolder{

         TextView city_name;
         TextView state_name;
         ImageButton delete_btn;

     }
	
	public static class parsingListLoader extends AsyncTaskLoader<List<ListData>>{

        List<ListData> mListDataList;
        ProgressDialog progressDialog = null;
       Context context;
        public parsingListLoader(Context context) {
            super(context);
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        public List<ListData> loadInBackground() {
        	
            Log.d(TAG, "doInBackground():");
            
            ListInfo listinfo = new ListInfo();
            FlippingDbAdapter dbAdapter = FlippingDbAdapter.getInstance(context);
            ListData cityArray[] = loadData();
            
            dbAdapter.insertAllCitynStateList(cityArray);
            
            Cursor listCursor = dbAdapter.getCitynStateList();
            while(listCursor.moveToNext()){
            	
            	String city = listCursor.getString(2);
            	String state = listCursor.getString(1);
            	
            	listinfo.setData(city, state);
            	
            }
            
            return listinfo.getData();
        }
       
        /**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
        @Override
        public void deliverResult(List<ListData> data) {

            Log.d(TAG, "deliverResult() called. data="+data);

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if (isReset()) {
                if (data != null) {
                    onReleaseResources(data);
                }
            }

            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            Log.d(TAG, "onStartLoading() called.");

            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
           
            if (mListDataList != null) {
                deliverResult(mListDataList);
            }

            if (takeContentChanged() || mListDataList == null) {
                forceLoad();
            }

        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            Log.d(TAG, "onStopLoading() called.");
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * Handles a request to cancel a load.
         */
        @Override
        public void onCanceled(List<ListData> data) {
            super.onCanceled(data);
            Log.d(TAG, "onCanceled() called.");

            // At this point we can release the resources associated with 'data'
            // if needed.
            onReleaseResources(data);
        }

        /**
         * Handles a request to completely reset the Loader.
         */
        @Override
        protected void onReset() {
            super.onReset();
            Log.d(TAG, "onReset() called.");

            onStopLoading();
            mListDataList = null;

        }

        /**
         * Helper function to take care of releasing resources associated
         * with an actively loaded data set.
         */
        protected void onReleaseResources(List<ListData> data) {
            Log.d(TAG, "onReleaseResources() called. data="+data);
            // For a simple List<> there is nothing to do.  For something
            // like a Cursor, we would close it here.
        }
       
    }
	
	@Override
	public Loader<List<ListData>> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return new parsingListLoader(aContext);
	}

	@Override
	public void onLoadFinished(Loader<List<ListData>> loader,
			List<ListData> data) {
		if(mtempList != null && mtempList.size() == 0){
    		mtempList.addAll(data);
    		Log.d(TAG, "onLoadFinished(): mtempList.size() = "+mtempList.size());
    	}
        mParseAdapter.setData(mCityList);
		
	}

	@Override
	public void onLoaderReset(Loader<List<ListData>> loader) {
		mParseAdapter.setData(null);
		
	}

	private static ListData[] loadData() {

		ListData cityArray[] = new ListData[9];
		/*for (int i = 0; i < cityArray.length; i++) {
		cityArray[i].city = "Bangalore";
		cityArray[i].state = "Karnataka";
	}*/

		ListInfo lInfo = new ListInfo();
		cityArray[0] = lInfo.new ListData("Bangalore", "Karnataka");
		cityArray[1] = lInfo.new ListData("Kolkata", "WestBengal");
		cityArray[2] = lInfo.new ListData("Mumbai", "Maharastra");
		cityArray[3] = lInfo.new ListData("Lucknow", "UttarPradesh");
		cityArray[4] = lInfo.new ListData("Bhopal", "MadhyaPradesh");
		cityArray[5] = lInfo.new ListData("Patna", "Bihar");
		cityArray[6] = lInfo.new ListData("Chandigadh", "Punjab");
		cityArray[7] = lInfo.new ListData("Chennai", "Tamilnadu");
		cityArray[8] = lInfo.new ListData("Gaya", "Bihar");
		/*cityArray[1].city = "Kolkata";
		cityArray[1].state = "WestBengal";

		cityArray[2].city = "Mumbai";
		cityArray[2].state = "Maharastra";

		cityArray[3].city = "Lucknow";
		cityArray[3].state = "UP";

		cityArray[4].city = "Bhopal";
		cityArray[4].state = "MP";

		cityArray[5].city = "Jabalpur";
		cityArray[5].state = "MP";

		cityArray[6].city = "Gaya";
		cityArray[6].state = "Bihar";

		cityArray[7].city = "Patna";
		cityArray[7].state = "Bihar";

		cityArray[8].city = "Chennai";
		cityArray[8].state = "Tamilnadu";

		cityArray[9].city = "Vishakhapatnam";
		cityArray[9].state = "AndhraPradesh";

		cityArray[10].city = "Bangalore";
		cityArray[10].state = "Karnataka";

		cityArray[11].city = "Bangalore";
		cityArray[11].state = "Karnataka";

		cityArray[12].city = "Bangalore";
		cityArray[12].state = "Karnataka";

		cityArray[13].city = "Bangalore";
		cityArray[13].state = "Karnataka";

		cityArray[14].city = "Bangalore";
		cityArray[14].state = "Karnataka";

		cityArray[15].city = "Bangalore";
		cityArray[15].state = "Karnataka";

		cityArray[16].city = "Bangalore";
		cityArray[16].state = "Karnataka";
*/
		return cityArray;
	}
	
	
	private Rotate3dAnimation applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = 100.0f;//view.getWidth()/2.0f;
        final float centerY = 20.0f;//view.getHeight()/2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        return rotation;
    }
 
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {

        }

        public void onAnimationRepeat(Animation animation) {
        }
    }
}
