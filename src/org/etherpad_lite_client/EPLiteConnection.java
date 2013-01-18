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

package org.etherpad_lite_client;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.*;

/**
 * Connection object for talking to and parsing responses from the Etherpad Lite Server.
 */
public class EPLiteConnection {
    public static final int CODE_OK = 0;
    public static final int CODE_INVALID_PARAMETERS = 1;
    public static final int CODE_INTERNAL_ERROR = 2;
    public static final int CODE_INVALID_METHOD = 3;
    public static final int CODE_INVALID_API_KEY = 4;

    /**
     * The url of the API
     */
    public URI uri;

    /**
     * The API key
     */
    public String apiKey;

    /**
     * The Etherpad Lite API version
     */
    public String apiVersion;

    /**
     * Initializes a new org.etherpad_lite_client.EPLiteConnection object.
     *
     * @param url an absolute url, including protocol, to the EPL api
     * @param apiKey the API Key
     * @param apiVersion the API version
     */
    public EPLiteConnection(String url, String apiKey, String apiVersion) {
        this.uri = URI.create(url);
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
    }

    /**
     * GETs from the HTTP JSON API.
     * 
     * @param apiMethod the name of the API method to call
     * @return HashMap
     */
    public HashMap get(String apiMethod) {
      return this.call(apiMethod, new HashMap(), "GET");
    }

    /**
     * GETs from the HTTP JSON API.
     * 
     * @param apiMethod the name of the API method to call
     * @param apiArgs a HashMap of url/form parameters. apikey will be set automatically
     * @return HashMap
     */
    public HashMap get(String apiMethod, HashMap apiArgs) {
      return this.call(apiMethod, apiArgs, "GET");
    }

    /**
     * POSTs to the HTTP JSON API.
     * 
     * @param apiMethod the name of the API method to call
     * @return HashMap
     */
    public HashMap post(String apiMethod) {
      return this.call(apiMethod, new HashMap(), "POST");
    }

    /**
     * POSTs to the HTTP JSON API.
     * 
     * @param apiMethod the name of the API method to call
     * @param apiArgs a HashMap of url/form parameters. apikey will be set automatically
     * @return HashMap
     */
    public HashMap post(String apiMethod, HashMap apiArgs) {
      return this.call(apiMethod, apiArgs, "POST");
    }

    /**
     * Calls the HTTP JSON API.
     * 
     * @param apiMethod the name of the API method to call
     * @param apiArgs a HashMap of url/form parameters. apikey will be set automatically
     * @param httpMethod the HTTP method to use ("GET" or "POST")
     * @return HashMap
     */
    private HashMap call(String apiMethod, HashMap apiArgs, String httpMethod) {
        // Build the url params
        String strArgs = "";
        apiArgs.put("apikey", this.apiKey);
        Iterator i = apiArgs.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry)i.next();
            Object value = e.getValue();
            if (value != null) {
                strArgs += e.getKey() + "=" + value;
                if (i.hasNext()) {
                  strArgs += "&";
                }
            }
        }
        
        trustServerAndCertificate();

        // Execute the API call
        String path = this.uri.getPath() + "/api/" + this.apiVersion + "/" + apiMethod;
        URL url;
        Request request;
        try {
            // A read (get) request
            if (httpMethod == "GET") {
                url = new URL(new URI(this.uri.getScheme(), null, this.uri.getHost(), this.uri.getPort(), path, strArgs, null).toString());
                request = new GETRequest(url);
            }
            // A write (post) request
            else if (httpMethod == "POST") {
                url = new URL(new URI(this.uri.getScheme(), null, this.uri.getHost(), this.uri.getPort(), path, null, null).toString());
                request = new POSTRequest(url, strArgs);
            }
            else {
                throw new EPLiteException(httpMethod + " is not a valid HTTP method");
            }
            return this.handleResponse(request.send());
        }
        catch (EPLiteException e) {
            throw new EPLiteException(e.getMessage());
        }
        catch (Exception e) {
            throw new EPLiteException("Unable to connect to Etherpad Lite instance (" + e.getClass() + "): " + e.getMessage());
        }
    }

    /**
     * Converts the API resonse's JSON string into a HashMap.
     * 
     * @param jsonString a valid JSON string
     * @return HashMap
     */
    private HashMap handleResponse(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            // Act on the response code
            if (!json.get("code").equals(null)) {
                switch ((Integer)json.get("code")) {
                    // Valid code, parse the response
                    case CODE_OK:
                        if (!json.get("data").equals(null)) {
                            return new JSONResponse(json.get("data").toString()).toHashMap();
                        } else {
                            return new HashMap();
                        }
                    // Invalid code, throw an exception with the message
                    case CODE_INVALID_PARAMETERS:
                    case CODE_INVALID_API_KEY:
                    case CODE_INVALID_METHOD:
                        throw new EPLiteException((String)json.get("message"));
                    default:
                        throw new EPLiteException("An unknown error has occurred while handling the response: " + jsonString);
                }
            // No response code, something's really wrong
            } else {
                throw new EPLiteException("An unknown error has occurred while handling the response: " + jsonString);
            }
        } catch (JSONException e) {
            System.err.println("Unable to parse JSON response (" + jsonString + "): " + e.getMessage());
            return new HashMap();
        }
    }

    /**
     * Creates a trust manager to trust all certificates if you open a ssl connection
     */
	private void trustServerAndCertificate() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
		
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
		
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
}
