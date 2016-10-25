package ch.ethz.inf.vs.a3.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linus on 25.10.2016.
 */

public class Message {

    public final String json;
    public final String username;
    public final String uuid;
    public final String timestamp;
    public final String type;
    public final String content;

    // Constructor to encode message
    public Message(String username, String uuid, String timestamp, String type, String content) throws JSONException {
        JSONObject header = new JSONObject();
        header.put("username", username);
        header.put("uuid", uuid);
        if (timestamp.isEmpty())
            timestamp = "{}";
        header.put("timestamp", timestamp);
        header.put("type", type);

        JSONObject body = new JSONObject();
        if (!content.isEmpty())
            body.put("content", content);

        JSONObject root = new JSONObject();
        root.put("header", header);
        root.put("body", body);

        this.json = root.toString();
        this.username = username;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.type = type;
        this.content = content;
    }

    // Constructor to decode message from JSON string
    public Message(String json) throws JSONException {
        this.json = json;
        JSONObject root = new JSONObject(json);

        JSONObject header = root.getJSONObject("header");
        this.username = header.getString("username");
        this.uuid = header.getString("uuid");
        this.timestamp = header.getString("timestamp");
        this.type = header.getString("type");

        // Content only exists for error and text message, will throw exception sometimes
        JSONObject body = root.getJSONObject("body");
        this.content = body.getString("content");
    }
}
