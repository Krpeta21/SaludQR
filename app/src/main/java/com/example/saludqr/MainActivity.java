package com.example.saludqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void Scanner(View view) {
        Intent i = new Intent(this, QRScanner.class);
        startActivity(i);
        finish();
    }
    public void Invitado(View view) {
        Intent i = new Intent(this, general.class);
        startActivity(i);
        finish();
    }
}