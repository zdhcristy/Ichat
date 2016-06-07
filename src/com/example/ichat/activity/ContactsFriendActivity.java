package com.example.ichat.activity;

import com.example.ichat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ContactsFriendActivity extends Activity{
	
	private TextView frientName;
	private TextView frientNumber;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_contacts_friend);
		frientName = (TextView) findViewById(R.id.friend_name);
		frientNumber = (TextView) findViewById(R.id.friend_number);
		Intent intent = getIntent();
		frientName.setText(intent.getStringExtra("name"));
		frientNumber.setText(intent.getStringExtra("number"));
		
		Button sendMessage = (Button) findViewById(R.id.send_message);
		sendMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ContactsFriendActivity.this,ChatActivity.class);
				intent.putExtra("label", frientName.getText().toString());
				startActivity(intent);
			}
		});
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
