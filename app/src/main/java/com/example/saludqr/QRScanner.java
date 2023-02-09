package com.example.saludqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanner extends AppCompatActivity {
    String idBusqueda;
    FloatingActionButton fabqr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        fabqr = findViewById(R.id.fab);
        fabqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador = new IntentIntegrator(QRScanner.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Escanee el QR del Adulto Mayor");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(false);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()== null){
                Toast.makeText(this,"Lectura cancelada", Toast.LENGTH_LONG).show();
            }else{
                idBusqueda = result.getContents();
                vistaDr();
                finish();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



    public void vistaDr() {
        Intent i = new Intent(this, drVista.class);
        i.putExtra("dato",idBusqueda);
        startActivity(i);
    }

}