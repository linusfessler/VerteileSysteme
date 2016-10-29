package ch.ethz.inf.vs.a3.udpclient;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.PriorityQueue;

import ch.ethz.inf.vs.a3.message.ErrorCodes;
import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageComparator;

/**
 * Created by linus on 25.10.2016.
 */

public class RetrieveChatLog extends AsyncTask<ChatActivity, Void, PriorityQueue<Message>> {

    private ChatActivity activity;

    private final static String LOG_TAG = "### RetrieveChatLog";

    @Override
    protected PriorityQueue<Message> doInBackground(ChatActivity... params) {
        activity = params[0];

        PriorityQueue<Message> queue = new PriorityQueue<>(1, new MessageComparator());
        DatagramSocket socket = UDPClient.getSocket();
        try {
            socket.setSoTimeout(NetworkConsts.SOCKET_TIMEOUT);
            while (true) {
                try {
                    byte[] buffer = new byte[NetworkConsts.PAYLOAD_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String response = new String(buffer, 0, packet.getLength());
                    Log.d(LOG_TAG, "Response: " + response);
                    queue.add(new Message(response));
                } catch (SocketTimeoutException te) {
                    Log.d(LOG_TAG, "Socket timeout reached.");
                    break;
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Could not receive registration ack.");
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return queue;
    }

    @Override
    protected void onPostExecute(PriorityQueue<Message> queue) {
        if (queue == null || queue.size() == 0)
            return;

        int i = 0;
        String[] contents = new String[queue.size()];
        for (Message message : queue) {
            if (message.content != null && message.content.matches("^[0-4]{1}")) {
                int errorCode = Integer.parseInt(message.content);
                contents[i++] = "Error " + errorCode + ": " + ErrorCodes.getStringError(errorCode);
            } else
                contents[i++] = message.content;
        }

        ListView list = (ListView) activity.findViewById(R.id.list_chat_log);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.chat_item, contents);
        list.setAdapter(adapter);

        Button button = ((Button) activity.findViewById(R.id.btn_retrieve_chat_log));
        button.setEnabled(true);
        button.setText(R.string.btn_retrieve_chat_log);
    }
}
