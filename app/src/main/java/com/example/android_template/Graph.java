package com.example.android_template;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        GraphView graph = findViewById(R.id.graph);

        graph.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graph.getViewport().setScrollable(true);  // activate horizontal scrolling
        graph.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScrollableY(true);  // activate vertical scrolling
        Bundle myBundle = getIntent().getExtras();
        int[] array = myBundle.getIntArray("GraphArray");


        DataPoint[] dataPoints = new DataPoint[array.length]; // declare an array of DataPoint objects with the same size as your list
        for (int i = 0; i < array.length; i++) {
            // add new DataPoint object to the array for each of your list entries
            dataPoints[i] = new DataPoint(i,array[i] ); // not sure but I think the second argument should be of type double
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints); // This one should be obvious right? :)
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
        graph.addSeries(series);

    }
}