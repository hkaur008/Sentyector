package com.example.android_template;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Record_page extends AppCompatActivity {
 Button start ,stop,file_start,file_stop, send_result;
    String path = null;  //Path In Device of audio
    MediaRecorder mediaRecorder ;
    Random random ;
    String FileName = "audioxyz"; // audio file name
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        start= (Button) findViewById(R.id.start);
        stop= (Button) findViewById(R.id.stop);
        file_start= (Button) findViewById(R.id.start_file);
        file_stop= (Button) findViewById(R.id.stop_file);
        send_result= (Button) findViewById(R.id.send_result);

        stop.setEnabled(false);
        file_start.setEnabled(false);
        file_stop.setEnabled(false);



//        myAudioRecorder.prepare();
//        myAudioRecorder.start();

        random = new Random();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()) {


                    path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    path += "/youraudiofile.wav";


                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    start.setEnabled(false);
                    stop.setEnabled(true);

                    Toast.makeText(Record_page.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                stop.setEnabled(false);
                file_start.setEnabled(true);
                start.setEnabled(true);
                file_stop.setEnabled(false);

                Toast.makeText(Record_page.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });
        file_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                stop.setEnabled(false);
                start.setEnabled(false);
                file_stop.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(Record_page.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        file_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop.setEnabled(false);
                start.setEnabled(true);
                file_stop.setEnabled(false);
                file_start.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });


      send_result.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(path!=null)
              {
                  Intent intent = new Intent( Record_page.this, Results.class );
                  intent.putExtra("path", path);
                  startActivity(intent);
              }
          }
      });
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(path);
    }
//    public String CreateRandomAudioFileName(int string){
//        StringBuilder stringBuilder = new StringBuilder( string );
//        int i = 0 ;
//        while(i < string ) {
//            stringBuilder.append(FileName.
//                    charAt(random.nextInt(FileName.length())));
//
//            i++ ;
//        }
//        return stringBuilder.toString();
//    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Record_page.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(Record_page.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Record_page.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
   }


}
