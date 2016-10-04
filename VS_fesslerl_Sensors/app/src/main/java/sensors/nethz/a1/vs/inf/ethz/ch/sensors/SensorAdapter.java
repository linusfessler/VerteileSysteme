package sensors.nethz.a1.vs.inf.ethz.ch.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by linus on 03.10.2016.
 */

public class SensorAdapter extends ArrayAdapter<Sensor> {

    private Context context;
    private List<Sensor> sensors;

    public SensorAdapter(Context context, List<Sensor> sensors) {
        super(context, R.layout.list_entry, sensors);
        this.context = context;
        this.sensors = sensors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Sensor sensor = sensors.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View listEntry = inflater.inflate(R.layout.list_entry, parent, false);

        TextView textView = (TextView) listEntry.findViewById(R.id.list_entry_text);
        textView.setText(sensor.getName());

        return listEntry;
    }
}
