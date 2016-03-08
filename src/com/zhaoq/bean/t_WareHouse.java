package com.zhaoq.bean;

//0:仓库表:t_WareHouse[仓库编号：cWhNo varchar(32),仓库名称：cWh varchar(32)]

public class t_WareHouse {
	public t_WareHouse(String cWhNo, String cWh) {
		super();
		this.cWhNo = cWhNo;
		this.cWh = cWh;
	}

	private String cWhNo;
	@Override
	public String toString() {
		return cWh;
	}

	private String cWh;

	public String getcWhNo() {
		return cWhNo;
	}

	public void setcWhNo(String cWhNo) {
		this.cWhNo = cWhNo;
	}

	public String getcWh() {
		return cWh;
	}

	public void setcWh(String cWh) {
		this.cWh = cWh;
	}

}
