package com.hnb.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.Observable;

public class MainActivity extends AppCompatActivity
{

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btn.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this  , "sdsdsd", Toast.LENGTH_LONG).show();});

    }
}
