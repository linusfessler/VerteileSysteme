package ch.ethz.inf.vs.a2.resource;

import android.util.Log;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by johannes on 19.10.16.
 */

public class RootResource extends Resource {

    private Map<URI, Resource> resourceMap;

    public RootResource(URI uri) {
        super(uri);
        resourceMap = new HashMap<>();
    }

    public void addResource(Resource resource) {
        resourceMap.put(resource.getUri(), resource);
    }

    public Resource getResource(URI uri) {
        return resourceMap.get(uri);
    }

    @Override
    protected String get(ParsedRequest request) {
        if (!request.header.get("Accept").contains("*/*") && !request.header.get("Accept").contains("text/html"))
            return HttpResponse.generateErrorResponse("415 Unsupported Media Type", "Content-Type is text/html.");

        // Forward request to SensorResource
        if (!getUri().equals(request.uri))
            return resourceMap.containsKey(request.uri) ?
                    resourceMap.get(request.uri).handleRequest(request) :
                    HttpResponse.generateErrorResponse("404 Not Found", "Sensor not found");

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Resource resource : resourceMap.values())
            sb.append("<li><a href='")
                    .append(resource.getUri().toString())
                    .append("'>")
                    .append(resource.getUri().toString())
                    .append("</a></li>");
        sb.append("</ul>");

        String content = sb.toString();

        return HttpResponse.generateHtmlResponse("200 OK", content);
    }

    @Override
    protected String post(ParsedRequest request) {
        return HttpResponse.generateErrorResponse("405 Method Not Allowed", "Post not defined for Root Resource.");
    }
}
