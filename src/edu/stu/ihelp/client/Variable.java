package edu.stu.ihelp.client;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Variable {
    private static SharedPreferences spfs;
    public static final String FILENAME = "data";
    public static final String NAME = "name";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_PHONE = "contact_phone";
    public static final String[] contactArray = { "1st", "2nd", "3rd", "4th",
            "5th" };
    public static final int CONTACT_MAX = 5;

    static String name = "";
    static String gender = "";
    static String phome = "";
    static String idNumbet = "";
    static String disaster = "";
    static String diStringNumber = "";
    static String Address = "";
    static String contact = "";
    static String contact_phone = "";
    static String location = "";

    static String web_id;

    // 緯度
    static String Latitude = "";
    // 經度
    static String Longitude = "";

    static void setData(Context context, List<Map<String, String>> data) {
        spfs = context.getSharedPreferences(Variable.FILENAME,
                Context.MODE_PRIVATE);
        for (int i = 0; i < Variable.CONTACT_MAX; i++) {

            if (data.size() == i) {
                break;
            } else {
                spfs.edit()
                        .putString(contactArray[i],
                                data.get(i).get(CONTACT_PHONE)).commit();
            }

            if (i == 0) {
                contact_phone = data.get(i).get(Variable.CONTACT_PHONE);
            }
            Log.e("set_name:" + data.get(i).get(Variable.CONTACT_NAME),
                    "set_phone:" + data.get(i).get(Variable.CONTACT_PHONE));
            Log.i("now", i + "");
        }

    }

    static void getData(Context context) {
        spfs = context.getSharedPreferences(Variable.FILENAME,
                Context.MODE_PRIVATE);

        Variable.name = spfs.getString(Variable.NAME, "");
        Log.e("name", spfs.getString("name", ""));
        Variable.contact_phone = spfs.getString(Variable.CONTACT_PHONE, "");
        Log.e("phone", spfs.getString("contact_phone", ""));
    }

    static boolean existData() {
        if (Variable.name.equals("")) {
            return false;
        } else if (Variable.contact_phone.equals("")) {
            return false;
        }
        return true;
    }

    static boolean checkData() {
        if (name.equals("") || idNumbet.equals("") || contact.equals("")) {
            return false;
        }
        return true;
    }

    static void getAllData() {
        System.out.println(name + ", " + idNumbet + ", " + contact);
    }
}
