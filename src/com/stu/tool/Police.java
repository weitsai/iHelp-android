package com.stu.tool;

import java.util.ArrayList;
import java.util.HashMap;

public class Police {

    private ArrayList<HashMap<String, String>> police;

    public Police() {
        police = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> number = new HashMap<String, String>();
        number.put("台北市", "0922222222");
        number.put("新北市", "0933333333");
        number.put("台中市", "0944444444");
        number.put("高雄市", "0955555555");
        police.add(number);
    }

    public String query(String Counties) {
        String result = null;
        for (int i = 0; i < police.size(); i++) {

            result = police.get(i).get(Counties);
            if (result.equals(Counties))
                break;
        }
        return result;
    }
}
