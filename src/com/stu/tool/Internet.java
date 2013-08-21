package com.stu.tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Internet {
    private NetworkInfo info = null;

    public Internet(Context context) {
        ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = CM.getActiveNetworkInfo();
    }

    /**
     * 目前網路是否可使用
     * 
     * @param context
     *            必要參數
     * @return true 為可以使用
     */
    boolean isAvailable() {
        if (info == null) {
            Log.w("intent", "null");
            return false;
        }
        return info.isAvailable();
    }

    /**
     * 網路是否已連接
     * 
     * @param context
     *            必要參數
     * @return true 為已連線
     */
    public boolean isConnected() {
        if (info == null) {
            Log.w("intent", "null");
            return false;
        }
        return info.isConnected();
    }

    /**
     * 網路是否已連接或連線中
     * 
     * @param context
     *            必要參數
     * @return true 為連線或連接忠
     */
    public boolean isConnectedOrConnecting() {
        if (info == null) {
            Log.w("intent", "null");
            return false;
        }
        return info.isConnectedOrConnecting();
    }

    /**
     * 網路目前是否有問題
     * 
     * @param context
     *            必要參數
     * @return false 為沒有問題
     */
    public boolean isFailover() {
        if (info == null) {
            Log.w("intent", "null");
            return false;
        }
        return info.isFailover();
    }

    /**
     * 判斷網路是否擁有對外連線能力
     * 
     * @param context
     *            必要參數
     * @return false 為沒有問題
     */
    public Boolean isWanConnect(String str) {
        Boolean connent = false;
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 " + str);
            int status = p.waitFor();
            if (status == 0) {
                connent = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connent;
    }

    /**
     * 判斷是否有開啟網路連線機制
     * 
     * @return true 有開啟 3G / wifi
     */
    public Boolean checkInternet() {
        if (info == null) {
            return false;
        }
        String status = info.getTypeName();
        String wifi = "WIFI";
        // 3G
        String imt2000 = "mobile";

        if (status.equals(wifi)) {
            return true;
        }

        if (status.equals(imt2000)) {
            return true;
        }
        return false;
    }

    /**
     * Post data of key value to URL
     * 
     * @param key
     * @param data
     * @param URL
     * @return result for URL
     */
    public String toPost(String[] key, String[] data, String URL) {
        try {

            List<NameValuePair> mList = new ArrayList<NameValuePair>();

            for (int i = 0; i < key.length; i++) {
                mList.add(new BasicNameValuePair(key[i], data[i]));
            }

            HttpClient client = new DefaultHttpClient();

            HttpPost httpRequest = new HttpPost(URL);

            httpRequest.setEntity(new UrlEncodedFormEntity(mList, HTTP.UTF_8));

            HttpResponse httpResponse = client.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                String result = EntityUtils.toString(httpResponse.getEntity());
                return result;
            } else {
                return "connect status error,now status：" + httpResponse.getStatusLine().getStatusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";

    }

    /**
     * Get data to server
     * 
     * @param URL
     * @return read data for server
     */
    public String toGet(String URL) {
        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpRequest = new HttpPost(URL);

            HttpResponse httpResponse = client.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                return result;
            } else
                return "connect status error";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
