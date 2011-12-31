package org.etherpad_lite_client;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GETRequest implements Request {
    private URL url;

    public GETRequest(URL url) {
        this.url = url;
    }

    public String send() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String response = "";
        String buffer;
        while ((buffer = in.readLine()) != null) {
            response += buffer;
        }
        in.close();
        return response;
    }
}
