package com.zhaoq.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoq.purchase.R;
import com.zhaoq.bean.WH_StockDetail;

public class Purchaseadapter extends BaseAdapter {
	private Context context;
	public static List<WH_StockDetail> list;

	public Purchaseadapter(Context context) {
		this.context = context;
		Purchaseadapter.list = new ArrayList<WH_StockDetail>();
	}

	@Override
	public int getCount() {

		return list.size();
	}

	public void setData(WH_StockDetail good) {
		Purchaseadapter.list.add(good);
	}

	public void reMove(int i, SQLiteDatabase db) { //第三个参数是状态，1是多彩，0是单采
		try {
			Cursor cursor = db.rawQuery("select * from WH_StockDetail where cBarcode=?", null);
			cursor.moveToPosition(i);// 把cursor作为数据源
			String id = cursor.getString(cursor.getColumnIndex("_id"));//
			long rowid = db.delete("WH_StockDetail", "_id=?",
					new String[] { id });
			if (rowid == 1) {
				Purchaseadapter.list.remove(i);
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
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHold viewhold;
		if (convertView == null) {
			viewhold = new ViewHold();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitems, null);
			viewhold.goodno = (TextView) convertView.findViewById(R.id.text1);
			viewhold.goodname = (TextView) convertView.findViewById(R.id.text2);
			viewhold.goodcode = (TextView) convertView.findViewById(R.id.text3);
			viewhold.goodnum = (TextView) convertView.findViewById(R.id.text4);
			viewhold.goodprice = (TextView) convertView
					.findViewById(R.id.text5);
			viewhold.goodmoney = (TextView) convertView
					.findViewById(R.id.text6);
			viewhold.goodstock = (TextView) convertView
					.findViewById(R.id.text7);
			convertView.setTag(viewhold);
		} else {
			viewhold = (ViewHold) convertView.getTag();
		}
		viewhold.goodcode.setText(list.get(position).getcBarcode());
		viewhold.goodname.setText(list.get(position).getcGoodsName());
		viewhold.goodno.setText("" + position);
		viewhold.goodnum.setText(list.get(position).getfQuantity());
		viewhold.goodprice.setText(list.get(position).getfInPrice() + "");
		viewhold.goodmoney.setText(list.get(position).getfInMoney() + "");
		viewhold.goodstock.setText(list.get(position).getfQty_Cur() + "");

		return convertView;
	}

	class ViewHold {
		TextView goodno, goodname, goodcode, goodnum, goodprice, goodmoney,
				goodstock;

	}
}
