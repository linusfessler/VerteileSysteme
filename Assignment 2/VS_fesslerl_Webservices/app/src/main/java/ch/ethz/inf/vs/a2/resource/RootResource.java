package ch.ethz.inf.vs.a2.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by johannes on 19.10.16.
 */

public class RootResource extends Resource {

    private List<URI> resources;

    public RootResource(){
        resources = new ArrayList<>();
    }

    public void addResource(URI uri){
        resources.add(uri);
    }

    @Override
    protected String get(ParsedRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append("<ul>");
        for(URI uri : resources){
            sb.append("<li><a href='").append(uri.toString()).append("'>").append(uri.toString()).append("</a></li>");
        }
        sb.append("</ul>");

        String content = sb.toString();

        return HttpResponse.generateHtmlResponse(content);
    }

    @Override
    protected String post(ParsedRequest request) {
        return HttpResponse.generateErrorResponse("Post not defnied for Sensor Resource");
    }
}
