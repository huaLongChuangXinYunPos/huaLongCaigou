package com.zhaoq.purchase;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import DB.GetMyDb;
import Tool.Config;
import Tool.VoleyToll;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zhaoq.purchase.R;
import com.example.purchase.SelectMainActivity;
import com.zhaoq.bean.t_Pass;

/**
 * com.zhaoq.purchase
 * @Email zhaoq_hero@163.com
 * @author zhaoQiang : 2016-3-8
 */
public class LoginActivity extends Activity implements OnClickListener {

	private EditText editname;
	private EditText editpass;
	private Button login, update;
	private List<t_Pass> list;
	private t_Pass t_pass;
	private ProgressDialog dialog;
	private ImageView finish;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		editname = (EditText) findViewById(R.id.editname);
		editpass = (EditText) findViewById(R.id.editpass);
		finish = (ImageView) findViewById(R.id.finish);
		login = (Button) findViewById(R.id.login);
		update = (Button) findViewById(R.id.update);
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setTitle("正在操作请稍等");
		dialog.setIcon(R.drawable.logo);
		login.setOnClickListener(this);
		update.setOnClickListener(this);
		db = GetMyDb.getSQLiteDatabase(this);
		finish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login:
			String user = editname.getText().toString().trim();
			String pass = editpass.getText().toString().trim();
			String name = null;
			if (!user.isEmpty() && !pass.isEmpty()) {

				Cursor cursor = db.rawQuery(
						"select * from t_Pass where User=?and Pass=?",
						new String[] { user, pass });
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						name = cursor.getString(cursor.getColumnIndex("Name"));
					}
					Intent intent = new Intent(LoginActivity.this,
							SelectMainActivity.class);
					intent.putExtra("id", user);
					intent.putExtra("name", name);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "登陆失败请正确输入",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "登陆失败请正确输入",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.update:
			update();
			break;
		case R.id.finish:
			finish();
			break;
		default:
			break;
		}
	}

	class MyUpdateAsy extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			dialog.setTitle("正在插入数据库");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			db.execSQL("DROP TABLE t_Pass");
			db.execSQL("create table t_Pass(_id Integer PRIMARY KEY AutoIncrement,User  text,Name text,Pass text)");
			db.beginTransaction();// 开启事务
			if (list != null)
				try {
					for (int i = 0; i < list.size(); i++) {
						String User = list.get(i).getUser();
						String Name = list.get(i).getName();
						String Pass = list.get(i).getPass();
						db.execSQL("insert into t_Pass values(?,?,?,?)",new Object[] { null, User, Name, Pass });
					}
					db.setTransactionSuccessful();// 设置事务的标志为True
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					db.endTransaction();// 结束事务,有两种情况：commit,rollback,
				}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.hide();
			Toast.makeText(getApplicationContext(), "更新完成", Toast.LENGTH_SHORT)
					.show();
			super.onPostExecute(result);
		}

	}

	protected void update() {
		dialog.show();
		dialog.setTitle("正在下载数据");
		list = new ArrayList<t_Pass>();
		StringRequest request = new StringRequest(Config.Updateuser,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject obj = new JSONObject(s);
							String resultStatus = obj.getString("resultStatus");
							if (resultStatus.equals("0")) {
								Toast.makeText(getApplicationContext(), "更新失败",
										Toast.LENGTH_LONG).show();
							} else {
								JSONArray array = obj.getJSONArray("data");
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj1 = array.getJSONObject(i);
									String Name = obj1.getString("Name");
									String Pass = obj1.getString("Pass");
									String User = obj1.getString("User");
									t_pass = new t_Pass();
									t_pass.setName(Name);
									t_pass.setPass(Pass);
									t_pass.setUser(User);
									list.add(t_pass);
								}
								new MyUpdateAsy().execute();
							}
						} catch (Exception e) {
							e.printStackTrace();
							dialog.hide();
							Toast.makeText(getApplicationContext(), "更新失败",
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						dialog.hide();
						Toast.makeText(getApplicationContext(), "更新失败",
								Toast.LENGTH_LONG).show();
					}
				});

		VoleyToll.getRequestQueue(getApplicationContext()).add(request);
	}
	@Override
	protected void onDestroy() {
		dialog.cancel();
		dialog=null;
		super.onDestroy();
	}

}
