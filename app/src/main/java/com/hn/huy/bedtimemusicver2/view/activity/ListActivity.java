package com.hn.huy.bedtimemusicver2.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hn.huy.bedtimemusicver2.R;


public class ListActivity extends Activity {
	
	/**
	 * 
	 * Activity chi hien thi fragment tu xml
	 * fragment chua cac tag host
	 * 
	 * */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_list);
		
	}

}
