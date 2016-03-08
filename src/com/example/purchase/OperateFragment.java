package com.example.purchase;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zhaoq.purchase.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhaoq.bean.t_CheckTast;
import com.zhaoq.bean.t_CheckTast_zone;
import com.zhaoq.bean.t_Goods;
import com.zhaoq.bean.t_Supplier;
import com.zhaoq.bean.t_WareHouse;

import DB.GetMyDb;
import DB.Mysqlite;
import Tool.Config;
import Tool.IsNetConnected;
import Tool.VoleyToll;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class OperateFragment extends Fragment implements OnClickListener {
	@ViewInject(R.id.updategoods)
	private Button updategoods;

	@ViewInject(R.id.updatesupplier)
	private Button updatesupplier;

	@ViewInject(R.id.updat_CheckTast)
	private Button updat_CheckTast;

	@ViewInject(R.id.updatet_CheckTast_zone)
	private Button updat_CheckTastButton;

	@ViewInject(R.id.updatet_WareHouse)
	private Button updatet_WareHouse;

	@ViewInject(R.id.opfinish)
	private ImageView finish1;

	private String DROP, create;
	private AlertDialog.Builder builder;
	private SQLiteDatabase db;
	private ProgressDialog dialog;
	private List<t_Goods> t_Goodslist;
	private List<t_Supplier> listt_Supplier;
	private List<t_CheckTast> listt_CheckTast;
	private List<t_CheckTast_zone> listt_CheckTast_zone;
	private List<t_WareHouse> listt_WareHouse;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.operatefragment, null);
		ViewUtils.inject(this, view);
		updategoods.setOnClickListener(this);
		updatesupplier.setOnClickListener(this);
		updat_CheckTast.setOnClickListener(this);
		updat_CheckTastButton.setOnClickListener(this);
		updatet_WareHouse.setOnClickListener(this);
		finish1.setOnClickListener(this);
		dialog = new ProgressDialog(getActivity());
		dialog.setIcon(R.drawable.logo);//
		// 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
		dialog.setTitle("提示");
		dialog.setCancelable(false);
		db = GetMyDb.getSQLiteDatabase(getActivity());
		initdialog();
		return view;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.opfinish:
			getActivity().finish();
			break;
		case R.id.updategoods:
			DROP = "DROP TABLE t_Goods";
			create = Mysqlite.t_Goods;
			initDialog("goods");
			break;
		case R.id.updatesupplier:
			DROP = "DROP TABLE t_Supplier";
			create = Mysqlite.t_Supplier;
			initDialog("supplier");
			break;
		case R.id.updat_CheckTast:
			DROP = "DROP TABLE t_CheckTast";
			create = Mysqlite.t_CheckTast;
			initDialog("t_CheckTast");
			break;
		case R.id.updatet_CheckTast_zone:
			DROP = "DROP TABLE t_CheckTast_zone";
			create = Mysqlite.t_CheckTast_zone;
			initDialog("t_CheckTast_zone");
			break;
		case R.id.updatet_WareHouse:
			DROP = "DROP TABLE t_WareHouse";
			create = Mysqlite.t_WareHouse;
			initDialog("t_WareHouse");
			break;

		default:

			break;
		}

	}

	private void initdialog() {
		builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.logo);
		builder.setTitle("提示:请谨慎操作");
		builder.setMessage("更新数据库需要一些时间");

	}

	private void initIntentlistt_CheckTast_zone() {
		dialog.show();
		dialog.setTitle("正在从网上下载子单信息");
		dialog.setMessage("请稍等");
		StringRequest request = new StringRequest(Config.t_CheckTast_zone,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject obj = new JSONObject(s);
							String resultStatus = obj.getString("resultStatus");
							if (resultStatus.equals("0")) {
								Toast.makeText(getActivity(), "获取网络数据失败",
										Toast.LENGTH_SHORT).show();
								dialog.hide();
								return;
							}
							JSONArray array = obj.getJSONArray("data");
							Gson gson = new Gson();
							listt_CheckTast_zone = gson.fromJson(
									array.toString(),
									new TypeToken<List<t_CheckTast_zone>>() {
									}.getType());

							new MyAsyInsertZone().execute();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						dialog.hide();
						Toast.makeText(getActivity(), "请求失败",
								Toast.LENGTH_SHORT).show();

					}
				});
		VoleyToll.getRequestQueue(getActivity()).add(request);
	}

	private boolean insertt_CheckTast_zone(
			List<t_CheckTast_zone> listt_CheckTast_zone) {
		db.beginTransaction();// 开启事务
		try {
			db.execSQL(DROP);
			db.execSQL(create);
			for (int i = 0; i < listt_CheckTast_zone.size(); i++) {
				t_CheckTast_zone a = listt_CheckTast_zone.get(i);
				db.execSQL(
						"insert into t_CheckTast_zone values(?,?,?,?,?)",
						new Object[] { null, a.getcCheckTaskNo(),
								a.getcCheckTask(), a.getcZoneNo(),
								a.getcZoneName() });
			}
			db.setTransactionSuccessful();// 设置事务的标志为True
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			db.endTransaction();// 结束事务,有两种情况：commit,rollback,
		}

	}

	class MyAsyInsertZone extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			dialog.setTitle("正在插入数据");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			insertt_CheckTast_zone(listt_CheckTast_zone);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(getActivity(), "插入完成", Toast.LENGTH_SHORT).show();
			dialog.hide();
			super.onPostExecute(result);
		}

	}

	protected void initDialog(final String is) {

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog1, int whichButton) {
				if (IsNetConnected.isNetConnected(getActivity())) {
					if (is.equals("goods")) {
						initIntentt_Goods();
					} else if (is.equals("supplier")) {
						initIntentt_Supplier();
					} else if (is.equals("t_CheckTast")) {
						initIntent_check();
					} else if (is.equals("t_CheckTast_zone")) {
						initIntentlistt_CheckTast_zone();
					} else if (is.equals("t_WareHouse")) {
						initIntent_WareHose();
					}

				} else {
					Toast toast = Toast.makeText(getActivity(), "没有网络",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();
	}

	protected void initIntent_WareHose() {
		dialog.show();
		dialog.setTitle("正在更新仓库");
		dialog.setMessage("请稍等");
		StringRequest request = new StringRequest(Config.t_WareHouse,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						System.out.println(s);
						Gson gson = new Gson();
						JSONObject obj;
						try {
							obj = new JSONObject(s);
							JSONArray array = obj.getJSONArray("data");
							listt_WareHouse = gson.fromJson(array.toString(),
									new TypeToken<List<t_WareHouse>>() {
									}.getType());
							db.beginTransaction();// 开启事务
							try {
								db.execSQL(DROP);
								db.execSQL(create);
								for (int i = 0; i < listt_WareHouse.size(); i++) {
									db.execSQL(
											"insert into t_WareHouse values(?,?,?)",
											new Object[] {
													null,
													listt_WareHouse.get(i)
															.getcWhNo(),
													listt_WareHouse.get(i)
															.getcWh(), });
								}
								Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
								db.setTransactionSuccessful();// 设置事务的标志为True

							} catch (Exception e) {
						
								Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
							} finally {
								dialog.hide();
								db.endTransaction();// 结束事务,有两种情况：commit,rollback,
							}

						} catch (JSONException e) {
							dialog.hide();
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						dialog.hide();
						Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
					}
				});
		VoleyToll.getRequestQueue(getActivity()).add(request);

	}

	private void initIntent_check() {
		dialog.show();
		dialog.setTitle("正在请求网络数据");
		listt_CheckTast = new ArrayList<t_CheckTast>();
		StringRequest request = new StringRequest(Config.t_CheckTast,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Log.i("tag1", s);
						// resultStatus
						// {"resultStatus":1,"data":[{"cCheckTask":"生鲜盘点","cCheckTaskNo":"8001","dCheckTask":"\/Date(1453132800000)\/"}]}
						try {
							JSONObject obj = new JSONObject(s);
							String str = obj.getString("resultStatus");
							if (str.equals("1")) {
								JSONArray array = obj.getJSONArray("data");
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj1 = array.getJSONObject(i);
									String cCheckTask = obj1
											.getString("cCheckTask");
									String cCheckTaskNo = obj1
											.getString("cCheckTaskNo");
									String dCheckTask = obj1
											.getString("dCheckTask");
									listt_CheckTast.add(new t_CheckTast(
											cCheckTaskNo, cCheckTask,
											dCheckTask));
								}
								insertt_CheckTast(listt_CheckTast);
							} else {
								dialog.hide();
								Toast.makeText(getActivity(), "从网络获取数据异常",
										Toast.LENGTH_SHORT).show();
								return;
							}

						} catch (Exception e) {
							dialog.hide();
							Toast.makeText(getActivity(), "请求失败",
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						dialog.hide();
						Toast.makeText(getActivity(), "请求失败",
								Toast.LENGTH_SHORT).show();
					}
				});
		VoleyToll.getRequestQueue(getActivity()).add(request);
	}

	private void insertt_CheckTast(List<t_CheckTast> listcheck) {
		// 插入数据
		dialog.setTitle("正在插入数据");
		db.beginTransaction();// 开启事务
		try {
			db.execSQL(DROP);
			db.execSQL(create);
			for (int i = 0; i < listcheck.size(); i++) {
				db.execSQL("insert into t_CheckTast values(?,?,?,?)",
						new Object[] { null,
								listcheck.get(i).getcCheckTaskNo(),
								listcheck.get(i).getcCheckTask(),
								listcheck.get(i).getdCheckTask() });
			}
			Toast.makeText(getActivity(), "更新完成", Toast.LENGTH_SHORT).show();
			db.setTransactionSuccessful();// 设置事务的标志为True
		} catch (Exception e) {
			Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
		} finally {
			db.endTransaction();// 结束事务,有两种情况：commit,rollback,
			dialog.hide();

		}
	}

	private void initIntentt_Supplier() {
		dialog.show();
		dialog.setTitle("正在从网上下载店家信息");
		StringRequest request = new StringRequest(Config.Supplier,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject obj = new JSONObject(s);
							String resultStatus = obj.getString("resultStatus");
							if (resultStatus.equals("0")) {
								return;
							}
							JSONArray array = obj.getJSONArray("data");
							Gson gson = new Gson();
							listt_Supplier = gson.fromJson(array.toString(),
									new TypeToken<List<t_Supplier>>() {
									}.getType());
							new MyAsySupplier().execute();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						dialog.hide();
						Toast.makeText(getActivity(), "请求失败",
								Toast.LENGTH_SHORT).show();

					}
				});
		VoleyToll.getRequestQueue(getActivity()).add(request);
	}

	private void initIntentt_Goods() {
		dialog.show();
		dialog.setTitle("正在请求网络数据");
		dialog.setMessage("请稍等");
		t_Goodslist = new ArrayList<t_Goods>();
		StringRequest request = new StringRequest(Config.Updategoods,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						new MyParsejsonGoods(s).execute();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						dialog.hide();
						Toast.makeText(getActivity(), "请求失败",
								Toast.LENGTH_SHORT).show();
					}
				});
		VoleyToll.getRequestQueue(getActivity()).add(request);
	}

	class MyParsejsonGoods extends AsyncTask<Void, Void, Boolean> { // 解析商品数据
		String str;

		public MyParsejsonGoods(String str) {
			this.str = str;
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
			dialog.setTitle("正在解析数据");
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			JSONObject obj;
			try {
				obj = new JSONObject(str);
				String resultStatus = obj.getString("resultStatus");
				if (resultStatus.equals("0")) {
					return false;
				} else {
					JSONArray array = obj.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj1 = array.getJSONObject(i);
						String cGoodsNo = obj1.getString("cGoodsNo");
						String cGoodsName = obj1.getString("cGoodsName");
						String cBarcode = obj1.getString("cBarcode");
						String cUnit = obj1.getString("cUnit");
						String cSpec = obj1.getString("cSpec");
						String fNormalPrice = obj1.getString("fNormalPrice");
						String fCKPrice = obj1.getString("fCKPrice");
						String cSupNo = obj1.getString("cSupNo");
						String fQty_Cur = obj1.getString("fQty_Cur");

						System.out.println("得到的" + fQty_Cur);
						t_Goodslist.add(new t_Goods(cGoodsNo, cGoodsName,
								cBarcode, cUnit, cSpec, fNormalPrice, fCKPrice,
								cSupNo, fQty_Cur));
					}
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.hide();
			if (result) {
				Toast.makeText(getActivity(), "解析成功", Toast.LENGTH_SHORT)
						.show();
				new MyAsyInsertgoods().execute();
			} else {
				Toast.makeText(getActivity(), "解析失败", Toast.LENGTH_SHORT)
						.show();

			}
			super.onPostExecute(result);
		}

		class MyAsyInsertgoods extends AsyncTask<Void, Void, Boolean> {

			@Override
			protected void onPreExecute() {
				dialog.show();
				dialog.setTitle("正在插入数据");
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {

				insertt_Goods(t_Goodslist);
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				dialog.hide();
				Toast.makeText(getActivity(), "插入完成", Toast.LENGTH_SHORT)
						.show();
				t_Goodslist = null;
				super.onPostExecute(result);
			}
		}

		private boolean insertt_Goods(List<t_Goods> t_Goodslist) {

			db.beginTransaction();// 开启事务
			try {
				db.execSQL(DROP);
				db.execSQL(create);
				for (int i = 0; i < t_Goodslist.size(); i++) {
					db.execSQL(
							"insert into t_Goods values(?,?,?,?,?,?,?,?,?,?)",
							new Object[] {
									null,
									t_Goodslist.get(i).getcGoodsNo(),
									t_Goodslist.get(i).getcGoodsName(),
									t_Goodslist.get(i).getcBarcode(),
									t_Goodslist.get(i).getcUnit(),
									t_Goodslist.get(i).getcSpec(),
									t_Goodslist.get(i).getfNormalPrice(),// 售价
									t_Goodslist.get(i).getfCkPrice(), // 进价
									t_Goodslist.get(i).getcSupNo(),
									t_Goodslist.get(i).getfQty_Cur() }); //
				}
				db.setTransactionSuccessful();// 设置事务的标志为True
				return true;
			} catch (Exception e) {
				return false;
			} finally {
				db.endTransaction();// 结束事务,有两种情况：commit,rollback,
			}
		}
	}

	class MyAsySupplier extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			dialog.setTitle("正在初始化商家");
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return inserttt_Supplier(listt_Supplier);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.hide();
			listt_Supplier = null;
			if (result) {
				Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

	private boolean inserttt_Supplier(List<t_Supplier> listt_Supplier) {
		// 插入数据
		db.execSQL(DROP);
		db.execSQL(create);
		db.beginTransaction();// 开启事务
		try {
			for (int i = 0; i < listt_Supplier.size(); i++) {
				db.execSQL("insert into t_Supplier values(?,?,?)",
						new Object[] { null, listt_Supplier.get(i).getcSupNo(),
								listt_Supplier.get(i).getcSupName() });
			}
			db.setTransactionSuccessful();// 设置事务的标志为True
			return true;
		} catch (Exception e) {
			System.out.println("异常");
			return false;
		} finally {

			db.endTransaction();// 结束事务,有两种情况：commit,rollback,
		}

	}
	@Override
	public void onDetach() {
		dialog.cancel();
		super.onDetach();
	}

}
