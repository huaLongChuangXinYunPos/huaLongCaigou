package com.zhaoq.adapter;

import java.util.ArrayList;
import java.util.List;
import com.zhaoq.purchase.R;
import com.zhaoq.bean.wh_CheckWhDetail;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Checkadapter extends BaseAdapter {
	private Context context;
	public static List<wh_CheckWhDetail> list;

	public Checkadapter(Context context) {
		this.context = context;
		Checkadapter.list = new ArrayList<wh_CheckWhDetail>();
	}

	public void setData(wh_CheckWhDetail good) {
		Checkadapter.list.add(good);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void reMove(int i, SQLiteDatabase db) {
		try {
			Cursor cursor = db.rawQuery("select * from wh_CheckWhDetail", null);
			cursor.moveToPosition(i);
			String id = cursor.getString(cursor.getColumnIndex("_id"));//
			long rowid = db.delete("wh_CheckWhDetail", "_id=?",
					new String[] { id });
			System.out.println(id);
			if (rowid == 1) {
				Checkadapter.list.remove(i);
				notifyDataSetChanged();
				Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "删除失败", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "删除失败", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewhold;
		if (convertView == null) {
			viewhold = new ViewHold();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.checkadapteritem, null);
			viewhold.id = (TextView) convertView.findViewById(R.id.text1);
			viewhold.name = (TextView) convertView.findViewById(R.id.text2);
			viewhold.ecode = (TextView) convertView.findViewById(R.id.text3);
			viewhold.num = (TextView) convertView.findViewById(R.id.text4);
			viewhold.price = (TextView) convertView.findViewById(R.id.text5);
			convertView.setTag(viewhold);
		} else {
			viewhold = (ViewHold) convertView.getTag();
		}
		final wh_CheckWhDetail check = list.get(position);
		viewhold.id.setText(check.getcGoodsNo());
		viewhold.name.setText(check.getcGoodsName());
		viewhold.ecode.setText(check.getcBarcode());
		viewhold.num.setText(check.getfQuantity());
		viewhold.price.setText(check.getfNormalPrice());

		return convertView;
	}

	class ViewHold {
		TextView id, name, ecode, num, price;// 售价
	}

}
