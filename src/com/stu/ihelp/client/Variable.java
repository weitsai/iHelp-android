package com.stu.ihelp.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Variable {
	public static final String FILENAME = "data";
	public static final String NAME = "name";
	public static final String CONTACT_PHONE = "contact_phone";
	private static SharedPreferences spfs;
	static String name = "";
	static String gender = "";
	static String phome = "";
	static String idNumbet = "";
	static String disaster = "";
	static String diStringNumber = "";
	static String Address = "";
	static String contact = "";
	static String contact_phone = "";
	static String location = "";

	static String web_id;

	// 緯度
	static String Latitude = "";
	// 經度
	static String Longitude = "";

	static void setData(Context context) {
		spfs = context.getSharedPreferences(Variable.FILENAME,
				Context.MODE_PRIVATE);

		Variable.name = spfs.getString(Variable.NAME, "");
		Log.e("name", spfs.getString("name", ""));
		Variable.contact_phone = spfs.getString(Variable.CONTACT_PHONE, "");
		Log.e("phone", spfs.getString("contact_phone", ""));
	}

	static boolean checkData() {
		if (name.equals("") || idNumbet.equals("") || contact.equals("")) {
			return false;
		}
		return true;
	}

	static void getAllData() {
		System.out.println(name + ", " + idNumbet + ", " + contact);
	}
}
