package com.example.android_template;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class TextSpeech extends AppCompatActivity {
        private final int REQ_CODE = 100;
        TextView textView;
         ImageView speak;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_textspeech);
            textView = (TextView)findViewById(R.id.text);
           speak = (ImageView) findViewById(R.id.speak);


            speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                    try {
                        startActivityForResult(intent, REQ_CODE);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry your device not supported",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQ_CODE: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        textView.setText((CharSequence) result.get(0));

                        Intent intent = new Intent( TextSpeech.this, Results.class );
                        intent.putExtra("passage", textView.getText());
                        startActivity(intent);
                    }
                    break;
                }
            }
        }
    }
