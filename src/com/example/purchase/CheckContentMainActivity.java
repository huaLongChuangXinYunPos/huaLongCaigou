package com.example.purchase;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhaoq.bean.wh_CheckWhDetail;
import com.zhaoq.purchase.R;

import DB.GetMyDb;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckContentMainActivity extends Activity {

	private ListView listview;
	SimpleCursorAdapter adapter;
	private TextView tijiao;
	private ImageView fin;
	String cZoneNo, cCheckTaskNo;
	private List<wh_CheckWhDetail> list;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_content_main);
		listview = (ListView) findViewById(R.id.checklistview);
		tijiao = (TextView) findViewById(R.id.tijiao);
		fin = (ImageView) findViewById(R.id.finish);
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setIcon(R.drawable.logo);
		dialog.setTitle("正在提交请稍等");
		initData();
		event();
	}

	private void event() {
		tijiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IsNetConnected.isNetConnected(getApplicationContext()))
					initDialog();
				else {
					Toast.makeText(getApplicationContext(), "没有网络",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		fin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	protected void initDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(R.drawable.logo);
		builder1.setTitle("提示:请谨慎操作");
		builder1.setMessage("你确定要提交吗");
		builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog1, int whichButton) {
				if (list != null)
					updalo();
					
			}
		});
		builder1.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder1.show();
	}

	class myAsy extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			SQLiteDatabase db = GetMyDb
					.getSQLiteDatabase(getApplicationContext());
			long rowid = db.delete("wh_CheckWhDetail",
					"cZoneNo=? and cCheckTaskNo=?", new String[] { cZoneNo,
							cCheckTaskNo });
			if (rowid > 0) {
				return true;
			} else {
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result){
				initlistview(null);
				Toast.makeText(getApplicationContext(), "更新完成",
						Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			dialog.hide();
			super.onPostExecute(result);

		}
	}

	public void updalo() {
		dialog.show();
		final String str = JSON.toJSONString(list);
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", str);
		System.out.println("盘点" + str);
		httpUtils.send(HttpMethod.POST, Config.commitcheck, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

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
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	private void initData() {
		cZoneNo = getIntent().getStringExtra("cZoneNo");
		cCheckTaskNo = getIntent().getStringExtra("cCheckTaskNo");
		System.out.println("任务编号" + cZoneNo);
		System.out.println("子单编号" + cCheckTaskNo);
		SQLiteDatabase db = GetMyDb.getSQLiteDatabase(this);
		Cursor cursor = db
				.rawQuery(
						"select * from wh_CheckWhDetail where cZoneNo=? and cCheckTaskNo=?",
						new String[] { cZoneNo, cCheckTaskNo });
		if (cursor.getCount() == 0) {//
			Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_LONG)
					.show();
		} else {
			initlistview(cursor);
		}
	}

	private void initlistview(Cursor cursor) {
		list = new ArrayList<wh_CheckWhDetail>();
		if (cursor != null)
			while (cursor.moveToNext()) {
				String cGoodsNo = cursor.getString(cursor
						.getColumnIndex("cGoodsNo"));
				String cGoodsName = cursor.getString(cursor
						.getColumnIndex("cGoodsName"));
				String cBarcode = cursor.getString(cursor
						.getColumnIndex("cBarcode"));
				String fQuantity = cursor.getString(cursor
						.getColumnIndex("fQuantity"));
				String fNormalPrice = cursor.getString(cursor
						.getColumnIndex("fNormalPrice"));
				String cCheckTaskNo = cursor.getString(cursor
						.getColumnIndex("cCheckTaskNo"));
				String cZoneNo = cursor.getString(cursor
						.getColumnIndex("cZoneNo"));
				String cOperatorNo = cursor.getString(cursor
						.getColumnIndex("cOperatorNo"));
				String cOperator = cursor.getString(cursor
						.getColumnIndex("cOperator"));
				list.add(new wh_CheckWhDetail(cGoodsNo, cGoodsName, cBarcode,
						fQuantity, fNormalPrice, cCheckTaskNo, cZoneNo,
						cOperatorNo, cOperator));
			}
		adapter = new SimpleCursorAdapter(this, R.layout.checkadapteritem,
				null, new String[] { "cGoodsNo", "cGoodsName", "cBarcode",
						"fQuantity", "fNormalPrice" }, new int[] { R.id.text1,
						R.id.text2, R.id.text3, R.id.text4, R.id.text5 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		if (cursor != null) {
			adapter.swapCursor(cursor);
		}
		adapter.notifyDataSetChanged();
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		list = null;
		adapter = null;
		listview = null;
		super.onDestroy();
	}

}
