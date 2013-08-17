package com.stu.ihelp.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PersonalData extends Activity {

	private EditText name, contact, contact_phone;
	private Button confirm, cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_data);

		name = (EditText) findViewById(R.id.edit_name);
		contact = (EditText) findViewById(R.id.edit_contact);
		contact_phone = (EditText) findViewById(R.id.edit_contact_phone);
		confirm = (Button) findViewById(R.id.btn_submit);
		cancel = (Button) findViewById(R.id.btn_cancel);

		name.setText(Variable.name);
		contact.setText(Variable.contact);
		contact_phone.setText(Variable.contact_phone);

		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Variable.name = name.getEditableText().toString();
				Variable.contact = contact.getEditableText().toString();
				Variable.contact_phone = contact_phone.getEditableText()
						.toString();
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
