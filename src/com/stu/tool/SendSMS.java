package com.stu.tool;

import java.util.UUID;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SendSMS {

	private String SERVER_URL = "http://120.119.77.60/~ali/eumps/service/receive.php";

	/**
	 * Detect the network status, if sent directly offline messages, or sending
	 * data to the server
	 * 
	 * @param context
	 * @param phone
	 *            cell phone number
	 * @param content
	 *            message content
	 */

	public SendSMS(Context context, String phone, String content,
			String location) {

		Internet it = new Internet(context);
		String text = null;
		if (it.isConnectedOrConnecting()) {
			Log.e("network", "online");
			Log.e("isAvailable", it.isAvailable() + "");
			Log.e("isConnected", it.isConnected() + "");
			Log.e("isConnectedOrConnecting", it.isConnectedOrConnecting() + "");
			Log.e("isFailover", it.isFailover() + "");
			Log.e("isWanConnect", it.isWanConnect("120.119.77.60") + "");
			String uid = UUID.randomUUID().toString().replaceAll("-", "");
			String url = SERVER_URL + "?MSGID="
					+ uid.substring(uid.length() - 6, uid.length()) + "&OA="
					+ phone + "&SM=" + "http://maps.google.com.tw/maps?q="
					+ location + "%0a" + content + "iHELP";

			Log.e("url", url);
			String result = it.GetTo(url);
			Log.e("result", result);
		} else {
			Log.e("network", "offline");
			text = "http://maps.google.com.tw/maps?q=" + location + "%0a"
					+ content + "&iHELP";
			sendMessage(context, phone, text);
		}

	}

	/**
	 * Send SMS message
	 * 
	 * @param content
	 *            SMS content
	 */
	public void sendMessage(Context context, String phone, String content) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				new Intent(), 0);

		smsManager.sendTextMessage(phone, null, content, pendingIntent, null);
	}

}
