package com.ak47.checkin_app;

import com.ak47.checkin_app.bean.MyUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GloableParams {

	// 屏幕高度 宽度
	public static int WIN_WIDTH;
	public static int WIN_HEIGHT;
	public static Map<String, MyUser> Users = new HashMap<String, MyUser>();
	public static List<MyUser> UserInfos = new ArrayList<MyUser>();// 好友信息
	public static Boolean isHasPulicMsg = false;
}
