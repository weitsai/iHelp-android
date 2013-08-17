package com.stu.tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		arr = obj.getJSONArray("address_components");
		obj = arr.getJSONObject(3);
		result = obj.getString("long_name");
		return result;
	}

}
