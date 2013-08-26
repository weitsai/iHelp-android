package edu.stu.ihelp.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IHelpActivity extends Activity {

    public static final int INTENT_DATA = 0;
    public static final int INTENT_GENERAL = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Variable.setData(IHelpActivity.this);
        
        if (!Variable.existData()) {
            startActivityForResult(new Intent(IHelpActivity.this, PersonalData.class), INTENT_DATA);
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