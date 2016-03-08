package com.zhaoq.purchase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.zhaoq.purchase.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhaoq.bean.wh_StockVerifyDetail;

import DB.GetMyDb;
import DB.Select;
import Tool.Config;
import Tool.IsNetConnected;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OnExampleGoodsContentMainActivity extends Activity {
	private ListView listview;
	private List<wh_StockVerifyDetail> list;
	private TextView shangjia, name, date;
	SimpleCursorAdapter adapter;
	Button imageView;
	int lastX, lastY, offsetX, offsetY;
	String No, Date, cSupplier, cOperator;
	ImageView finish;
	private TextView yiyanhuo;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_main);
		yiyanhuo = (TextView) findViewById(R.id.yiyanhuo);
		yiyanhuo.setText("已验货");
		listview = (ListView) findViewById(R.id.contentlistview);
		shangjia = (TextView) findViewById(R.id.shangjia);
		name = (TextView) findViewById(R.id.name);
		imageView = (Button) findViewById(R.id.imgcomit);
		date = (TextView) findViewById(R.id.date);
		finish = (ImageView) findViewById(R.id.finish);
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setIcon(R.drawable.logo);
		dialog.setTitle("正在提交请稍等");
		event();
		initData();
		select();// 初始化要提交的数据

	}

	private void initData() {
		No = getIntent().getStringExtra("No");
		Date = getIntent().getStringExtra("Date");
		cSupplier = getIntent().getStringExtra("cSupplier");
		shangjia.setText("商家：  " + cSupplier);
		date.setText("采购日期：  " + Date);
		System.out.println("商家编号" + No);
		Cursor cursor = Select.wh_StockVerifyDetail(No, Date,
				getApplicationContext());
		if (cursor.getCount() == 0) {//
			Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_LONG)
					.show();
		} else {
			initlistview(cursor);

		}
	}

	private void event() {
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (offsetX == 0 && offsetX == 0) {
					if (IsNetConnected.isNetConnected(getApplicationContext())) {
						initDialog();
					} else {
						Toast.makeText(getApplicationContext(), "没有网",
								Toast.LENGTH_SHORT).show();
					}

				}
			}
		});
		imageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = x;
					lastY = y;
					break;
				case MotionEvent.ACTION_MOVE:
					offsetX = x - lastX;
					offsetY = y - lastY;
					// Log.i("tag", "距离自身控件x" + x);
					// Log.i("tag", "距离自身控件y" + y);
					// Log.i("tag", "偏移量x" + offsetX);
					// Log.i("tag", "偏移量y" + offsetY);
					v.offsetLeftAndRight(offsetX);
					v.offsetTopAndBottom(offsetY);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});

	}

	class myAsy extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			SQLiteDatabase db = GetMyDb
					.getSQLiteDatabase(getApplicationContext());
			long rowid = db.delete("wh_StockVerifyDetail",
					"cSupplierNo=? and dDate=?", new String[] { No, Date });
			if (rowid > 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				initlistview(null);
				Toast.makeText(getApplicationContext(), "更新完成",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			dialog.hide();
			super.onPostExecute(result);

		}
	}

	protected void initDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(R.drawable.logo);
		builder1.setTitle("提示:请谨慎操作");
		builder1.setMessage("你确定要提交吗");
		builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog1, int whichButton) {
				if (list != null){
					updalo();
				}
					
			}
		});
		builder1.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder1.show();

	}

	protected void select() {
		list = new ArrayList<wh_StockVerifyDetail>();
		final Cursor cursor = Select.selectWH_StockDetail(No, Date,
				getApplicationContext());
		while (cursor.moveToNext()) {
			String dDate = cursor.getString(cursor.getColumnIndex("dDate"));
			String cSupplier = cursor.getString(cursor
					.getColumnIndex("cSupplier"));
			String cSupplierNo = cursor.getString(cursor
					.getColumnIndex("cSupplierNo"));
			cOperator = cursor.getString(cursor.getColumnIndex("cOperator"));
			String cOperatorNo = cursor.getString(cursor
					.getColumnIndex("cOperatorNo"));
			String cWhNo = cursor.getString(cursor.getColumnIndex("cWhNo"));
			String cWh = cursor.getString(cursor.getColumnIndex("cWh"));
			String cGoodsNo = cursor.getString(cursor
					.getColumnIndex("cGoodsNo"));
			String cGoodsName = cursor.getString(cursor
					.getColumnIndex("cGoodsName"));
			String cBarcode = cursor.getString(cursor
					.getColumnIndex("cBarcode"));
			String fQuantity = cursor.getString(cursor
					.getColumnIndex("fQuantity"));
			String fInPrice = cursor.getString(cursor
					.getColumnIndex("fInPrice"));
			String fInMoney = cursor.getString(cursor
					.getColumnIndex("fInMoney"));
			String fQty_Cur = cursor.getString(cursor
					.getColumnIndex("fQty_Cur"));
			System.out.println("查出的的" + fQty_Cur);
			list.add(new wh_StockVerifyDetail(dDate, cSupplier, cSupplierNo,
					cOperator, cOperatorNo, cWhNo, cWh, cGoodsNo, cGoodsName,
					cBarcode, fQuantity, fInPrice, fInMoney, fQty_Cur));
		}
		name.setText("采购员：" + cOperator);
	}

	public void add() {
		for (int i = 0; i < list.size(); i++) {

		}
	}

	public void updalo() {
		dialog.show();
		final String str = JSON.toJSONString(list);
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", str);

		httpUtils.send(HttpMethod.POST, Config.commitxamineGoods, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println(arg1);
						System.out.println("失败" + arg0);
					
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						System.out.println("成功" + arg0.result);
						try {
							JSONObject obj=new JSONObject(arg0.result);
							String resultStatus=obj.getString("resultStatus");
							if(resultStatus.equals("1")){
								new myAsy().execute();
							}
							else{
								Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void initlistview(Cursor cursor) {
		adapter = new SimpleCursorAdapter(this, R.layout.listitems, null,
				new String[] { "_id", "cBarcode", "cGoodsName", "fQuantity",
						"fInPrice", "fInMoney", "fQty_Cur" }, new int[] {
						R.id.text1, R.id.text2, R.id.text3, R.id.text4,
						R.id.text5, R.id.text6, R.id.text7 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		if (cursor != null) {
			adapter.swapCursor(cursor);
		}
		adapter.notifyDataSetChanged();
		listview.setAdapter(adapter);
	}

}
