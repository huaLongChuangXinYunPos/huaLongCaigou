package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class GetMyDb {
	private static SQLiteDatabase db = null;
	static Mysqlite sq;

	public static SQLiteDatabase getSQLiteDatabase(Context context) {
		if (db == null) {
			sq = new Mysqlite(context);
			db = sq.getWritableDatabase();
		}
		return db;
	}

}
