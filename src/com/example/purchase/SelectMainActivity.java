package com.example.purchase;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhaoq.purchase.MainActivity;
import com.zhaoq.purchase.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectMainActivity extends Activity implements OnClickListener {

	int position = 0;
	private long firstTime = 0;
	private String userno, name;
	@ViewInject(R.id.purchase)
	private ImageButton purchase;
	@ViewInject(R.id.stock)
	private ImageButton stock;
	@ViewInject(R.id.check)
	private ImageButton check;
	@ViewInject(R.id.yipandian)
	private ImageButton yipandian;
	@ViewInject(R.id.onorder)
	private ImageButton onorder;
	@ViewInject(R.id.caozuo)
	private ImageButton caozuo;

	@ViewInject(R.id.examinegoods)
	private ImageButton examinegoods;
	@ViewInject(R.id.yiyanhuo)
	private ImageButton yiyanhuo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_main);
		ViewUtils.inject(this);
		purchase.setOnClickListener(this);
		stock.setOnClickListener(this);
		check.setOnClickListener(this);
		onorder.setOnClickListener(this);
		
		caozuo.setOnClickListener(this);
		
		examinegoods.setOnClickListener(this);
		yiyanhuo.setOnClickListener(this);
		yipandian.setOnClickListener(this);
		
		intData();
	}

	private void intData() {
		Intent user = getIntent();
		userno = user.getStringExtra("id");
		name = user.getStringExtra("name");
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
				Toast.makeText(SelectMainActivity.this, "再按一次退出程序...",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("id", userno);
		intent.putExtra("name", name);
		int id = v.getId();

		switch (id) {
		case R.id.purchase:
			intent.putExtra("select", 0);//盘点
			break;
		case R.id.stock:
			intent.putExtra("select", 1);//库存
			break;
		case R.id.check:
			intent.putExtra("select", 2);//盘点
			break;
		case R.id.yipandian:
			intent.putExtra("select", 7);//已盘点
			break;
		case R.id.onorder:
			intent.putExtra("select", 3);//已采购
			break;
		case R.id.caozuo:
			intent.putExtra("select", 4);//操作
			break;
		case R.id.examinegoods:
			intent.putExtra("select", 5);//验货
			break;
		case R.id.yiyanhuo:
			intent.putExtra("select", 6);//已验货
			break;

		}
		startActivity(intent);

	}
}
