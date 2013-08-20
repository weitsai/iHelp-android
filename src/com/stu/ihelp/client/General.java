package com.stu.ihelp.client;

import java.util.Calendar;

import org.json.JSONException;

import test.whell.OnWheelChangedListener;
import test.whell.WheelView;
import test.whell.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.stu.tool.Internet;
import com.stu.tool.Json;
import com.stu.tool.Locate;
import com.stu.tool.Police;
import com.stu.tool.SendSMS;

public class General extends Activity {
    String tag = "General";
    TextView data;
    // Wheel
    WheelView join1;
    WheelView join2;
    String[] joindata;
    String[] joindata2;

    Locate gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.general);
        join1 = (WheelView) findViewById(R.id.join1);
        join2 = (WheelView) findViewById(R.id.join2);

        gps = new Locate(General.this);
        setWhellData();

    }

    public void clock(View v) {
        this.finish();
    }

    public void submit(View v) {
        if (gps.canGetLocation()) {

            final String located = gps.getPosition();

            Internet conn = new Internet(General.this);
            if (join1.getCurrentItem() == 0 || join2.getCurrentItem() == 0) {
                Toast.makeText(getBaseContext(), "請選擇災情與人數", Toast.LENGTH_SHORT).show();
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String content = Variable.name + "這裡發生" + joindata[join1.getCurrentItem()] + "，總共有" + joindata2[join2.getCurrentItem()] + "人";

                        new SendSMS(General.this, Variable.contact_phone, content, located);

                        General.this.finish();
                    }
                }.start();
            }

        } else {

            gps.showSettingsAlert();
        }
    }

    private void setWhellData() {
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(join1);
                updateDays(join2);
            }
        };
        Calendar calendar = Calendar.getInstance();
        /*
         * 控制一開始的位子 APRIL為第一個,ALL_STYLES 為第二個,MONTH 為中間值
         */
        int curMonth = calendar.get(Calendar.APRIL);
        joindata = new String[] {
                "請選擇災情", "火災", "鬧事", "身體狀況", "搶劫", "交通事故", "偷竊", "不清楚"
        };
        join1.setViewAdapter(new DateArrayAdapter(this, joindata, 0));
        join1.setCurrentItem(curMonth);

        joindata2 = new String[] {
                "請選擇人數", "未知", "1", "2", "3", "4", "5", "6", "7", "8"
        };
        join2.setViewAdapter(new DateArrayAdapter(this, joindata2, 0));
        join2.setCurrentItem(curMonth);
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

    // 位址轉換並查詢警察局簡訊電話並寄出簡訊
    class addressTransform extends AsyncTask<Void, Void, String> {
        private final String url = "http://maps.google.com/maps/api/geocode/json?sensor=true&language=zh-TW&latlng=";
        private final String smsUrl = "http://maps.google.com/maps?q=";
        private final String key = "&iHELP";
        private String located;

        addressTransform(String located) {
            this.located = located;
            Log.i("URL：" + url, "located：" + located);
        }

        @Override
        protected String doInBackground(Void... arg0) {

            Internet connect = new Internet(General.this);
            String result = connect.GetTo(url + located);
            String country = null;
            try {

                country = new Json().getCountry(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("result", country);
            String phone = new Police().query(country);
            String content = smsUrl + located + key;
            new SendSMS(getBaseContext(), phone, located, content);
            return null;
        }
    }

}
