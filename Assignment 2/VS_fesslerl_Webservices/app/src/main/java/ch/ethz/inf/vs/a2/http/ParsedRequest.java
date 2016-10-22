package ch.ethz.inf.vs.a2.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by johannes on 19.10.16.
 */

public class ParsedRequest {

    public final String method;
    public final URI uri;
    public final Map<String,String> header;
    public final Map<String,String> content;

    public ParsedRequest(String method, URI uri){
        this.method = method;
        this.uri = uri;
        this.header = new HashMap<>();
        this.content = new HashMap<>();
    }

    public void addHeader(String key, String val) {
        header.put(key,val);
    }
    public void addContent(String key, String val) {
        content.put(key,val);
    }
}
