package org.etherpad_lite_client;

import java.net.URL;

public interface Request {
    URL url = null;

    public abstract String send() throws Exception;
}
