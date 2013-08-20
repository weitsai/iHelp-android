package com.stu.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Receive SMS broadcast receiving system, the Coustom function that do
 * something when receiver SMS broadcast.
 * 
 * @author L
 * 
 */
public class receiverSMS extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String fromAddress = message.getOriginatingAddress();

                String msg = message.getMessageBody();
                Log.e("phone number" + fromAddress, "content" + msg);
            }

            custome(context);

        }
    }

    /**
     * Override the method do somethings D
     * 
     * @param context
     */
    public void custome(Context context) {

    }

}
