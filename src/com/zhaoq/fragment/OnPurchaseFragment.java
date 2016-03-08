package com.zhaoq.fragment;

import java.util.ArrayList;
import java.util.List;
import com.zhaoq.purchase.R;
import com.zhaoq.adapter.Orderadapter;
import com.zhaoq.bean.CSupplier;
import com.zhaoq.purchase.OnPurchaseContentMainActivity;

import DB.GetMyDb;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OnPurchaseFragment extends Fragment {
	private ListView listview;
	private List<CSupplier> list;
	private ImageView finish;
	Orderadapter adapter;

	public static Fragment getFragment() {
		Fragment f = new OnPurchaseFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.onorder, null);
		finish=(ImageView) view.findViewById(R.id.finish);
		listview = (ListView) view.findViewById(R.id.orderlistview);
		InitData();
		event();
		return view;
	}

	private void event() {
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				
			}
		});
		
	}

	public void InitData() {
		list = new ArrayList<CSupplier>();
		SQLiteDatabase db = GetMyDb.getSQLiteDatabase(getActivity());
		Cursor cursor = db
				.rawQuery(
						"select  DISTINCT dDate ,cSupplier ,cSupplierNo from WH_StockDetail",
						null);

		if (cursor.getCount() == 0) {//
			Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_LONG).show();
		} else {// 如果有数据就遍历
			while (cursor.moveToNext()) {
				String cSupplier = cursor.getString(cursor
						.getColumnIndex("cSupplier"));
				String cSupNo = cursor.getString(cursor
						.getColumnIndex("cSupplierNo"));
				String dDate = cursor.getString(cursor.getColumnIndex("dDate"));
			
				list.add(new CSupplier(cSupNo, cSupplier, dDate));
			}
			adapter = new Orderadapter(getActivity(), list);
			listview.setAdapter(adapter);
			System.out.println(list);
		}
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						OnPurchaseContentMainActivity.class);
				if (list.get(position) != null) {
					intent.putExtra("No", list.get(position).getcSupplierNo());
					intent.putExtra("cSupplier", list.get(position)
							.getcSupplier());
					intent.putExtra("Date", list.get(position).getdDate());
				
					startActivity(intent);
				}

			}
		});

	}
}
