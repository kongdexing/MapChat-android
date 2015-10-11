package cn.gdeveloper.mapchat.http.request;

import java.util.Map;

public interface BaseHttpRequestParameters {

    public String getBodyContent();
    public BaseHttpRequestParameters setBodyContent(String bodyContent);
    // add the request headers.
    public BaseHttpRequestParameters addRequestProperty(String key, String value);
    public BaseHttpRequestParameters clearRequestProperty();
    public Map<String, String> getRequestPropreties();

}
