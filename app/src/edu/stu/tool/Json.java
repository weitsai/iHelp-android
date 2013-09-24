package edu.stu.tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Json {
    private JSONObject obj;
    private JSONArray arr;
    private String result;

    /**
     * JsonParser
     * 
     * @param jsonText
     * @return
     * @throws JSONException
     */
    public String getCountry(String jsonText) throws JSONException {

        obj = new JSONObject(jsonText);
        arr = obj.getJSONArray("results");
        obj = arr.getJSONObject(0);
        Log.e("address_components", obj.getString("formatted_address"));
        return obj.getString("formatted_address");
    }
}
