package com.example.ichat.activity;

import com.example.ichat.R;
import com.example.ichat.R.id;
import com.example.ichat.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends Activity{
	
	private LinearLayout welcomeLayout;
	private AlphaAnimation start_anima;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		welcomeLayout = (LinearLayout) findViewById(R.id.welcome_layout);
		initData();
	}

	private void initData() {
		start_anima = new AlphaAnimation(0.1f, 1.0f);
		start_anima.setDuration(1888);
		welcomeLayout.setAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
				finish();
			}
		});
	}
}
