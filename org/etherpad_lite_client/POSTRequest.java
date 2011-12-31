package org.etherpad_lite_client;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class POSTRequest implements Request {
    private URL url;

    private String args;

    public POSTRequest(URL url, String args) {
        this.url = url;
        this.args = args;
    }

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
