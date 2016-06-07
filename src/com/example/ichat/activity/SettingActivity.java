package com.example.ichat.activity;

import com.example.ichat.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("…Ë÷√");
		setContentView(R.layout.activity_setting);
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
