package ch.ethz.inf.vs.a1.fesslerl.sensors;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


/**
 * Created by johannes on 04.10.16.
 */

public class MyGraphContainer implements GraphContainer {

    private final int MAX_DATA_POINTS = 100;

    private GraphView graph;
    private Deque<float[]> vals = new ArrayDeque<>();
    private List<LineGraphSeries<DataPoint>> series = new ArrayList<>();
    private double oldVal = 0.0;

    public MyGraphContainer(GraphView graph, String unit, int numVals){
        this.graph = graph;
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("s");
        graph.getGridLabelRenderer().setVerticalAxisTitle(unit);
        for(int i = 0; i < numVals; ++i) {
            LineGraphSeries<DataPoint> s = new LineGraphSeries<>();
            s.setThickness(6);
            s.setColor(Color.rgb((100+33*i)%255,(150+66*i)%255,(200+i*132)%255));
            series.add(s);
            graph.addSeries(s);
        }
    }

    @Override
    public void addValues(double xIndex, float[] values) {
        if(xIndex > oldVal) { // this is necessary because during testing it sometimes happened, that apparantly the new x-value was not greater than the old one. this leads to an error when appending data.
            while (vals.size() >= MAX_DATA_POINTS)
                vals.removeFirst();
            vals.addLast(values);

            graph.getViewport().setMaxX(xIndex);
            graph.getViewport().setMinX(series.get(0).getLowestValueX());
            for (int i = 0; i < values.length; i++)
                series.get(i).appendData(new DataPoint(xIndex, values[i]), true, MAX_DATA_POINTS);

            oldVal = xIndex;
        }
    }

    @Override
    public float[][] getValues() {
        float[][] res = new float[vals.size()][];
        int i = 0;
        for(float[] v: vals){
            res[i] = v.clone();
            ++i;
        }
        return res;
    }
}
