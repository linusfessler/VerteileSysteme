package sensors.nethz.a1.vs.inf.ethz.ch.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
        super(context, R.layout.list_row, sensors);
        this.context = context;
        this.sensors = sensors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_item);
        textView.setText(sensors.get(position).getName());

        return rowView;
    }
}
