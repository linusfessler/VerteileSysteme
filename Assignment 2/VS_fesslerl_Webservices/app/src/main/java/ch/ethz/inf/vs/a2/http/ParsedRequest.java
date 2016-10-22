package ch.ethz.inf.vs.a2.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by johannes on 19.10.16.
 */

public class ParsedRequest {
    public final String method;
    public final String path;
    public final Map<String,String> header;
    public ParsedRequest(String method, String path){
        this.method = method;
        this.path = path;
        this.header = new HashMap<>();
    }

    public void addHeader(String key, String val){
        header.put(key,val);
    }

}
