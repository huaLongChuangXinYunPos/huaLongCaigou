package com.zhaoq.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zhaoq.purchase.R;
import com.example.qr_codescan.MipcaActivityCapture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhaoq.bean.Stock;

import Tool.VoleyToll;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class StockFragment extends Fragment {

	private Button button, chaxun;

	@ViewInject(R.id.stockfinish)
	private ImageView finish;

	private TextView bianhao, num, name, goodcode ;
	private EditText editText;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	List<Stock> list = null;

	public static Fragment getFragment() {
		Fragment f = new StockFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.stock, null);
		ViewUtils.inject(this, view);
		button = (Button) view.findViewById(R.id.scanning);
		chaxun = (Button) view.findViewById(R.id.chaxun);
		
		bianhao = (TextView) view.findViewById(R.id.bianhao);
		num = (TextView) view.findViewById(R.id.num);
		name = (TextView) view.findViewById(R.id.name);
		goodcode = (TextView) view.findViewById(R.id.code);
		
		editText = (EditText) view.findViewById(R.id.edit);
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();

			}
		});
		button.setOnClickListener(new OnClickListener() {// 扫描

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

			}
		});
		chaxun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String code = editText.getText().toString();
				if (!code.isEmpty())
					select(code);
			}
		});
		return view;
	}

	protected void select(String code) {

		StringRequest request = new StringRequest(Request.Method.GET,
				"http://192.168.3.199:1234/AppServer/AppServer.aspx?action=FCUR&cBarcode="
						+ code, new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						Log.i("tag", s);
						try {
							JSONObject obj = new JSONObject(s);
							String resultStatus = obj.getString("resultStatus");
							if (resultStatus.equals("0")) {
								name.setText("没有此商品");
							} else {
								JSONArray array = obj.getJSONArray("data");
								Gson gson = new Gson();
								list = gson.fromJson(array.toString(),
										new TypeToken<List<Stock>>() {
										}.getType());
								Stock goods = list.get(0);
								// private String cGoodsNo;
								// private String EndQty;
								// private String cGoodsName;
								// private String cBarcode;
								// private String cSpec;
								// private String cUnit;
//								商品单位：cUnit varchar(16),
//								  商品规格：cSpec  varchar(16), 
								String cBarcode = goods.getcBarcode();
								String cGoodsNo = goods.getcGoodsNo();
								String cGoodsName = goods.getcGoodsName();
								String EndQty = goods.getEndQty();
							
								bianhao.setText("商 品 编 号:   "+cGoodsNo);
								num.setText("商 品 数 量:    "+EndQty);
								name.setText("商 品 名 字:     "+cGoodsName);
								goodcode.setText("商 品 条 码:  "+cBarcode);
								
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Log.i("tag", "出错volley");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "赵秋阳");
				return map;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		VoleyToll.getRequestQueue(getActivity()).add(request);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == getActivity().RESULT_OK) {
				Bundle bundle = data.getExtras();

				editText.setText(bundle.getString("result"));
				select(bundle.getString("result"));

			}
			break;
		}
	}

}
