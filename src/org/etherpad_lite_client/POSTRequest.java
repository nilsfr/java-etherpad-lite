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

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A class for easily executing an HTTP POST request.<br />
 * <br />
 * Example:<br />
 * <br />
 * <code>
 * Request req = new POSTRequest(url_object);<br />
 * String resp = req.send();<br />
 * </code>
 */
public class POSTRequest implements Request {
    /**
     * The URL object.
     */
    private URL url;

    /**
     * Instantiates a new POSTRequest.
     * 
     * @param url the URL object
     */
    private String args;

    public POSTRequest(URL url, String args) {
        this.url = url;
        this.args = args;
    }

    /**
     * Sends the request and returns the response.
     * 
     * @return String
     */
    public String send() throws Exception {
        URLConnection con = this.url.openConnection();
        con.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        out.write(this.args);
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = "";
        String buffer;
        while ((buffer = in.readLine()) != null) {
            response += buffer;
        }
        in.close();
        return response;
    }
}
