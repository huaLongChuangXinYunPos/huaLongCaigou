package com.zhaoq.fragment;
import java.util.ArrayList;
import java.util.List;
import com.zhaoq.purchase.R;
import com.example.qr_codescan.MipcaActivityCapture;
import com.zhaoq.adapter.Checkadapter;
import com.zhaoq.bean.t_CheckTast;
import com.zhaoq.bean.t_CheckTast_zone;
import com.zhaoq.bean.wh_CheckWhDetail;

import DB.GetMyDb;
import DB.Select;
import Tool.StringUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CheckFragment extends Fragment {
	private ListView listview;
	private SQLiteDatabase db;
	private Button button, ccommit, cancle, selectgood;
	private Spinner Spinnert_CheckTast, Spinnert_CheckTast_zone;
	private List<t_CheckTast> listt_CheckTast;
	private List<t_CheckTast_zone> listt_CheckTast_zone;
	private wh_CheckWhDetail checkWhDetail;
	private ArrayAdapter<t_CheckTast> t_CheckTastadapter;
	private ArrayAdapter<t_CheckTast_zone> listt_CheckTast_zoneadapter;
	private TextView goodname, particular;
	private EditText ecode, goodsnum;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private ProgressDialog dialog;
	private Checkadapter adapter;
	private String cGoodsNo, cGoodsName, renwu, cCheckTaskNo, cZoneNo;
	AlertDialog.Builder builder;
	private ImageView finish;
	private int listviewPosition;

	public static Fragment getFragment(String userno, String name) {
		Fragment f = new CheckFragment();
		Bundle bundle = new Bundle();
		bundle.putString("id", userno);
		bundle.putString("name", name);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		initData();
		initListview();
		event();
		return view;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
	}

	private View initView() {
		View view = LinearLayout.inflate(getActivity(), R.layout.check, null);
		button = (Button) view.findViewById(R.id.cpurchasescan);
		ccommit = (Button) view.findViewById(R.id.ccommit);
		cancle = (Button) view.findViewById(R.id.ccancle);
		particular = (TextView) view.findViewById(R.id.particular);
		finish = (ImageView) view.findViewById(R.id.finish);
		dialog = new ProgressDialog(getActivity());
		dialog.setCancelable(false);
		dialog.setIcon(R.drawable.logo);
		dialog.setTitle("正在操作请稍等");
		ecode = (EditText) view.findViewById(R.id.ecode);
		goodsnum = (EditText) view.findViewById(R.id.goodsnum);
		selectgood = (Button) view.findViewById(R.id.cselect);
		listview = (ListView) view.findViewById(R.id.purchaselistview);
		Spinnert_CheckTast = (Spinner) view.findViewById(R.id.task);
		Spinnert_CheckTast_zone = (Spinner) view.findViewById(R.id.childmenu);
		goodname = (TextView) view.findViewById(R.id.goodname);
		initdialog();
		registerForContextMenu(listview);
		return view;
	}

	private void initdialog() {
		builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("提示:请谨慎操作");
		builder.setMessage("更新数据库需要一些时间");

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.lstviewmenu, menu);
		menu.setHeaderIcon(R.drawable.ic_launcher);
		menu.setHeaderTitle("哈哈");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		final int position = menuInfo.position;//
		System.out.println(position);
		if (id == R.id.delete) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					getActivity());
			builder1.setIcon(R.drawable.ic_launcher);
			builder1.setTitle("提示:请谨慎操作");
			builder1.setMessage("你确定要删除吗");
			builder1.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog1,
								int whichButton) {
							adapter.reMove(position, db);
						}
					});
			builder1.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			builder1.show();
		}
		return super.onContextItemSelected(item);
	}

	private void event() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setSelected(true);
				listviewPosition = position;
				selectList(position);
				ccommit.setEnabled(true);
				
			}
		});

		ccommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cGoodsNo = checkWhDetail.getcGoodsNo(); // 商品编号
				String cGoodsName = checkWhDetail.getcGoodsName(); // 商品名称
				String cBarcode = checkWhDetail.getcBarcode(); // 商品条码
				String fNormalPrice = checkWhDetail.getfNormalPrice();// 商品售价
				String fQuantity = goodsnum.getText().toString().trim(); // 商数量品
				String cCheckTaskNo = checkWhDetail.getcCheckTaskNo();
				String cZoneNo = checkWhDetail.getcZoneNo();
				String cOperatorNo=checkWhDetail.getcOperatorNo();
				String cOperator=checkWhDetail.getcOperator();
				if (StringUtils.isInteger(fQuantity)) {
					checkWhDetail.setfQuantity(fQuantity); //
					// 商品编号：cGoodsNo varchar(32),
					// 商品名称：cGoodsName varchar(64),
					// 盘点数量：fQuantity money
					// 商品条码：cBarcode text,
					// 商品售价：fNormalPrice text
					Cursor cursor = db
							.rawQuery(
									"select * from wh_CheckWhDetail  where  cGoodsNo=?",
									new String[] { cGoodsNo});
					if (cursor.getCount() == 0) {// 如果以前没有这个商品就插入
						db.execSQL(
								"insert into wh_CheckWhDetail values(?,?,?,?,?,?,?,?,?,?)",
								new Object[] { null, cGoodsNo, cGoodsName,
										fQuantity, cBarcode, fNormalPrice,
										cCheckTaskNo, cZoneNo,cOperatorNo,cOperator});
						adapter.setData(checkWhDetail);
						Toast.makeText(getActivity(), "插入成功",
								Toast.LENGTH_SHORT).show();
					} else {// 如果有这个商品就更新
						ContentValues values = new ContentValues();
						values.put("fQuantity", fQuantity);
						int id = db.update("wh_CheckWhDetail", values,
								"cGoodsNo=?", new String[] {
										cGoodsNo});
						if (id > 0)
							Toast.makeText(getActivity(), "更新完成",
									Toast.LENGTH_SHORT).show();
						else {
							Toast.makeText(getActivity(), "更新失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					adapter.notifyDataSetChanged();
					ccommit.setEnabled(false);
					selectgood.setText("点击查询");
					ecode.setText("");
					goodsnum.setText("");
					listview.setSelection(Checkadapter.list.size()-1);

				} else {
					Toast.makeText(getActivity(), "请正确输入", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Spinnert_CheckTast_zone.setEnabled(true);
				// Spinnert_CheckTast_zone.setEnabled(true);
				ecode.setText("");
			}
		});
		selectgood.setOnClickListener(new OnClickListener() { // 从输入的条形码查询商品

					@Override
					public void onClick(View v) {
						String cBarcode = ecode.getText().toString().trim();
						if (StringUtils.isInteger(cBarcode)) {

							new MyAsySeGoood().execute(cBarcode);// 根据条形码查询商品
							InputMethodManager imm = (InputMethodManager) getActivity()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(ecode.getWindowToken(),
									0);

						} else {
							goodname.setText("品名:");
							Toast.makeText(getActivity(), "请正确输入",
									Toast.LENGTH_LONG).show();

						}
					}
				});
		button.setOnClickListener(new OnClickListener() { // 扫描

			@Override
			public void onClick(View v) {
			
				Intent intent = new Intent();
				intent.setClass(getActivity(), MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

			}
		});
		Spinnert_CheckTast
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View view,
							int position, long id) {
						// loposition = position;
						renwu = listt_CheckTast.get(position).getcCheckTask();
						cCheckTaskNo = listt_CheckTast.get(position)
								.getcCheckTaskNo();
						System.out.println(cCheckTaskNo);
						new MyAsySelectZone().execute(cCheckTaskNo);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		Spinnert_CheckTast_zone
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View view,
							int position, long arg3) {
						cZoneNo = listt_CheckTast_zone.get(position)
								.getcZoneNo();
						particular.setText("任务："
								+ renwu
								+ "~子单: "
								+ listt_CheckTast_zone.get(position)
										.getcZoneName());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();

			}
		});
	}

	protected void selectList(int posn) {
		checkWhDetail = Checkadapter.list.get(posn);
		String name = checkWhDetail.getcGoodsName();
	
		String num = checkWhDetail.getfQuantity();
	
		if (num != null) {
			goodsnum.setText("" + num);
		} else {
			goodsnum.setText("");
		}
		goodname.setText("品名：" + name);
		
		
	}

	public void initListview() {
		adapter = new Checkadapter(getActivity());
		listview.setAdapter(adapter);
		Spinnert_CheckTast_zone.setEnabled(true);
		Spinnert_CheckTast_zone.setSelection(0);
		Spinnert_CheckTast_zone.setSelection(0);
		Spinnert_CheckTast_zone.setEnabled(true);

	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == getActivity().RESULT_OK) {
				Bundle bundle = data.getExtras();

				ecode.setText(bundle.getString("result"));
				System.out.println(bundle.getString("result"));
				// select(bundle.getString("result"));
				new MyAsySeGoood().execute(bundle.getString("result"));// 根据条形码查询商品
			}
			break;
		}
	}
	
	private boolean select(String code) {// 根据条形码查商品
		Cursor cursor = db.rawQuery("select * from t_Goods where cBarcode=? or cGoodsNo=?",
				new String[] { code,code });

		cGoodsName = null; // 商品名称
		// String cUnit; // 商品单位
		// String cSpec; // 商品规格
		// String fNormalPrice = null; // 商品售价
		String cBarcode=null;
		String fNormalPrice = null; // 最新进价
		if (cursor.getCount() == 0) {// 没有查到
			return false;
		} else {
			while (cursor.moveToNext()) {
				cGoodsNo = cursor.getString(cursor.getColumnIndex("cGoodsNo"));
				cGoodsName = cursor.getString(cursor
						.getColumnIndex("cGoodsName"));

				fNormalPrice = cursor.getString(cursor
						.getColumnIndex("fNormalPrice"));
				cBarcode = cursor.getString(cursor
						.getColumnIndex("cBarcode"));
			}
			cursor.close();
			checkWhDetail = new wh_CheckWhDetail();

			checkWhDetail.setcGoodsNo(cGoodsNo); // 商品编码
			checkWhDetail.setcGoodsName(cGoodsName); // 商品名称
			checkWhDetail.setcBarcode(cBarcode); // 商品条码
			checkWhDetail.setfNormalPrice(fNormalPrice);
			checkWhDetail.setcCheckTaskNo(cCheckTaskNo);
			checkWhDetail.setcZoneNo(cZoneNo);
			checkWhDetail.setcOperator(getArguments().getString("name"));// 采购员姓名
			checkWhDetail.setcOperatorNo(getArguments().getString("id"));// 采购员编号
			return true;
		}
	}

	private void initData() {
		db = GetMyDb.getSQLiteDatabase(getActivity());
		if (!Select.select("select * from t_CheckTast", db)
				|| !Select.select("select * from t_CheckTast_zone", db)) { // 如果有数据就
			Toast.makeText(getActivity(), "请更新任务表和子单数据库", Toast.LENGTH_SHORT)
					.show();
		} else {
			initSpinner();
		}
	}

	class MyAsySelectZone extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String no = params[0];
			listt_CheckTast_zone = new ArrayList<t_CheckTast_zone>();
			if (Select.select("select * from t_CheckTast_zone", db)) {
				Cursor cursor = db.rawQuery(
						"select * from t_CheckTast_zone where cCheckTaskNo=?",
						new String[] { no });
				while (cursor.moveToNext()) {
					String cZoneNo = cursor.getString(cursor
							.getColumnIndex("cZoneNo"));
					String cZoneName = cursor.getString(cursor
							.getColumnIndex("cZoneName"));
					listt_CheckTast_zone.add(new t_CheckTast_zone(cZoneNo,
							cZoneName));
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				if (listt_CheckTast_zone.size() > 0)
					listt_CheckTast_zoneadapter = new ArrayAdapter<t_CheckTast_zone>(
							getActivity(),
							R.layout.textxml,R.id.textxml, 
							listt_CheckTast_zone);
				Spinnert_CheckTast_zone.setAdapter(listt_CheckTast_zoneadapter);
				dialog.hide();
			}
			super.onPostExecute(result);
		}
	}

	private void initSpinner() {
		if (listt_CheckTast == null) {
			listt_CheckTast = new ArrayList<t_CheckTast>();
		} else {
			listt_CheckTast.clear();
		}
		Cursor cursor = db.rawQuery("select * from t_CheckTast", null);
		while (cursor.moveToNext()) {
			String cSupNo = cursor.getString(cursor
					.getColumnIndex("cCheckTaskNo"));
			String cSupName = cursor.getString(cursor
					.getColumnIndex("cCheckTask"));
			String dCheckTask = cursor.getString(cursor
					.getColumnIndex("dCheckTask"));
			listt_CheckTast.add(new t_CheckTast(cSupNo, cSupName, dCheckTask));
		}
		cursor.close();
		t_CheckTastadapter = new ArrayAdapter<t_CheckTast>(getActivity(),
				R.layout.textxml,R.id.textxml,listt_CheckTast);
		Spinnert_CheckTast.setAdapter(t_CheckTastadapter);
	}

	class MyAsySeGoood extends AsyncTask<String, Void, Boolean> { // 查询商品
		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// 根据商品条形码查询商品信息
			return select(params[0]);
			// spinner.setVisibility(View.VISIBLE);

		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.hide();
			if (result == true) {
				ccommit.setEnabled(true);
				goodname.setText("商品名:" + cGoodsName);
				ecode.setText("");
				goodsnum.setFocusable(true);

			} else {
				
				Toast.makeText(getActivity(), "没有这个商品", Toast.LENGTH_LONG)
						.show();
				goodname.setText("商品名:" + "");
				goodsnum.setText("");
				ecode.setText("");
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
