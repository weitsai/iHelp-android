package edu.stu.ihelp.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import test.whell.OnWheelChangedListener;
import test.whell.OnWheelScrollListener;
import test.whell.WheelView;
import test.whell.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.stu.tool.Internet;
import edu.stu.tool.Locate;

public class General extends Activity {
    private EditText reportData;
    // Wheel
    private WheelView join1;
    private String[] joindata;

    private SpannableStringBuilder reportBody = null;
    private String address = "";

    private Locate gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.general);
        join1 = (WheelView) findViewById(R.id.join1);
        reportData = (EditText) findViewById(R.id.report_data);

        join1.addScrollingListener(new OnWheelScrollListener() {

            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                reportBody = new SpannableStringBuilder();
                int titleLength = 0;

                if (!Variable.name.equals("")) {
                    reportBody.append("我是" + Variable.name);
                }

                titleLength = reportBody.length();

                if (join1.getCurrentItem() == 0) {
                    reportBody.append("發生緊急狀況。");
                    reportBody.setSpan(new ForegroundColorSpan(Color.RED),
                            titleLength + 2, reportBody.length() - 1,
                            Spanned.SPAN_COMPOSING);
                } else {
                    reportBody.append("發生" + joindata[join1.getCurrentItem()]
                            + "。");
                    reportBody.setSpan(new ForegroundColorSpan(Color.RED),
                            titleLength + 2, reportBody.length() - 1,
                            Spanned.SPAN_COMPOSING);
                }

                if (!address.equals("")) {
                    reportBody.append("\n" + address);
                }

                reportData.setText(reportBody);
            }
        });

        reportData.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void afterTextChanged(Editable s) {
                String temp = reportData.getText().toString();
                reportBody = new SpannableStringBuilder();
                reportBody.append(temp);
            }
        });

        gps = new Locate(General.this);
        setWhellData();

        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        }

        if (checkIntrnet() && gps.canGetLocation()) {
            try {
                address = gps.getAddress();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

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

        final String located = gps.getPosition();
        String title = "http://maps.google.com.tw/maps?q=" + located
                + "&GeoSMS=iHELP\n";

        String locatedArray[] = located.split(",");
        String cityPhone = gps.getCityPhone(
                Double.parseDouble(locatedArray[1]),
                Double.parseDouble(locatedArray[0]));

        // sendSMS(cityPhone, title + reportBody);

        for (String phone : Variable.contactsPhone) {
            if (phone.equals("")) {
                continue;
            }
            sendSMS(phone.replaceAll("\\s+", ""), title + reportBody);

        }

        General.this.finish();

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

    private void setWhellData() {
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(join1);
            }
        };
        Calendar calendar = Calendar.getInstance();
        /*
         * 控制一開始的位子 APRIL為第一個,ALL_STYLES 為第二個,MONTH 為中間值
         */
        int curMonth = calendar.get(Calendar.APRIL);
        joindata = new String[] { "請選擇災情", "火災", "鬧事", "身體狀況", "搶劫", "交通事故",
                "偷竊" };
        join1.setViewAdapter(new DateArrayAdapter(this, joindata, 0));
        join1.setCurrentItem(curMonth);

    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView join) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, join.getCurrentItem());
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.w("Data", "" + join.getCurrentItem());
    }

    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(20);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setGravity(Gravity.CENTER);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

}
