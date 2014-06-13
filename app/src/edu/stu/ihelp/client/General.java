package edu.stu.ihelp.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import test.whell.OnWheelChangedListener;
import test.whell.OnWheelScrollListener;
import test.whell.WheelView;
import test.whell.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import java.util.Collections;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemSelectedListener;  
import android.widget.ArrayAdapter;  
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;  
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import edu.stu.tool.Internet;
import edu.stu.tool.Locate;
import edu.stu.db.CityAdapter;

public class General extends Activity{

    private EditText reportData;
    private EditText reportPosition;
    private TextView helper = null;
    private TextView dialogtitle = null;
    private TextView message = null;
    private CheckBox check;
    private static final String[] joinData={"緊急事故","火災","身體狀況","鬧事","搶劫","交通事故","偷竊"};
    private static final String[] joinCity={"臺北市","新北市","臺中市","臺南市","高雄市","基隆市","新竹市","嘉義市","桃園縣","新竹縣","苗栗縣","彰化縣","南投縣","雲林縣","嘉義縣","屏東縣","宜蘭縣","花蓮縣","臺東縣","澎湖縣","金門縣","連江縣"};
    private Spinner spinner1;
    private Spinner spinner2;
    private ArrayAdapter<String> adapter = null;
    private ArrayAdapter<String> adapter2 = null;
    private String reportBody = "";
    private String address = "";
    private String title = "";
    private String locatedArray[];
    private String city = "";
    private String cityPhone = "";
    private Locate gps;
    private Dialog dialog;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.general);
        reportData = (EditText) findViewById(R.id.report_data);
        reportPosition = (EditText) findViewById(R.id.report_position);
        helper = (TextView) findViewById(R.id.helper);
        
        
   //災情選擇
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,joinData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){    
             
                if(arg2 == 0){                  
                }
                else if(arg2 <=2){
                    helper.setText("消防局");
                    helper.setTextColor(Color.RED);
                 
                }else{          
                    helper.setText("警察局");
                    helper.setTextColor(Color.BLUE);
                }       
            }
            public void onNothingSelected(AdapterView<?> arg0){
            }
        });
        spinner1.setVisibility(View.VISIBLE);
        
        
   //發生位置
        gps = new Locate(General.this);
        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
        
        if(checkIntrnet() && gps.canGetLocation()){
            try{
                address = gps.getAddress();
                reportBody = address;
                final String located = gps.getPosition();
                title = "http://maps.google.com.tw/maps?q=" + located + "&GeoSMS=iHELP\n";

                locatedArray = located.split(",");
                cityPhone = gps.getCityPhone(
                        Double.parseDouble(locatedArray[1]),
                        Double.parseDouble(locatedArray[0]),
                        spinner1.getSelectedItemPosition());
                city = gps.getCityName(
                        Double.parseDouble(locatedArray[1]), 
                        Double.parseDouble(locatedArray[0]));
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(ExecutionException e){
                e.printStackTrace();
            }
        }
        reportPosition.setText(reportBody);
        reportPosition.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void afterTextChanged(Editable s) {
                title = "";
            }
        });
        
        
   //發送目的地
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,joinCity);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);       
        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){    
                for(int i=0;i<22;i++){
                    if(arg0.getItemAtPosition(i).toString().equals(city)){
                        arg0.setSelection(i);
                        city = "";
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> arg0){
            }
        });
        
       
        
    }
    
    public void clock(View v) {
        Toast.makeText(this, "取消求救", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void submit(View v) {
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
            return;
        }
        
        showCheckSubmitDialog();

    }

    private void sendSMS(String phone, String text) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> messageArray = smsManager.divideMessage(text);
        smsManager.sendMultipartTextMessage(phone, null, messageArray, null,
                null);
    }

    private boolean checkIntrnet() {
        Internet interner = new Internet(General.this);
        if (!interner.isConnectedOrConnecting() && interner.isFailover()) {
            return false;
        }

        return interner.isWanConnect("8.8.8.8");
    }
    
    private void showCheckSubmitDialog(){
        final Dialog dialog = new Dialog(General.this,R.style.MyDialog);//指定自定義樣式
        dialog.setContentView(R.layout.dialogbox);//指定自定義layout

        //可自由調整佈局內部元件的屬性
        message = (TextView) dialog.findViewById(R.id.Message);
        dialogtitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        check = (CheckBox) dialog.findViewById(R.id.checkbox1);
        LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.lldialog);
        ll.getLayoutParams().width=400;
        ll.getLayoutParams().height=650;

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        dialogtitle.setText(Html.fromHtml("您即將以簡訊向" + "<font color=red>" + 
        spinner2.getSelectedItem().toString()+ helper.getText()+"</font>"+"報案"));
        
        if(!Variable.name.equals("")){
            message.setText(Html.fromHtml(title + "<br>" + "我是" + Variable.name + "，這裡發生" + "<font color=red>" +
        spinner1.getSelectedItem().toString() + "</font>" + "，位置是" + "<font color=red>" + 
                    reportPosition.getText() + "</font>" + "。" + "<br>" + "其他資訊:" + reportData.getText())); 
        }else{
            message.setText(Html.fromHtml(title + "<br>" + "這裡發生" + "<font color=red>" +
        spinner1.getSelectedItem().toString() +"</font>" + "，位置是" + "<font color=red>" + 
                    reportPosition.getText() + "</font>" + "。" + "<br>" + "其他資訊:" + reportData.getText())); 
        }
        
       cityPhone = gps.getCityPhoneByName(spinner2.getSelectedItem().toString(), spinner1.getSelectedItemPosition());
        //新增自定義按鈕點擊監聽
        Button btnYes = (Button)dialog.findViewById(R.id.dialog_button_ok);
        btnYes.setOnClickListener(new Button.OnClickListener(){ 
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    Toast.makeText(General.this, "報案訊息連同連絡人一起送出" , Toast.LENGTH_SHORT).show();
                    for (String phone : Variable.contactsPhone) {
                        if (phone.equals("")) {
                            continue;
                        }
                        sendSMS(phone.replaceAll("\\s+", ""),message.getText().toString());
                    }
                    finish();
                }else{
                    Toast.makeText(General.this, "報案訊息送出" + cityPhone, Toast.LENGTH_SHORT).show();
                    sendSMS(cityPhone, message.getText().toString());
                    finish();
                }
            }         

        }); 
        Button btnNo = (Button)dialog.findViewById(R.id.dialog_button_cancel);
        btnNo.setOnClickListener(new Button.OnClickListener(){ 
            @Override
            public void onClick(View v) {
                Toast.makeText(General.this, "報案訊息取消送出", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        //顯示dialog
        dialog.show();


    }

}
