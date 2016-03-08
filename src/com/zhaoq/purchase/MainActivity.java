package com.zhaoq.purchase;

import com.example.purchase.OperateFragment;
import com.zhaoq.purchase.R;
import com.zhaoq.fragment.CheckFragment;
import com.zhaoq.fragment.ExamineGoodsFragment;
import com.zhaoq.fragment.OnCheckFragment;
import com.zhaoq.fragment.OnExampleFragment;
import com.zhaoq.fragment.OnPurchaseFragment;
import com.zhaoq.fragment.PurchaseFragment;
import com.zhaoq.fragment.StockFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 采购项目
 * com.zhaoq.purchase
 * @Email zhaoq_hero@163.com
 * @author zhaoQiang : 2016-3-8
 */
public class MainActivity extends FragmentActivity {
	FragmentManager manager;
	private String userno, name;
	private int select;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		manager = getSupportFragmentManager();
		intData();
		eventFragment();
	}

	private void intData() {
		Intent user = getIntent();
		userno = user.getStringExtra("id");
		name = user.getStringExtra("name");
		select = user.getIntExtra("select", 0);
	}

	private void eventFragment() {
		switch (select) {
		case 0:
			Fragment pc = PurchaseFragment.getFragment(userno, name); // 采购
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, pc).commit();

			break;
		case 1:
			Fragment sc = StockFragment.getFragment(); // 库存
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, sc).commit();

			break;
		case 2:
			Fragment cc = CheckFragment.getFragment(userno, name); // 盘点
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, cc).commit();
			break;
		case 3:
			Fragment op = OnPurchaseFragment.getFragment(); // 已采购
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, op).commit();
			break;
		case 4:

			Fragment oa = new OperateFragment(); // 操作
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, oa).commit();
			break;
		case 5:

			Fragment ex =ExamineGoodsFragment.getFragment(userno, name); // 操作
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, ex).commit();
			break;
		case 6:
			Fragment fex = new OnExampleFragment(); // 已验货
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, fex).commit();
			break;
		case 7:
			Fragment  oc= new OnCheckFragment(); // 已
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.linear, oc).commit();
			break;
		default:
			break;
		}

	}

}
