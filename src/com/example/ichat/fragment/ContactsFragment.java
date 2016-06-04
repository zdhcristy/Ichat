package com.example.ichat.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ichat.R;
import com.example.ichat.R.drawable;
import com.example.ichat.R.id;
import com.example.ichat.R.layout;
import com.example.ichat.activity.ChatActivity;
import com.example.ichat.activity.ContactsFriendActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ContactsFragment extends Fragment implements OnItemClickListener{
	
	private ListView mListView;
	private SimpleAdapter contactsAdapter;
	private List<Map<String, Object>> datas;
	private List<String> nameList = new ArrayList<String>();
	private List<String> numberList = new ArrayList<String>();
	public static final String PICTURE = "picture";
	public static final String NAME = "content";
	private String contactsName;
	private String contactsNumber;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container , false);
		mListView = (ListView) view.findViewById(R.id.lv_contacts);
		datas = new ArrayList<Map<String,Object>>();
		readContacts();
		contactsAdapter = new SimpleAdapter(getContext(), getDatas(), R.layout.fragment_contacts_item, 
				new String[]{PICTURE,NAME}, new int[]{R.id.lv_contacts_picture,R.id.lv_contacts_name});
		mListView.setAdapter(contactsAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}
	
	private List<Map<String, Object>> getDatas() {
		for (int i = 0; i < nameList.size(); i++) {
			contactsName = nameList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(PICTURE, R.drawable.ic_list_friend);
			map.put(NAME, contactsName);
			datas.add(map);
		}
		return datas;
	}
	//读取系统联系人
	private void readContacts(){
		Cursor cursor = null;
		try{
			cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,null,null,null);
			while(cursor.moveToNext()){
				String displayName = cursor.getString(cursor.getColumnIndex(
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String displayNumber = cursor.getString(cursor.getColumnIndex(
						ContactsContract.CommonDataKinds.Phone.NUMBER));
				nameList.add(displayName);
				numberList.add(displayNumber);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		contactsName = nameList.get(position);
		contactsNumber = numberList.get(position);
		Intent intent = new Intent(getContext(),ContactsFriendActivity.class);
		intent.putExtra("name", contactsName);
		intent.putExtra("number", contactsNumber);
		getContext().startActivity(intent);
	}
}
