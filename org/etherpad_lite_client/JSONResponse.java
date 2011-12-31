package org.etherpad_lite_client;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.*;

public class JSONResponse {

    private String jsonString;

    /**
     * Creates a new JSON Response object.
     * 
     * @param jsonString a valid JSON string
     */
    public JSONResponse(String jsonString) {
        this.jsonString = jsonString;
    }

    /**
     * Converts the JSON string into a HashMap.
     * 
     * @return HashMap
     */
    public HashMap toHashMap() throws JSONException {
        return this.getHashMap(this.jsonString);
    }

    private HashMap getHashMap(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        HashMap map = new HashMap();
        Iterator i = json.keys();
        while (i.hasNext()) {
            String key = (String)i.next();
            String strVal = json.get(key).toString();
            // An object
            if (strVal.matches("\\{.+")) {
                map.put(key, this.getHashMap(strVal));
            }
            // An array
            else if (strVal.matches("\\[.+")) {
                map.put(key, this.getArray(strVal));
            }
            // An int
            else if (strVal.matches("\\d+")) {
                map.put(key, Integer.parseInt(strVal));
            }
            // Assume it's a string
            else {
                map.put(key, strVal);
            }
        }
        return map;
    }

    private Object getArray(String jsonString) throws JSONException {
        JSONArray ja = new JSONArray(jsonString);
        String[] ary = new String[ja.length()];
        for (int i=0; i < ja.length(); i += 1) {
            ary[i] = ja.get(i).toString();
        }
        return ary;
    }
}
