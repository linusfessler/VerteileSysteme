package ch.ethz.inf.vs.a3.clock;

/**
 * Created by linus on 25.10.2016.
 */

public class LamportClock implements Clock {

    private int time;

    @Override
    public void update(Clock other) {
        if (other instanceof LamportClock)
            time = Math.max(getTime(), ((LamportClock) other).getTime());
    }

    @Override
    public void setClock(Clock other) {
        if (other instanceof LamportClock)
            time = ((LamportClock) other).getTime();
    }

    @Override
    public void tick(Integer pid) {
        time++;
    }

    @Override
    public boolean happenedBefore(Clock other) {
        if (other instanceof LamportClock) {
            return getTime() < ((LamportClock) other).getTime();
        }
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(getTime());
    }

    @Override
    public void setClockFromString(String clock) {
        try {
            time = Integer.parseInt(clock);
        } catch (Exception e) {
            // Do nothing
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
