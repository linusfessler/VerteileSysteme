package ch.ethz.inf.vs.a2.resource;

import java.net.URI;

import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by johannes on 19.10.16.
 */

public abstract class Resource {

    private URI uri;

    public Resource(URI uri) {
        this.uri = uri;
    }

    public String handleRequest(ParsedRequest parsedRequest){
        switch (parsedRequest.method){
            case "GET": return get(parsedRequest);
            case "POST": return post(parsedRequest);
        }
        return null;
    }

    public URI getUri() {
        return uri;
    }

    protected abstract String get(ParsedRequest request);
    protected abstract String post(ParsedRequest request);
}
