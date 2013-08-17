package com.stu.ihelp.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

public class Contact extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact);
	}

	public void submit(View v) {
		Variable.contact = "";
		finish();

	}

	public void clock(View v) {
		finish();
	}
	
	
}
