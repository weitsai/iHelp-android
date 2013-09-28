package edu.stu.ihelp.client;

import java.util.Arrays;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

public class IHelpActivity extends Activity {

    public static final int INTENT_DATA = 0;
    public static final int INTENT_GENERAL = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences spfs = getSharedPreferences("PersonalData", 0);
        Variable.name = spfs.getString("UserName", "");
        String contactsPhone = spfs.getString("contacts", "");
        if (!contactsPhone.equals(""))
            Variable.contactsPhone = new HashSet<String>(
                    Arrays.asList(contactsPhone.split(",")));
        if (Variable.name.equals("")) {
            Toast.makeText(this, "建議您到個人資料輸入姓名", 0).show();
        }

    }

    boolean bool = false;

    public void setPersonalData(View v) {
        if (!bool) {
            bool = true;
            startActivityForResult(new Intent(IHelpActivity.this,
                    PersonalData.class), INTENT_DATA);
        }
    }

    public void onClickGeneral(View v) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getSimState()) {
        case TelephonyManager.SIM_STATE_ABSENT:
            Toast.makeText(this, "若沒有插入 sim 卡可能無法使用該服務", 0).show();
            break;
        case TelephonyManager.SIM_STATE_UNKNOWN:
            Toast.makeText(this, "sim 卡發生了不知名狀況請聯絡電信商", 0).show();
            break;
        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
            Toast.makeText(this, "請先將 NetworkPIN 碼解鎖", 0).show();
            break;
        case TelephonyManager.SIM_STATE_PIN_REQUIRED:
            Toast.makeText(this, "請先將 sim 卡 PIN 碼解鎖", 0).show();
            break;
        case TelephonyManager.SIM_STATE_PUK_REQUIRED:
            Toast.makeText(this, "請先將 sim 卡 PUK 碼解鎖", 0).show();
            break;
        case TelephonyManager.SIM_STATE_READY:
            startActivityForResult(
                    new Intent(IHelpActivity.this, General.class),
                    INTENT_GENERAL);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bool = false;

        super.onActivityResult(requestCode, resultCode, data);
    }
}