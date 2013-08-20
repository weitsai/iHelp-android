package com.stu.ihelp.client;

public class Variable {
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
