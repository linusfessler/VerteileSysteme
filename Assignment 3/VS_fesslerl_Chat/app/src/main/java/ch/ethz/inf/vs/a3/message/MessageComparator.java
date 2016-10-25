package ch.ethz.inf.vs.a3.message;

import java.util.Comparator;

import ch.ethz.inf.vs.a3.clock.VectorClock;

/**
 * Message comparator class. Use with PriorityQueue.
 */
public class MessageComparator implements Comparator<Message> {

    @Override
    public int compare(Message message1, Message message2) {
        VectorClock clock1 = new VectorClock();
        VectorClock clock2 = new VectorClock();
        clock1.setClockFromString(message1.timestamp);
        clock2.setClockFromString(message2.timestamp);
        return clock1.happenedBefore(clock2) ? -1 : 1;
    }

}
