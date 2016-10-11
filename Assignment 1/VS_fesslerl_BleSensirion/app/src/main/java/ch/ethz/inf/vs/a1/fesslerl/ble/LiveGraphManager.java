package ch.ethz.inf.vs.a1.fesslerl.ble;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.graphics.Color;

import java.util.Calendar;

/**
 * Created by markus on 10.10.16.
 */

public class LiveGraphManager {

    private final int MAX_DATA_POINTS_TEMP = 100;
    private final int MAX_DATA_POINTS_HUM = 100;

    private GraphView graphTemp;
    private GraphView graphHum;
    private LineGraphSeries<DataPoint> lgSeriesTemperature;
    private LineGraphSeries<DataPoint> lgSeriesHumidity;
    private Calendar calendar;
    private long startTime;

    LiveGraphManager(GraphView temperatureGraph, GraphView humidityGraph){
        // Initialize GraphViews
        graphTemp = temperatureGraph;
        graphHum = humidityGraph;

        // Initialize Calendar for updating values easily
        calendar = Calendar.getInstance();
//        startTime = calendar.getTimeInMillis();

        initTemperatureGraph();
        initHumidityGraph();
    }

    // TODO: finish this init function
    private void initTemperatureGraph(){

        // Set axis titles
        graphTemp.getGridLabelRenderer().setVerticalAxisTitle("C");
        graphTemp.getGridLabelRenderer().setHorizontalAxisTitle("s");

        // create LineGraphSeries for TemperatureGraph
        lgSeriesTemperature = new LineGraphSeries<>();
        lgSeriesTemperature.setThickness(6);
        lgSeriesTemperature.setColor(Color.RED);

        graphTemp.addSeries(lgSeriesTemperature);
    }

    private void initHumidityGraph(){

        // Humidity can only be between 0 and 100%
        // TODO: Check if bounds are correct. (Does the sensor get values between 0 and 1 or 0 and 100, etc.?
        graphHum.getViewport().setYAxisBoundsManual(true);
        graphHum.getViewport().setMinY(0.0);
        graphHum.getViewport().setMaxY(100.0);

        // Set axis titles
        graphHum.getGridLabelRenderer().setVerticalAxisTitle("%");
        graphHum.getGridLabelRenderer().setHorizontalAxisTitle("s");

        // create LineGraphSeries for HumidityGraph
        lgSeriesHumidity = new LineGraphSeries<>();
        lgSeriesHumidity.setThickness(6);
        lgSeriesHumidity.setColor(Color.BLUE);

        graphHum.addSeries(lgSeriesHumidity);
    }

    // TODO: finish this update function
    private void updateTemperatureGraph(double val){
//        lgSeriesTemperature.appendData(new DataPoint(calendar.getTimeInMillis() - startTime, val), true, MAX_DATA_POINTS_TEMP);

        // According to the documentation, DataPoint can handle Date Objects.
        lgSeriesTemperature.appendData(new DataPoint(calendar.getTime(), val), true, MAX_DATA_POINTS_TEMP);
    }

    private void updateHumidityGraph(double val){
//        lgSeriesHumidity.appendData(new DataPoint(calendar.getTimeInMillis() - startTime, val), true, MAX_DATA_POINTS_HUM);

        // Accordig to the documentation, DataPoint can handle Date Objects.
        lgSeriesHumidity.appendData(new DataPoint(calendar.getTime(), val), true, MAX_DATA_POINTS_HUM);
    }

}
