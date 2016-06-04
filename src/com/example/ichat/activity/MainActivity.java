package com.example.ichat.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.ichat.ChangeColorOfBottomView;
import com.example.ichat.R;
import com.example.ichat.ChatListView.IRefreshListener;
import com.example.ichat.R.id;
import com.example.ichat.R.layout;
import com.example.ichat.R.menu;
import com.example.ichat.adapter.MainAdapter;
import com.example.ichat.fragment.ChatFragment;
import com.example.ichat.fragment.ContactsFragment;
import com.example.ichat.fragment.FindFragment;
import com.example.ichat.fragment.MeFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener{

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private MainAdapter mAdapter;
	private List<ChangeColorOfBottomView> mTabIndicators = new ArrayList<ChangeColorOfBottomView>();
	private ChangeColorOfBottomView chat,contacts,find,me;
	private LinearLayout mBottomMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initDatas();
		initEvent();
	}
	
	private void initView(){
		setOverflowButtonAlways();
		//隐藏actionbar图标
		getActionBar().setDisplayShowHomeEnabled(false);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(4);
		mBottomMenu = (LinearLayout) findViewById(R.id.bottom_menu);
		find = (ChangeColorOfBottomView) findViewById(R.id.bottom_menu_find);
		chat = (ChangeColorOfBottomView) findViewById(R.id.bottom_menu_chat);
		contacts = (ChangeColorOfBottomView) findViewById(R.id.bottom_menu_contacts);
		me = (ChangeColorOfBottomView) findViewById(R.id.bottom_menu_me);
		mTabIndicators.add(find);
		mTabIndicators.add(chat);
		mTabIndicators.add(contacts);
		mTabIndicators.add(me);
		
		find.setIconAlpha(1.0f);
	}
	
	private void initDatas() {
		mTabs.clear();
		ChatFragment chatFragment = new ChatFragment();
		ContactsFragment contactsFragment = new ContactsFragment();
		FindFragment findFragment = new FindFragment();
		MeFragment meFragment = new MeFragment();
		mTabs.add(findFragment);
		mTabs.add(chatFragment);
		mTabs.add(contactsFragment);
		mTabs.add(meFragment);
		//初始化适配器
		mAdapter = new MainAdapter(getSupportFragmentManager(), mTabs);
		mViewPager.setAdapter(mAdapter);
	}
	/**
	 * 初始化所有事件
	 */
	private void initEvent() {
		mViewPager.addOnPageChangeListener(this);
		find.setOnClickListener(this);
		chat.setOnClickListener(this);
		contacts.setOnClickListener(this);
		me.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//设置ActionBar显示OverflowButton，并改变点击代表隐藏的menu键弹出菜单显示的位置
	private void setOverflowButtonAlways()
	{
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			//因Field内成员变量为pirvate
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//设置ActionBar隐藏的item显示icon
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) 
	{
		if(featureId == Window.FEATURE_ACTION_BAR && menu!=null)
		{
			if(menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
	//ActionBar隐藏菜单点击事件
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_chat:
			Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
			startActivity(chatIntent);
			break;
			
		case R.id.item_add_friends:
			Intent addIntent = new Intent(MainActivity.this, AddFriendsActivity.class);
			startActivity(addIntent);
			break;
			
		case R.id.item_scan:
			Intent scanIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivity(scanIntent);
			
			break;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		resetothertabs();
		switch (v.getId()) {
		case R.id.bottom_menu_find:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, true);
			break;
			
		case R.id.bottom_menu_chat:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, true);
			break;
			
		case R.id.bottom_menu_contacts:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, true);
			break;
			
		case R.id.bottom_menu_me:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, true);
			break;
		}
	}
	/**
	 * 重置其他的TabIndicator的颜色
	*/
	private void resetothertabs() {
		for(int i=0;i<mTabIndicators.size();i++){
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}
	/**
	 * 以下三个方法是注册addOnPageChangeListener必须实现的
	*/
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if(positionOffset>0){
			resetothertabs();
			ChangeColorOfBottomView left = mTabIndicators.get(position);
			ChangeColorOfBottomView right = mTabIndicators.get(position+1);
			left.setIconAlpha(1-positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}
	@Override
	public void onPageSelected(int position) {
		
	}
	@Override
	public void onPageScrollStateChanged(int position) {
		
	}

}
