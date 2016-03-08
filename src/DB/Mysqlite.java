package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Mysqlite extends SQLiteOpenHelper {

	public static String t_Supplier = "create table t_Supplier(_id Integer PRIMARY KEY AutoIncrement,cSupNo  text,cSupName text)";
	public static String t_WareHouse = "create table t_WareHouse(_id Integer PRIMARY KEY AutoIncrement,cWhNo  text,cWh text)";
	public static String t_Pass = "create table t_Pass(_id Integer PRIMARY KEY AutoIncrement,User  text,Name text,Pass text)";
	public static String t_Goods = "create table t_Goods(_id Integer PRIMARY KEY AutoIncrement,cGoodsNo text,cGoodsName text,cBarcode text,cUnit text,cSpec  text, fNormalPrice text,fCKPrice text,cSupNo text,fQty_Cur text)";
	public static String WH_StockDetail = "create table WH_StockDetail(_id Integer PRIMARY KEY AutoIncrement,dDate text,cSupplier text,cSupplierNo text,cOperator text,cOperatorNo text,cWhNo text,cWh  text,cGoodsNo text,cGoodsName text,cBarcode text,fQuantity text ,fInPrice text,fInMoney text,fQty_Cur text)";
	public static String t_CheckTast = "create table t_CheckTast(_id Integer PRIMARY KEY AutoIncrement,cCheckTaskNo text,cCheckTask text,dCheckTask text)";
	public static String t_CheckTast_zone = "create table t_CheckTast_zone(_id Integer PRIMARY KEY AutoIncrement,cCheckTaskNo varchar(32),cCheckTask varchar(32),cZoneNo varchar(32),cZoneName text)";
	public static String wh_CheckWhDetail = "create table wh_CheckWhDetail(_id Integer PRIMARY KEY AutoIncrement,cGoodsNo varchar(32),cGoodsName varchar(64),fQuantity text,cBarcode text,fNormalPrice text,cCheckTaskNo text,cZoneNo text,cOperatorNo text,cOperator text)";
    public static String wh_StockVerifyDetail="create table wh_StockVerifyDetail(_id Integer PRIMARY KEY AutoIncrement,dDate text,cSupplier text,cSupplierNo text,cOperator text,cOperatorNo text,cWhNo text,cWh  text,cGoodsNo text,cGoodsName text,cBarcode text,fQuantity text ,fInPrice text,fInMoney text,fQty_Cur text)";
	public Mysqlite(Context context) {
		super(context, "Purchase.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 供应商t_Supplier【商家编号：cSupNo varchar(32),商家名称：cSupName varchar(64)】
		db.execSQL(t_Supplier);

		// 0:仓库表:t_WareHouse[仓库编号：cWhNo varchar(32),仓库名称：cWh varchar(32)]
		db.execSQL(t_WareHouse);
		// 1、操作员信息表t_Pass【用户编号:User varchar(32),用户姓名：Name varchar(32),密码：Pass
		// varchar(32)】
		db.execSQL(t_Pass);
		// 2、商品基本信息表t_Goods【
		// 商品编号：cGoodsNo varchar(32),
		// 商品名称：cGoodsName varchar(64),
		// 商品条码：cBarcode varchar(32),
		// 商品单位：cUnit varchar(16),
		// 商品规格：cSpec varchar(16),
		// 商品售价：fNormalPrice money,
		// 最新进价：fCkPrice money
		// 商家编号 ；,cSupNo text
		// 【1000、2000、500】
		// 4万 分成多表： 1、6901668055033 --69016680***** 【500】 【表1】
		// 2、69016680***** --69016680***** 【500】 【表1】
		// db.execSQL("create table t_Goods(_id Integer PRIMARY KEY AutoIncrement,商品编号：cGoodsNo varchar(32),商品名称：cGoodsName varchar(64),       商品条码：cBarcode varchar(32),商品单位：cUnit varchar(16),商品规格：cSpec  varchar(16),        商品售价：fNormalPrice money,         最新进价：fCkPrice money");
		db.execSQL(t_Goods);
		// 3、采购信息表WH_StockDetail[采购日期：dDate datetime,商家名称：cSupplier
		// varchar(32),商家编号：cSupplierNo varchar(32),
		// 采购员姓名：cOperator varchar(32),采购员编号：cOperatorNo varchar(32),
		// 仓库编号：cWhNo varchar(32), 仓库名称：cWh varchar(32),
		// 商品编号：cGoodsNo varchar(32),商品名称：cGoodsName varchar(64),
		// 商品条码：cBarcode varchar(32),采购数量：fQuantity money ,采购单价：fInPrice
		// money,采购金额：fInMoney money]
		//fQty_Cur 库存
		db.execSQL(WH_StockDetail);

		// 4、盘点任务表t_CheckTast[任务编号：cCheckTaskNo varchar(32),任务名称：cCheckTask
		// varchar(32),盘点日期:dCheckTask datetime]
		db.execSQL(t_CheckTast);
		// 5、盘点子单表t_CheckTast_zone[任务编号：cCheckTaskNo varchar(32),任务名称：cCheckTask
		// varchar(32),
		// 子单编号：cZoneNo varchar(32),子单名称：cZoneName varchar(32)]
		db.execSQL(t_CheckTast_zone);
		// 6、盘点子单商品表wh_CheckWhDetail【
		// 商品编号：cGoodsNo varchar(32),
		// 商品名称：cGoodsName varchar(64),
		// 盘点数量：fQuantity money
		// 商品条码：cBarcode text,
		// 商品售价：fNormalPrice text
		// 任务编号：cCheckTaskNo varchar(32),
		// 子单编号：cZoneNo varchar(32),
		db.execSQL(wh_CheckWhDetail);
		db.execSQL(wh_StockVerifyDetail);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
