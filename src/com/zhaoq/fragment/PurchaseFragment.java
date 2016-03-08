package com.zhaoq.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import DB.GetMyDb;
import Tool.StringUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoq.purchase.R;
import com.example.qr_codescan.MipcaActivityCapture;
import com.zhaoq.adapter.Purchaseadapter;
import com.zhaoq.bean.WH_StockDetail;
import com.zhaoq.bean.t_Supplier;
import com.zhaoq.bean.t_WareHouse;

public class PurchaseFragment extends Fragment {
	private ListView listview;
	private SQLiteDatabase db;
	private Button button, commit, cancle, selectgood;
	private Spinner Spinnert_Supplier, Spinnerwarehouse;
	private List<t_Supplier> listt_Supplier;
	private List<t_WareHouse> listt_WareHouse;
	private WH_StockDetail wH_StockDetail;
	private ArrayAdapter<t_Supplier> t_SupplierNoadapter;
	private ArrayAdapter<t_WareHouse> t_WareHouseadapter;
	private TextView goodname;
	private CheckBox Batch; // 批采
	private EditText price, ecode, goodsnum;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private ProgressDialog dialog;
	private Purchaseadapter adapter;
	private int loposition;// 记录要选择的商家
	private int warehouseposition;// 仓库位置
	private String cGoodsName, fCKPrice;
	AlertDialog.Builder builder;
	private ImageView finish;
	private TextView kucun;
	String fQty_Cur = null;
	private int listviewPosition;// listview点击的位置

	public static Fragment getFragment(String userno, String name) {
		Fragment f = new PurchaseFragment();
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

	private View initView() {
		View view = LinearLayout
				.inflate(getActivity(), R.layout.purchase, null);
		button = (Button) view.findViewById(R.id.purchasescan);
		commit = (Button) view.findViewById(R.id.commit);
		cancle = (Button) view.findViewById(R.id.cancle);
		finish = (ImageView) view.findViewById(R.id.finish);
		kucun = (TextView) view.findViewById(R.id.kucun);
		Batch = (CheckBox) view.findViewById(R.id.Batch);
		dialog = new ProgressDialog(getActivity());
		dialog.setCancelable(false);
		dialog.setTitle("正在操作请稍等");
		price = (EditText) view.findViewById(R.id.price1);
		ecode = (EditText) view.findViewById(R.id.ecode);
		goodsnum = (EditText) view.findViewById(R.id.goodsnum);
		selectgood = (Button) view.findViewById(R.id.fselect);
		listview = (ListView) view.findViewById(R.id.purchaselistview);
		Spinnert_Supplier = (Spinner) view.findViewById(R.id.spinnerno);
		Spinnerwarehouse = (Spinner) view.findViewById(R.id.warehouse);
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
		menu.setHeaderTitle("操作");
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
		ecode.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					
					String str=ecode.getText().toString();
					new MyAsySeGoood().execute(str);// 根据条形码查询商品
					
					return true;

				}

				return false;
			}
		});
		Batch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					if (loposition != 0 && warehouseposition != 0) {
						initBatch(listt_Supplier.get(loposition).getcSupNo());
						unregisterForContextMenu(listview);// 把listview的长安事件解绑
						Spinnert_Supplier.setEnabled(false);
					}

					else {
						Batch.setChecked(false);
						Toast.makeText(getActivity(), "请选择商家和仓库",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					registerForContextMenu(listview);
					Purchaseadapter.list.clear();
					adapter.notifyDataSetChanged();
					Spinnert_Supplier.setEnabled(true);
				}
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setSelected(true);
				listviewPosition = position;
				selectList(position);
				commit.setEnabled(true);
			}
		});
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();

			}
		});
		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String dDate = wH_StockDetail.getdDate(); // 采购日期：
				String cSupplier = wH_StockDetail.getcSupplier();// 商家名称
				final String cSupplierNo = wH_StockDetail.getcSupplierNo(); // 商家编号
				String cOperator = wH_StockDetail.getcOperator(); // 采购员姓名
				String cOperatorNo = wH_StockDetail.getcOperatorNo(); // 采购员编号
				String cWhNo = wH_StockDetail.getcWhNo(); // 仓库编号
				String cWh = wH_StockDetail.getcWh(); // 仓库名称
				final String cGoodsNo = wH_StockDetail.getcGoodsNo(); // 商品编号
				String cGoodsName = wH_StockDetail.getcGoodsName(); // 商品名称
				String cBarcode = wH_StockDetail.getcBarcode(); // 商品条码

				final String fQuantity = goodsnum.getText().toString().trim(); // 商数量品
				final String fInPrice = price.getText().toString().trim(); // 商品价格
				String fQty_Cur = wH_StockDetail.getfQty_Cur(); // 库存
				if (StringUtils.isInteger(fQuantity)
						&& StringUtils.isDouble(fInPrice)) {
					double fInMoney1 = Integer.parseInt(fQuantity)
							* Double.parseDouble(fInPrice);
					final String fInMoney = ""
							+ StringUtils.isRounding(fInMoney1);// 保留两位
					wH_StockDetail.setfInPrice(fInPrice); // 采购价格
					wH_StockDetail.setfQuantity(fQuantity); // 采购量
					wH_StockDetail.setfInMoney(fInMoney); // 采购金额

					Cursor cursor = db
							.rawQuery(
									"select * from WH_StockDetail where  cGoodsNo=? and cSupplierNo=?",
									new String[] { cGoodsNo, cSupplierNo });
					if (cursor.getCount() == 0) {// 如果以前没有这个商品就插入
						db.execSQL(
								"insert into WH_StockDetail values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new Object[] { null, dDate, cSupplier,
										cSupplierNo, cOperator, cOperatorNo,
										cWhNo, cWh, cGoodsNo, cGoodsName,
										cBarcode, fQuantity, fInPrice,
										fInMoney, fQty_Cur });
						if (!Batch.isChecked()) { // 不选中
							adapter.setData(wH_StockDetail);
						}
						Toast.makeText(getActivity(), "插入成功",
								Toast.LENGTH_SHORT).show();
					} else {// 如果有这个商品就更新
						ContentValues values = new ContentValues();
						values.put("fInPrice", fInPrice);
						values.put("fQuantity", fQuantity);
						values.put("fInMoney", fInMoney);
						int id = db.update("WH_StockDetail", values,
								"cGoodsNo=?and cSupplierNo=?", new String[] {
										cGoodsNo, cSupplierNo });
						if (!Batch.isChecked()) { // 不选中
							adapter.setData(wH_StockDetail);
						}
						if (id > 0)
							Toast.makeText(getActivity(), "更新完成",
									Toast.LENGTH_SHORT).show();
						else {
							Toast.makeText(getActivity(), "更新失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					adapter.notifyDataSetChanged();
					commit.setEnabled(false);
					ecode.setText("");
					goodname.setText("商品名:" + "");
					kucun.setText("库存:" + "");
					price.setText("");
					goodsnum.setText("");
					goodsnum.clearFocus();
					ecode.setSelectAllOnFocus(true);
					ecode.setFocusable(true);

					ecode.setFocusableInTouchMode(true);

					ecode.requestFocus();
				
					if (!Batch.isChecked()) {
						listview.setSelection(Purchaseadapter.list.size() - 1);
					} else {
						listview.setSelection(listviewPosition);
					}

				} else {
					Toast.makeText(getActivity(), "请正确输入", Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Spinnert_Supplier.setEnabled(true);
				ecode.setText("");
				goodname.setText("商品名:" + "");
				kucun.setText("库存:" + "");

			}
		});
		selectgood.setOnClickListener(new OnClickListener() { // 从输入的条形码查询商品

					@Override
					public void onClick(View v) {
						String cBarcode = ecode.getText().toString().trim();// 根据条码或编码
						if (StringUtils.isInteger(cBarcode)) {
							new MyAsySeGoood().execute(cBarcode);// 根据条形码查询商品

						} else {
							Toast.makeText(getActivity(), "请正确输入",
									Toast.LENGTH_LONG).show();
							price.setText("");
							ecode.setText("");
							goodname.setText("商品名:" + "");
							kucun.setText("库存:" + "");
							goodsnum.setText("");
						}
					}
				});
		button.setOnClickListener(new OnClickListener() { // 扫描

			@Override
			public void onClick(View v) {
				goodname.setText("商品名:" + "");
				kucun.setText("库存:" + "");
				Intent intent = new Intent();
				intent.setClass(getActivity(), MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

			}
		});
		Spinnerwarehouse
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						warehouseposition = position;

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		Spinnert_Supplier
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View view,
							int position, long id) {
						loposition = position;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}

	protected void initBatch(String no) {// 根据商家编号查出所有的商品
		if (loposition == 0 || warehouseposition == 0) {
			Toast.makeText(getActivity(), "请正确输入", Toast.LENGTH_SHORT).show();
		} else {
			Cursor cursor = db.rawQuery("select * from t_Goods where cSupNo=?",
					new String[] { no });
			String cGoodsNo = null;// 商品编号
			cGoodsName = null; // 商品名称
			String cBarcode = null;
			fCKPrice = null; // 最新进价
			String cWhNo = listt_WareHouse.get(warehouseposition).getcWhNo(); // 下拉框需要的仓库编号
			String cWh = listt_WareHouse.get(warehouseposition).getcWh(); // 下拉框需要的仓库名称

			if (cursor.getCount() == 0) {// 没有查到
				Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT)
						.show();
			} else {
				while (cursor.moveToNext()) {
					cGoodsNo = cursor.getString(cursor
							.getColumnIndex("cGoodsNo"));
					cGoodsName = cursor.getString(cursor
							.getColumnIndex("cGoodsName"));
					fQty_Cur = cursor.getString(cursor
							.getColumnIndex("fQty_Cur"));
					fCKPrice = cursor.getString(cursor
							.getColumnIndex("fCKPrice"));// 进价
					cBarcode = cursor.getString(cursor
							.getColumnIndex("cBarcode"));
					wH_StockDetail = new WH_StockDetail();
					SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd");
					String date = a.format(new Date());
					wH_StockDetail.setdDate(date); // 采购日期
					wH_StockDetail.setcSupplier(listt_Supplier.get(loposition)
							.getcSupName().toString());// 商家名称
					wH_StockDetail.setcSupplierNo(listt_Supplier
							.get(loposition).getcSupNo().toString());// 商家编号
					wH_StockDetail.setcOperator(getArguments()
							.getString("name"));// 采购员姓名
					wH_StockDetail.setcOperatorNo(getArguments()
							.getString("id"));// 采购员编号
					wH_StockDetail.setcWhNo(cWhNo);
					wH_StockDetail.setcWh(cWh);
					wH_StockDetail.setcGoodsNo(cGoodsNo); // 商品编码
					wH_StockDetail.setcGoodsName(cGoodsName); // 商品名称
					wH_StockDetail.setcBarcode(cBarcode); // 商品条码
					wH_StockDetail.setfQty_Cur(fQty_Cur); // 库存
					wH_StockDetail.setfCKPrice(fCKPrice);// 进从商品表查出来的价格
					wH_StockDetail.setfInPrice(fCKPrice);// 应为listview以前没有但必须有值
					// WH_StockDetail.s
					Purchaseadapter.list.add(wH_StockDetail);
				}
				System.out.println(Purchaseadapter.list);
				adapter.notifyDataSetChanged();
			}
		}
	}

	public void selectList(int posn) {// 根据集合查商品，并显示到对话框
		wH_StockDetail = Purchaseadapter.list.get(posn);
		String name = wH_StockDetail.getcGoodsName();
		String knum = wH_StockDetail.getfQty_Cur();
		String pri = wH_StockDetail.getfCKPrice();
		String num = wH_StockDetail.getfQuantity();
		System.out.println("价格" + pri);
		if (num != null) {
			goodsnum.setText("" + num);
		} else {
			goodsnum.setText("");
		}
		goodname.setText("品名：" + name);
		kucun.setText("库存：" + knum);
		price.setText(pri);
	}

	public void initListview() {
		adapter = new Purchaseadapter(getActivity());
		listview.setAdapter(adapter);
		Spinnert_Supplier.setEnabled(true);
		Spinnert_Supplier.setSelection(0);

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

				new MyAsySeGoood().execute(bundle.getString("result"));// 根据条形码查询商品
			}
			break;
		}
	}

	// private boolean select(String cBarcode, String no) {// 根据条形码查商品
	// Cursor cursor = db.rawQuery(
	// "select * from t_Goods where (cBarcode=? or cGoodsNo=?) and cSupNo=?",
	// new String[] { cBarcode,cBarcode, no});
	
	private boolean select(String code) { // 根据条形码或编码查商品
		if (loposition == 0 || warehouseposition == 0) {
			return false;
		} else {
			Cursor cursor = db.rawQuery(
					"select * from t_Goods where (cBarcode=? or cGoodsNo=?)",
					new String[] { code, code });
			String cGoodsNo = null;// 商品编号
			cGoodsName = null; // 商品名称
			// String cUnit; // 商品单位
			// String cSpec; // 商品规格
			// String fNormalPrice = null; // 商品售价
			String cBarcode = null;
			fCKPrice = null; // 最新进价
			String cWhNo = listt_WareHouse.get(warehouseposition).getcWhNo(); // 下拉框需要的仓库编号
			String cWh = listt_WareHouse.get(warehouseposition).getcWh(); // 下拉框需要的仓库名称
			System.out.println(cWhNo + "caioasdf" + cWh);
			if (cursor.getCount() == 0) {// 没有查到
				return false;
			} else {
				while (cursor.moveToNext()) {
					cGoodsNo = cursor.getString(cursor
							.getColumnIndex("cGoodsNo"));
					cGoodsName = cursor.getString(cursor
							.getColumnIndex("cGoodsName"));
					fQty_Cur = cursor.getString(cursor
							.getColumnIndex("fQty_Cur"));
					fCKPrice = cursor.getString(cursor
							.getColumnIndex("fCKPrice"));
					cBarcode = cursor.getString(cursor
							.getColumnIndex("cBarcode"));
				}
				cursor.close();
				wH_StockDetail = new WH_StockDetail();
				SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd");
				String date = a.format(new Date());
				wH_StockDetail.setdDate(date); // 采购日期
				wH_StockDetail.setcSupplier(listt_Supplier.get(loposition)
						.getcSupName().toString());// 商家名称
				wH_StockDetail.setcSupplierNo(listt_Supplier.get(loposition)
						.getcSupNo().toString());// 商家编号
				wH_StockDetail.setcOperator(getArguments().getString("name"));// 采购员姓名
				wH_StockDetail.setcOperatorNo(getArguments().getString("id"));// 采购员编号
				wH_StockDetail.setcWhNo(cWhNo);
				wH_StockDetail.setcWh(cWh);
				wH_StockDetail.setcGoodsNo(cGoodsNo); // 商品编码
				wH_StockDetail.setcGoodsName(cGoodsName); // 商品名称
				wH_StockDetail.setcBarcode(cBarcode); // 商品条码
				wH_StockDetail.setfQty_Cur(fQty_Cur); // 库存
				wH_StockDetail.setfCKPrice(fCKPrice);
				return true;
			}
		}
	}

	private boolean SelectSupplier() { // 查店家
		Cursor cursor = db.rawQuery("select * from t_Supplier", null);
		int count = cursor.getCount();
		cursor.close();
		if (count == 0) {
			// 如果没有数据就
			return false;
		} else {// 如果有数据就遍历
			return true;
		}
	}

	private boolean SelectGoods() { // 查商品
		Cursor cursor = db.rawQuery("select * from t_Goods", null);
		int count = cursor.getCount();
		cursor.close();
		if (count == 0) {
			// 如果没有数据就
			return false;
		} else {// 如果有数据就遍历
			return true;
		}
	}

	private boolean SelectWareHouse() {
		Cursor cursor = db.rawQuery("select * from t_WareHouse", null); // 查商品
		int count = cursor.getCount();
		cursor.close();
		if (count == 0) {
			// 如果没有数据就
			return false;
		} else {// 如果有数据就遍历
			return true;
		}
	}

	private void initData() {
		db = GetMyDb.getSQLiteDatabase(getActivity());
		if (SelectSupplier()) { // 如果有数据就
			initSpinner();
		} else {
			Toast.makeText(getActivity(), "请更新商家数据库", Toast.LENGTH_SHORT)
					.show();
			selectgood.setEnabled(false);
		}
		if (!SelectGoods()) {
			Toast.makeText(getActivity(), "请更新商品数据库", Toast.LENGTH_SHORT)
					.show();
		}
		if (SelectWareHouse()) {
			initSpinnerWareHouse();
		} else {
			Toast.makeText(getActivity(), "请更新仓库", Toast.LENGTH_SHORT).show();
		}
	}

	private void initSpinnerWareHouse() {
		Cursor cursor = db.rawQuery("select * from t_WareHouse", null);
		listt_WareHouse = new ArrayList<t_WareHouse>();

		listt_WareHouse.add(new t_WareHouse("", ""));

		while (cursor.moveToNext()) {
			String cWhNo = cursor.getString(cursor.getColumnIndex("cWhNo"));
			String cWh = cursor.getString(cursor.getColumnIndex("cWh"));
			listt_WareHouse.add(new t_WareHouse(cWhNo, cWh));

		}
		cursor.close();
		System.out.println(listt_WareHouse);
		t_WareHouseadapter = new ArrayAdapter<t_WareHouse>(getActivity(),
				R.layout.textxml, R.id.textxml, listt_WareHouse);
		Spinnerwarehouse.setAdapter(t_WareHouseadapter);

	}

	private void initSpinner() {
		Cursor cursor = db.rawQuery("select * from t_Supplier", null);
		listt_Supplier = new ArrayList<t_Supplier>();

		listt_Supplier.add(new t_Supplier("", ""));

		while (cursor.moveToNext()) {
			String cSupNo = cursor.getString(cursor.getColumnIndex("cSupNo"));
			String cSupName = cursor.getString(cursor
					.getColumnIndex("cSupName"));
			listt_Supplier.add(new t_Supplier(cSupNo, cSupName));
		}
		cursor.close();
		System.out.println(listt_Supplier);
		t_SupplierNoadapter = new ArrayAdapter<t_Supplier>(getActivity(),
				R.layout.textxml, R.id.textxml, listt_Supplier);
		Spinnert_Supplier.setAdapter(t_SupplierNoadapter);
	}

	class MyAsySupplier extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			dialog.setTitle("正在初始化spinner");
			commit.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return SelectSupplier();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				initSpinner();
				selectgood.setEnabled(true);
				dialog.hide();

			} else {
				Toast.makeText(getActivity(), "请更新数据", Toast.LENGTH_LONG)
						.show();
			}
			super.onPostExecute(result);
		}
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
			// String no = listt_Supplier.get(loposition).getcSupNo();// 商家编号
			// return select(params[0], no);
			return select(params[0]);

		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.hide();
			if (result == true) {
				commit.setEnabled(true);
				Spinnert_Supplier.setEnabled(false);
				goodname.setText("品名:" + cGoodsName);
				ecode.setText("");
				price.setText(fCKPrice);
				kucun.setText("库存:" + fQty_Cur);
			
				goodsnum.setSelectAllOnFocus(true);
				goodsnum.setFocusable(true);
				goodsnum.setFocusableInTouchMode(true);
				goodsnum.requestFocus();
			} else {
				if (loposition == 0 || warehouseposition == 0) {
					Toast.makeText(getActivity(), "没有输入商家或仓库名称",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity(), "没有这个商品请查看输入是否正确",
							Toast.LENGTH_LONG).show();
				}
				goodname.setText("品名:" + "");
				kucun.setText("库存:" + "");
				price.setText("");
				goodsnum.setText("");
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onDestroy() {
		Purchaseadapter.list = null;
		dialog = null;
		super.onDestroy();
	}
}
