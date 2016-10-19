package ch.ethz.inf.vs.a2.resource;

import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by johannes on 19.10.16.
 */

public abstract class Resource {
    //TODO: Maybe add an "URI" field here..

    public String handleRequest(ParsedRequest parsedRequest){
        switch (parsedRequest.method){
            case "GET": return get(parsedRequest);
            case "POST": return post(parsedRequest);
        }
        return null;
    }

    protected abstract String get(ParsedRequest request);
    protected abstract String post(ParsedRequest request);

}
