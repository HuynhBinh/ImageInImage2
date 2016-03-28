package com.hnb.imageinimage2;


import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class GuideActivity extends Activity 
{
	
	TextView tv1 ;
	TextView tv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		tv2 = (TextView)findViewById(R.id.textView2);
		
		final Animation a = AnimationUtils.loadAnimation(this, R.anim.combine);
		final Animation a1 = AnimationUtils.loadAnimation(this, R.anim.scale);
					
		tv1.startAnimation(a);
		tv2.startAnimation(a1);
	}
	

}
