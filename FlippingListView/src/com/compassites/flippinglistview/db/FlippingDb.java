
package com.compassites.flippinglistview.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class FlippingDb {

	public static final String DATABASE_NAME = "flipping.db";

	public static final int DATABASE_VERSION = 6;

	public static final String AUTHORITY = "com.compassites.android.flipping.db.provider";

	public static final String CITY_LIST_TABLE_NAME = "city_list";
	
	public static final String MESSAGE_AND_MEMBERS_VIEW_NAME = "message_memebers_view"; //it is a view


	public static final class CityList implements BaseColumns, CityListColumns {

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CITY_LIST_TABLE_NAME);
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.compassites.android.flipping.db.cityList";

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.compassites.android.flipping.db.cityList";
	}


	private  static interface CityListColumns{

		final String CITY_NAME = "city_name";

		final String STATE_NAME = "state_name";

	}

}
