package com.jesusjzp.sanwensuibi;

import java.util.Random;

import com.jesusjzp.sanwensuibi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {
	
	int[] imgArray = {R.drawable.img01, R.drawable.img02,
					R.drawable.img03, R.drawable.img04,
					R.drawable.img05, R.drawable.img06};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.welcome);
		LinearLayout ll = (LinearLayout) findViewById(R.id.welcome);

		ll.setBackgroundResource(imgArray[(int)(Math.random()*6)]);

		Handler x = new Handler();
		x.postDelayed(new StartThread(), 2000);
	}

	class StartThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			startActivity(new Intent(getApplication(),
					ExampleListActivity.class));
			WelcomeActivity.this.finish();
		}

	}
}
