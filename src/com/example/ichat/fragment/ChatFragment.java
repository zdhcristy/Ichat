package com.example.ichat.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.ichat.ChatListView;
import com.example.ichat.ChatListView.IRefreshListener;
import com.example.ichat.R;
import com.example.ichat.activity.ChatActivity;
import com.example.ichat.adapter.ChatAdapter;
import com.example.ichat.bean.ChatItem;

public class ChatFragment extends Fragment implements OnItemClickListener,IRefreshListener{
	
	private ChatListView mListView;
	private ChatAdapter chatAdapter;
	private List<ChatItem> chatList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container , false);
		mListView = (ChatListView)view.findViewById(R.id.lv_chat);
		getDatas();
		showData(chatList);
		mListView.setInterface(this);
		mListView.setOnItemClickListener(this);
		return view;
	}
	
	private void showData(List<ChatItem> list){
		if (chatAdapter == null) {
			chatAdapter = new ChatAdapter(getContext(), list);
			mListView.setAdapter(chatAdapter);
		} else {
			chatAdapter.onDataChange(list);
		}
	}
	
	private void getDatas() {
		chatList = new ArrayList<ChatItem>();
		for (int i = 0; i < 18; i++) {
			ChatItem chatItem = new ChatItem();
			chatItem.setPictureId(R.drawable.item_group_chat);
			chatItem.setContent("大家一起来Ichat聊天"+i);
			chatList.add(chatItem);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getContext(),ChatActivity.class);
		getContext().startActivity(intent);
	}
    //下拉更新数据
	@Override
	public void onRefresh() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				getRefreshData();
				showData(chatList);
				mListView.reflashComplete();
			}
		}, 1444);
	}
	//底部分页加载数据
	@Override
	public void onLoadMore() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				getLoadData();
				showData(chatList);
				mListView.loadComplete();
			}
		}, 1444);
	}
	//下拉更新的数据
	private void getRefreshData() {
		for (int i = 0; i < 2; i++) {
			ChatItem chatItem = new ChatItem();
			chatItem.setPictureId(R.drawable.item_group_chat);
			chatItem.setContent("顶部新增Ichat聊天"+i);
			chatList.add(chatItem);
		}
	}
	//底部分页更新的数据
	private void getLoadData() {
		for (int i = 0; i < 2; i++) {
			ChatItem chatItem = new ChatItem();
			chatItem.setPictureId(R.drawable.item_group_chat);
			chatItem.setContent("底部新增Ichat聊天"+i);
			chatList.add(chatItem);
		}
	}
	
}
