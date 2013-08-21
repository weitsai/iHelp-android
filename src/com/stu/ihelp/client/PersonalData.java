package com.stu.ihelp.client;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class PersonalData extends Activity {

	private EditText et_name, et_contact_phone;
	private Button confirm, cancel;
	private SharedPreferences spfs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_data);

		et_name = (EditText) findViewById(R.id.edit_name);
		et_contact_phone = (EditText) findViewById(R.id.edit_contact_phone);
		confirm = (Button) findViewById(R.id.btn_submit);
		cancel = (Button) findViewById(R.id.btn_cancel);

		et_name.setText(Variable.name);
		et_contact_phone.setText(Variable.contact_phone);

		spfs = getSharedPreferences(Variable.FILENAME, MODE_PRIVATE);

		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				spfs.edit()
						.putString(Variable.NAME,
								et_name.getEditableText().toString())
						.putString(
								Variable.CONTACT_PHONE,
								et_contact_phone.getEditableText().toString())
						.commit();
				setResult(RESULT_OK);
				PersonalData.this.finish();

			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				PersonalData.this.finish();

			}
		});
	}
}
