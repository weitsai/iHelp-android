package com.stu.tool;

import java.util.List;

import android.content.Context;
import android.telephony.gsm.SmsManager;

public class Sms {
  Sms(String phoneNum, String msg, String daddr) {
    // 取得預設的SmsManager
    SmsManager sms = SmsManager.getDefault();
    List<String> texts = sms.divideMessage(msg);
    for (String text : texts) {
      sms.sendTextMessage(phoneNum, null, text, null, null);
    }
  }
}
