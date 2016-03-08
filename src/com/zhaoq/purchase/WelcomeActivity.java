package com.zhaoq.purchase;

import com.zhaoq.purchase.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

/**
 * com.zhaoq.purchase
 * @Email zhaoq_hero@163.com
 * @author zhaoQiang : 2016-3-8
 */
public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);	
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			new myAsk().execute();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class myAsk extends AsyncTask<Void,Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
			Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
			super.onPostExecute(result);
		}
	
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
