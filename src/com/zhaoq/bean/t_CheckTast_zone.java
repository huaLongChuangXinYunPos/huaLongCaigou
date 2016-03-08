package com.zhaoq.bean;

//5、盘点子单表t_CheckTast_zone[任务编号：cCheckTaskNo varchar(32),任务名称：cCheckTask varchar(32),
//                        子单编号：cZoneNo varchar(32),子单名称：cZoneName varchar(32)]
public class t_CheckTast_zone {

	private String cCheckTaskNo;
	private String cCheckTask;
	private String cZoneNo;
	private String cZoneName;

	@Override
	public String toString() {
		return cZoneNo;
	}


	public t_CheckTast_zone(String cZoneNo, String cZoneName) {
		super();
		this.cZoneNo = cZoneNo;
		this.cZoneName = cZoneName;
	}


	public String getcCheckTaskNo() {
		return cCheckTaskNo;
	}

	public void setcCheckTaskNo(String cCheckTaskNo) {
		this.cCheckTaskNo = cCheckTaskNo;
	}

	public String getcCheckTask() {
		return cCheckTask;
	}

	public void setcCheckTask(String cCheckTask) {
		this.cCheckTask = cCheckTask;
	}

	public String getcZoneNo() {
		return cZoneNo;
	}

	public void setcZoneNo(String cZoneNo) {
		this.cZoneNo = cZoneNo;
	}

	public String getcZoneName() {
		return cZoneName;
	}

	public void setcZoneName(String cZoneName) {
		this.cZoneName = cZoneName;
	}

}
