package ch.ethz.inf.vs.a1.fesslerl.ble;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by markus on 10.10.16.
 */

public class LiveGraphManager {

    private final int MAX_DATA_POINTS_TEMP = 100;
    private final int MAX_DATA_POINTS_HUM = 100;
    private final double TEMP_Y_AXIS_ADD = 2.0;
    private final String UNIT_TEMP = "°C";
    private final String UNIT_HUM = "%";


    private GraphView graphTemp;
    private GraphView graphHum;
    private LineGraphSeries<DataPoint> seriesTemp;
    private LineGraphSeries<DataPoint> seriesHum;
    private Calendar calendar;
    private long startTime;

    LiveGraphManager(GraphView graphTemp, GraphView graphHum){
        // Initialize GraphViews
        this.graphTemp = graphTemp;
        this.graphHum = graphHum;

        // Initialize Calendar for updating values easily
        calendar = Calendar.getInstance();
        startTime = calendar.getTimeInMillis();

        initTempGraph();
        initHumGraph();
    }

    // TODO: finish this init function
    private void initTempGraph(){
        seriesTemp = new LineGraphSeries<>();
        initGraph(graphTemp, seriesTemp, UNIT_TEMP, Color.RED);
        graphTemp.getViewport().setYAxisBoundsManual(true);
        graphTemp.getViewport().setMinY(0.);
        graphTemp.getViewport().setMaxX(30.);
    }

    private void initHumGraph(){
        // TODO: Check if bounds are correct. (Does the sensor get values between 0 and 1 or 0 and 100, etc.?
        // Humidity can only be between 0 and 100%
        graphHum.getViewport().setYAxisBoundsManual(true);
        graphHum.getViewport().setMinY(0.0);
        graphHum.getViewport().setMaxY(100.0);

        seriesHum = new LineGraphSeries<>();
        initGraph(graphHum, seriesHum, UNIT_HUM, Color.BLUE);
    }

    private void initGraph(GraphView graph, LineGraphSeries series, String unit, int color) {
        // Set axis titles
        graph.getGridLabelRenderer().setVerticalAxisTitle(unit);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("s");

        // Needed for "scroll to end"
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);

        // Modify LineGraphSeries
        series.setThickness(6);
        series.setColor(color);

        graph.addSeries(series);
    }

    public void updateTempGraph(double val, TextView textView) {
        updateGraph(graphTemp, seriesTemp, val, MAX_DATA_POINTS_TEMP, textView, "Temperature: ", UNIT_TEMP);
        if(!seriesTemp.isEmpty()){
            graphTemp.getViewport().setMinY(seriesTemp.getLowestValueY() - TEMP_Y_AXIS_ADD);
            graphTemp.getViewport().setMaxY(seriesTemp.getHighestValueY() + TEMP_Y_AXIS_ADD);
        }
    }

    public void updateHumGraph(double val, TextView textView) {
        updateGraph(graphHum, seriesHum, val, MAX_DATA_POINTS_HUM, textView, "Humidity: ", UNIT_HUM);
    }

    private void updateGraph( GraphView graph, LineGraphSeries series, double val, int max, TextView textView, String text, String unit) {
        double time = (Calendar.getInstance().getTimeInMillis()- startTime)/1000.;
        if(!series.isEmpty())
            graph.getViewport().setMinX(series.getLowestValueX());
        graph.getViewport().setMaxX(time);
        series.appendData(new DataPoint(time, val), true, max);

        DecimalFormat format = new DecimalFormat("#0.00;-#0.00");
        textView.setText(text + format.format(val) + " " + unit);
    }
}
