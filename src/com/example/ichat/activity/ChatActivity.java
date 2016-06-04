package com.example.ichat.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ichat.R;
import com.example.ichat.R.id;
import com.example.ichat.R.layout;
import com.example.ichat.adapter.MsgAdapter;
import com.example.ichat.bean.Msg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends Activity{
	
	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter msgAdapter;
	private List<Msg> msgList = new ArrayList<Msg>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		initMsgs();
		initViews();
		msgAdapter = new MsgAdapter(ChatActivity.this, R.layout.activity_chat_item, msgList);
		msgListView.setAdapter(msgAdapter);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content = inputText.getText().toString();
				if(!"".equals(content)){
					Msg msg = new Msg(content, Msg.TYPE_SEND);
					msgList.add(msg);
					msgAdapter.notifyDataSetChanged();
					msgListView.setSelection(msgList.size());
					inputText.setText("");
				}
			}
		});
	}

	private void initViews() {
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		msgListView = (ListView) findViewById(R.id.msg_list);
	}

	private void initMsgs() {
		Msg msg1 = new Msg("你好，朋友~~",Msg.TYPE_RECEIVED);
		Msg msg2 = new Msg("你好，请问你是？",Msg.TYPE_SEND);
		Msg msg3 = new Msg("你猜，我们很多年没见了呢！",Msg.TYPE_RECEIVED);
		Msg msg4 = new Msg("又是这一套...",Msg.TYPE_SEND);
		msgList.add(msg1);
		msgList.add(msg2);
		msgList.add(msg3);
		msgList.add(msg4);
	}
}
