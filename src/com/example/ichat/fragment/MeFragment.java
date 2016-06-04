package com.example.ichat.fragment;

import com.example.ichat.R;
import com.example.ichat.activity.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MeFragment extends Fragment implements OnClickListener{
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_me, container , false);
		LinearLayout setting = (LinearLayout) view.findViewById(R.id.me_setting);
		LinearLayout gallery = (LinearLayout) view.findViewById(R.id.me_gallery);
		setting.setOnClickListener(this);
		gallery.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.me_setting:
			Intent settingIntent = new Intent(getContext(),SettingActivity.class);
			startActivity(settingIntent);
			break;

		case R.id.me_gallery:
			Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivity(galleryIntent);
			break;
		}
		
	}
}
