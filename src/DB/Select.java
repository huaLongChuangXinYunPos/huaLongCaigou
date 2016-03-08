package DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Select {

	public static Cursor selectWH_StockDetail(String No,String date ,Context context) {
		SQLiteDatabase db = GetMyDb.getSQLiteDatabase(context);
		Cursor cursor = db.rawQuery(
				"select * from WH_StockDetail where cSupplierNo=? and dDate=?",
				new String[] {No,date});
		return cursor;// 跟具公司编号查订单

	}
	public static Cursor wh_StockVerifyDetail(String No,String date ,Context context) {
		SQLiteDatabase db = GetMyDb.getSQLiteDatabase(context);
		Cursor cursor = db.rawQuery(
				"select * from wh_StockVerifyDetail where cSupplierNo=? and dDate=?",
				new String[] {No,date});
		return cursor;// 跟具公司编号查订单

	}
	public static boolean select(String sql, SQLiteDatabase db){
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		if (count == 0) {
			// 如果没有数据就
			return false;
		} else {// 如果有数据就遍历
			return true;
		}
	}


}
