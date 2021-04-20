package com.example.android_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;


public class Results extends AppCompatActivity {
    TextView result_analysis, percentage, date, date_result, time, time_result, result, result_result;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference, responseReference;
    // Instantiate the RequestQueue.
    Context context;
    RequestQueue requestQueue;
    String url = "https://sentyectorapi.herokuapp.com/api/predemotions/";
    JSONObject jsonBody = new JSONObject();
    String passage = null;
    Button graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        result_analysis = (TextView) findViewById(R.id.result_analysis);
        percentage = (TextView) findViewById(R.id.PERCENTAGE);
        date = (TextView) findViewById(R.id.date);
        date_result = (TextView) findViewById(R.id.date_result);
        time = (TextView) findViewById(R.id.Time);
        time_result = (TextView) findViewById(R.id.Time_result);
        result = (TextView) findViewById(R.id.Result);
        result_result = (TextView) findViewById(R.id.Result_result);

        context = getApplicationContext();

        Bundle extras = getIntent().getExtras();
        passage = extras.getString("passage");
        requestQueue = Volley.newRequestQueue(this);

        graph = (Button) findViewById(R.id.graph);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        graph.setEnabled(false);
        senti_analysis(passage);




    }


    @IgnoreExtraProperties
    public class Report {

        public String yyyy;
        public String mm;
        public String dd;
        public String time;
        public Date date;
        public String response;

        public Report() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Report(Date date, String response) {
            DateFormat yearformat = new SimpleDateFormat("yyyy");
            DateFormat monthFormat = new SimpleDateFormat("MM");
            DateFormat dayformat = new SimpleDateFormat("dd");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            this.yyyy = yearformat.format(date);
            this.mm = monthFormat.format(date);
            this.dd = dayformat.format(date);
            this.time = timeFormat.format(date);
            this.response = response;
        }

    }

    public void put_data(Date Time, String response) {

        Report report = new Report(Time, response);
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        responseReference = mDatabaseReference.child("report").child(dateFormat.format(Time));
//        DateFormat yearformat = new SimpleDateFormat("yyyy");
//        DateFormat monthFormat = new SimpleDateFormat("MM");
//        DateFormat dayformat = new SimpleDateFormat("dd");
//        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//        responseReference = mDatabaseReference.child("report").child(yearformat.format(Time)).child(monthFormat.format(Time)).child(dayformat.format(Time)).child(timeFormat.format(Time));
//        responseReference = mDatabaseReference.child("report");

        responseReference = mDatabaseReference.child("report").child("results").child(String.valueOf(System.currentTimeMillis()));
        responseReference.setValue(response);
        retrive();
    }

    public void retrive() {


        mDatabaseReference.child("report").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                   percentage.setText("error");
                } else {

                    Object object = task.getResult().getValue(Object.class);

                    String json = new Gson().toJson(object);
                    try {
                        JSONObject obj = new JSONObject(json);
                        JSONObject res = obj.getJSONObject("results");

                        Iterator<String> it = res.keys();

                        ArrayList<OneItem> list = new ArrayList<>();

                        while (it.hasNext()) {

                            String id = it.next();
                            list.add(new OneItem(id, res.getString(id)));

                        }
                        int  positive=0, present=0;
                        Collections.sort(list);
                        StringBuilder totime = new StringBuilder();
                        StringBuilder toresponse = new StringBuilder();
                        StringBuilder todate = new StringBuilder();

                         int array [] = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            todate.append(list.get(i).date+"\n");
                            totime.append(list.get(i).time+"\n");
                            toresponse.append(list.get(i).response+"\n");
                            Log.d("testing", "onComplete: "+list.get(i).response+"\n"+positive);
                            if(list.get(i).response.equals("positive"))
                            {
                                positive++;
                              present++;
                            }
                            else{
                                present--;
                            }
                            array[ list.size()-i-1]=(present*100)/(i+1);
                            Log.d("array", "onComplete: "+array[i]);
                        }


                        result_result.setText(toresponse.toString());
                        date_result.setText(todate.toString());
                        time_result.setText(totime.toString());
                        percentage.setText("positive: "+positive+"\n" +((positive*100) /list.size() )+"% ");
                        graph.setEnabled(true);
                        graph.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Results.this,Graph.class);
                                intent.putExtra("GraphArray", array);
                                startActivity(intent);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //                   buildgraph(json);
                }
            }
        });


    }

    public void buildgraph(String json) {


    }


    public void senti_analysis(String text_result) {
        try {
            jsonBody.put("textopredict", text_result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String prediction = null;
                try {
                    prediction = response.getString("prediction");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result_analysis.setText( text_result + "  \n\n\n  Prediction :" + prediction.toUpperCase());

                Date dateObj = Calendar.getInstance().getTime();
                put_data(dateObj, prediction);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}