package com.example.android_template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView email = (TextView)findViewById(R.id.email);
        TextView name = (TextView)findViewById(R.id.name);
        Button record = (Button)findViewById(R.id.button);
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);



        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();

            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Picasso.with(this).load(personPhoto).into(imageView2);
            email.setText(personEmail);
            name.setText(personName.toUpperCase());
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this , TextSpeech.class);
                startActivity(intent);
            }
        });




    }




}