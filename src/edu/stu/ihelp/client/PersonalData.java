package edu.stu.ihelp.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.stu.ihelp.client.PersonalData.ContactList.ViewHolder;

public class PersonalData extends Activity {

    private EditText et_name, et_contact_phone;
    private Button confirm, cancel;
    private SharedPreferences spfs;

    private ContentResolver resolver;

    private ContactList adapter;
    private ListView listview;
    private Cursor contacts_number;
    private Map<String, String> contactsMap;
    private List<Map<String, String>> contactsArrayList;
    private static final String NAME = "name";
    private static final String PHONE = "PHONE";

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

                Variable.name = et_name.getEditableText().toString();
                Variable.contact_phone = et_contact_phone.getEditableText()
                        .toString();

                spfs.edit()
                        .putString(Variable.NAME,
                                et_name.getEditableText().toString())
                        .putString(Variable.CONTACT_PHONE,
                                et_contact_phone.getEditableText().toString())
                        .commit();
                setResult(RESULT_OK);
                Toast.makeText(PersonalData.this, "儲存成功", 0).show();
                PersonalData.this.finish();

            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // getContact();
                setResult(RESULT_CANCELED);
                Toast.makeText(PersonalData.this, "取消儲存", 0).show();
                PersonalData.this.finish();

            }
        });
    }

    private void getContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalData.this);
        LayoutInflater factory = LayoutInflater.from(PersonalData.this);
        View view = factory.inflate(R.layout.contact_people, null);
        builder.setView(view);
        listview = (ListView) view.findViewById(R.id.contact_list);
        resolver = PersonalData.this.getContentResolver();

        getPhoneBookData();

        adapter = new ContactList(PersonalData.this, contactsArrayList);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Log.e("click", arg2 + "");
                ViewHolder holder = (ViewHolder) arg1.getTag();
                holder.selected.toggle();

                adapter.setStatus(arg2, holder.selected.isChecked());

            }
        });

        builder.setPositiveButton("confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

        builder.create().show();
    }

    private void getPhoneBookData() {
        contactsArrayList = new ArrayList<Map<String, String>>();
        contacts_number = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        String name, phone;
        while (contacts_number.moveToNext()) {

            name = contacts_number.getString(contacts_number
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            phone = contacts_number
                    .getString(contacts_number
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (!phone.equals("")) {
                contactsMap = new HashMap<String, String>();
                contactsMap.put(NAME, name);
                contactsMap.put(PHONE, phone);
                contactsArrayList.add(contactsMap);
            }

        }
    }

    class ContactList extends BaseAdapter {
        LayoutInflater inflater;
        List<Boolean> btnStatus;
        List<Map<String, String>> mList;

        ContactList(Context context, List<Map<String, String>> list) {
            this.inflater = LayoutInflater.from(context);
            this.mList = list;
            btnStatus = new ArrayList<Boolean>();
            for (int i = 0; i < mList.size(); i++) {
                btnStatus.add(false);
            }
        }

        public boolean getStatus(int position) {
            return btnStatus.get(position);
        }

        public void setStatus(int position, boolean bn) {
            btnStatus.set(position, bn);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.people, parent, false);
                holder.name = (TextView) view.findViewById(R.id.contact_name);
                holder.phone = (TextView) view.findViewById(R.id.contact_phone);
                holder.selected = (CheckBox) view.findViewById(R.id.select);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.name.setText(mList.get(position).get(NAME));
            holder.phone.setText(mList.get(position).get(PHONE));
            holder.selected.setChecked(btnStatus.get(position));

            return view;
        }

        class ViewHolder {
            TextView name;
            TextView phone;
            CheckBox selected;
        }

    }

}
