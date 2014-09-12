package com.compassites.flippinglistview.data;

import java.util.ArrayList;

public class ListInfo {

ArrayList<ListData> cityDatalist = new ArrayList<ListData>();
	
	public void setData(String city, String state){
		cityDatalist.add(new ListData(city, state));
	}
	
	public ArrayList<ListData> getData(){
		return cityDatalist;
	}
	
	public class ListData{
		public String city;
		public String state;
		
		public ListData(String city, String state){
			this.city = city;
			this.state = state;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ListData)) {
			    return false;
			  }
			ListData delUser = (ListData) o;
			  return city.equals(delUser.city);
		}
	}
	
}
