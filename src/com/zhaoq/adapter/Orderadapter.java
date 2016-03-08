package com.zhaoq.adapter;
import java.util.List;
import com.zhaoq.purchase.R;
import com.zhaoq.bean.CSupplier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Orderadapter extends BaseAdapter {

	private Context context;
	private List<CSupplier> list;

	public Orderadapter(Context context, List<CSupplier> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
					R.layout.onorderitem, null);
			viewhold.CSupplierno = (TextView) convertView
					.findViewById(R.id.text1);
			viewhold.CSupplierNO = (TextView) convertView
					.findViewById(R.id.text2);
			viewhold.CSupplierName = (TextView) convertView
					.findViewById(R.id.text3);
			viewhold.Date=(TextView)convertView.findViewById(R.id.text4);

			convertView.setTag(viewhold);
		} else {
			viewhold = (ViewHold) convertView.getTag();
		}
		viewhold.CSupplierno.setText(""+position);
		viewhold.CSupplierNO.setText(list.get(position).getcSupplierNo());
		viewhold.CSupplierName.setText(list.get(position).getcSupplier());
		viewhold.Date.setText(list.get(position).getdDate());

		return convertView;
	}

	class ViewHold {
		TextView CSupplierno, CSupplierNO, CSupplierName,Date;

	}

}
