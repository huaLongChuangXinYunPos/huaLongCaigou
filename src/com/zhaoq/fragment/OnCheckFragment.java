package com.zhaoq.fragment;

import com.example.purchase.CheckContentMainActivity;
import com.zhaoq.purchase.R;

import DB.GetMyDb;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class OnCheckFragment extends Fragment {

	private ListView listview;
	private ImageView imageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.oncheckfragme, null);
		listview = (ListView) view.findViewById(R.id.checklistview);
		imageView=(ImageView) view.findViewById(R.id.finish);
		
		event();
		return view;
	}
	@Override
	public void onResume() {
		initData();
		super.onResume();
	}

	private void event() {
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				
			}
		});

	}

	private void initData() {
		SQLiteDatabase db = GetMyDb.getSQLiteDatabase(getActivity());
		String sql = "select DISTINCT * from t_CheckTast_zone where cZoneNo in (select cZoneNo from wh_CheckWhDetail) and cCheckTaskNo in (select cCheckTaskNo from wh_CheckWhDetail)";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {//
			Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_LONG).show();
		} else {
			initlistview(cursor);
		}
	}

	private void initlistview(final Cursor cursor) {
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.onorderitem, null, new String[] { "cCheckTaskNo",
						"cCheckTask", "cZoneNo", "cZoneName" }, new int[] {
						R.id.text1, R.id.text2, R.id.text3, R.id.text4 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		if (cursor != null) {
			adapter.swapCursor(cursor);
		}
		adapter.notifyDataSetChanged();
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cursor.moveToPosition(position);
				int a = cursor.getPosition();
				System.out.println(a);
				String cZoneNo = cursor.getString(cursor
						.getColumnIndex("cZoneNo"));
				String cCheckTaskNo = cursor.getString(cursor
						.getColumnIndex("cCheckTaskNo"));
				Intent intent = new Intent(getActivity(),
						CheckContentMainActivity.class);
				intent .putExtra("cZoneNo", cZoneNo);
				intent .putExtra("cCheckTaskNo", cCheckTaskNo);
				startActivity(intent);
			}
		});
	}

}
