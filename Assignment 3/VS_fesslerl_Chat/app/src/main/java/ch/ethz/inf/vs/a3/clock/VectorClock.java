package ch.ethz.inf.vs.a3.clock;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linus on 25.10.2016.
 */

public class VectorClock implements Clock{

    // Process ID => logical time
    private Map<Integer, Integer> vector;

    public VectorClock() {
        vector = new HashMap<>();
    }

    @Override
    public void update(Clock other) {
        if (!(other instanceof VectorClock))
            return;

        VectorClock clock = (VectorClock) other;
        for (int pid : clock.vector.keySet())
            if (!vector.containsKey(pid) ||
                    (vector.containsKey(pid) && vector.get(pid) < clock.vector.get(pid)))
                vector.put(pid, clock.vector.get(pid));
    }

    @Override
    public void setClock(Clock other) {
        if (other instanceof VectorClock)
            vector = new HashMap<>(((VectorClock) other).vector);
    }

    @Override
    public void tick(Integer pid) {
        if (vector.containsKey(pid))
            vector.put(pid, vector.get(pid) + 1);
    }

    @Override
    public boolean happenedBefore(Clock other) {
        if (!(other instanceof VectorClock))
            return false;

        VectorClock clock = (VectorClock) other;
        for (int pid : vector.keySet()) {
            if (!clock.vector.containsKey(pid) || vector.get(pid) > clock.vector.get(pid))
                return false;
        }

        return !vector.equals(clock.vector);
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        for (Map.Entry<Integer, Integer> entry : vector.entrySet()) {
            try {
                json.put(entry.getKey().toString(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json.toString();
    }

    @Override
    public void setClockFromString(String clock) {
        try {
            Map<Integer, Integer> vector = new HashMap<>();
            JSONObject json = new JSONObject(clock);
            JSONArray names = json.names();
            if (names != null) {
                for (int i = 0; i < names.length(); i++) {
                    String name = names.getString(i);
                    vector.put(Integer.parseInt(name), json.getInt(name));
                }
            }
            this.vector = vector;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getTime(Integer pid) {
        if (vector.containsKey(pid))
            return vector.get(pid);
        return -1;
    }

    public void addProcess(Integer pid, int time) {
        if (!vector.containsKey(pid))
            vector.put(pid, time);
    }
}
