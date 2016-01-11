package com.ak47.checkin_app.common;

import android.content.Context;
import android.text.TextUtils;

import com.ak47.checkin_app.Constants;
import com.ak47.checkin_app.MyUser;
import com.alibaba.fastjson.JSON;

public class UserUtils {
	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static MyUser getUserModel(Context context) {
		MyUser user = null;
		String jsondata = Utils.getValue(context, Constants.UserInfo);
		// Log.e("", jsondata);
		if (!TextUtils.isEmpty(jsondata))
			user = JSON.parseObject(jsondata, MyUser.class);
		return user;
	}

	/**
	 * 获取用户ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserID(Context context) {
		MyUser user = getUserModel(context);
		if (user != null)
			return user.getTelephone();
		else
			return "";
	}

	/**
	 * 获取用户名字
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		MyUser user = getUserModel(context);
		if (user != null)
			return user.getUserName();
		else
			return "";
	}
}
