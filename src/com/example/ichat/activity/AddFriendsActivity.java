package com.example.ichat.activity;

import com.example.ichat.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;

public class AddFriendsActivity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_add_friends);
//		SearchView searchFriends = (SearchView) findViewById(R.id.search_friends);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//				|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//		searchFriends.setOnCloseListener(new OnCloseListener() {
//			
//			@Override
//			public boolean onClose() {
//				return true;
//			}
//		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
