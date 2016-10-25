package ch.ethz.inf.vs.a3.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linus on 25.10.2016.
 */

public class Message {

    public final String json;

    public Message(String username, String uuid, String timestamp, String type, String content) {
        String json = null;
        try {
            JSONObject header = new JSONObject();
            header.put("username", username);
            header.put("uuid", uuid);
            header.put("timestamp", timestamp);
            header.put("type", type);

            JSONObject body = new JSONObject();
            if (!content.isEmpty())
                body.put("content", content);

            JSONObject root = new JSONObject();
            root.put("header", header);
            root.put("body", body);

            json = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.json = json;
    }
}
