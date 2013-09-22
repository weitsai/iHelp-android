package edu.stu.ihelp.client;

import java.util.HashSet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class IHelpActivity extends Activity {

    public static final int INTENT_DATA = 0;
    public static final int INTENT_GENERAL = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Variable.getData(IHelpActivity.this);
        
        SharedPreferences spfs = getSharedPreferences("PersonalData", 0);
        Variable.name = spfs.getString("UserName", "");
        Variable.contactsPhone = (HashSet<String>) spfs.getStringSet(
                "contacts", Variable.contactsPhone);
        if (Variable.name.equals("")) {
            Toast.makeText(this, "建議您到個人資料輸入姓名", 0).show();
        }

    }

    boolean bool = false;

    public void setPersonalData(View v) {
        if (!bool) {
            bool = true;
            startActivityForResult(new Intent(IHelpActivity.this, PersonalData.class), INTENT_DATA);
        }
    }

    public void onClickGeneral(View v) {
        startActivityForResult(new Intent(IHelpActivity.this, General.class), INTENT_GENERAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bool = false;

        super.onActivityResult(requestCode, resultCode, data);
    }
}