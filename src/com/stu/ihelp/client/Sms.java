package com.stu.ihelp.client;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;

public class Sms {
	Sms(String phoneNum, String msg, Context context, String daddr) {
		// 取得預設的SmsManager
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);

		String url = "120.119.77.249/a?a=" + daddr;
		// 傳送SMS
		smsManager.sendTextMessage(phoneNum, null, url + "\n" + msg, pendingIntent, null);
		// System.out.println((url + "\n" + msg).length());
	}

}
