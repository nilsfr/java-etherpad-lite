/**
 * Copyright 2011 Jordan Hollinger
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package src.org.etherpad_lite_client;

import java.util.HashMap;
import java.util.Iterator;

import src.org.json.JSONArray;
import src.org.json.JSONException;
import src.org.json.JSONObject;

/**
 * Uses org.json (json.org/java) to convert JSON strings to JSONObject and JSONArray objects,
 * then converts those into HashMap and array objects.<br />
 * <br />
 * The idea is that these "native" object types are more expected and familiar. Additionally,
 * the particular JSON parser we use is an implementation detail, and shouldn't be exposed 
 * to the end-user. If we ever change JSON libs, it shouldn't break user code.
 */
public class JSONResponse {

    /**
     * The full string of JSON
     */
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

    /**
     * Converts a string like "{a: 'b', c: 'd'}" into a HashMap.
     * 
     * @param jsonString a string of JSON representing a key-value pair
     * @return HashMap
     */
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
            // A long
            else if (strVal.matches("\\d+")) {
                map.put(key, Long.valueOf(strVal));
            }
            // A boolean
            else if (strVal.matches("(true)|(false)")) {
                map.put(key, Boolean.parseBoolean(strVal));
            }
            // Assume it's a string
            else {
                map.put(key, strVal);
            }
        }
        return map;
    }

    /**
     * Converts a string like "['foo', 'bar']" into an array of strings.
     * 
     * @param jsonString a string of JSON representing an array
     * @return String[]
     */
    private String[] getArray(String jsonString) throws JSONException {
        JSONArray ja = new JSONArray(jsonString);
        String[] ary = new String[ja.length()];
        for (int i=0; i < ja.length(); i += 1) {
            ary[i] = ja.get(i).toString();
        }
        return ary;
    }
}
