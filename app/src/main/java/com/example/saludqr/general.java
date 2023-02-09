package com.example.saludqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class general extends AppCompatActivity {
    FloatingActionButton fabqr;
    RequestQueue requestQueue;
    TextView nombreB, apellidoB, idPaciente, edadB, sangreB, emergenciaB, medicacionB,padecimientosB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        idPaciente = (TextView) findViewById(R.id.idPaciente);
        nombreB = (TextView) findViewById(R.id.nombreB);
        apellidoB = (TextView) findViewById(R.id.apellidoB);
        edadB = (TextView) findViewById(R.id.edadB);
        sangreB = (TextView) findViewById(R.id.sangreB);
        emergenciaB = (TextView) findViewById(R.id.emergenciaB);
        medicacionB = (TextView) findViewById(R.id.medicacionB);
        padecimientosB = (TextView) findViewById(R.id.padecimientosB);


        fabqr = findViewById(R.id.fab);
        fabqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador = new IntentIntegrator(general.this);
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
                    idPaciente.setText(result.getContents());
                    buscarPacientes("http://localhost:80/saludqr/buscar_paciente.php?id="+idPaciente.getText()+"");
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    public void Scanner(View view) {
        Intent i = new Intent(this, QRScanner.class);
        startActivity(i);
    }
    public void buscarPacientes(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombreB.setText(jsonObject.getString("nombre"));
                        apellidoB.setText(jsonObject.getString("apellido"));
                        emergenciaB.setText(jsonObject.getString("emergencia"));
                        edadB.setText(jsonObject.getString("edad"));
                        sangreB.setText(jsonObject.getString("sangre"));
                        padecimientosB.setText(jsonObject.getString("padecimientos"));
                        medicacionB.setText(jsonObject.getString("medicacion"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Datos inexistentes.", Toast.LENGTH_SHORT).show();
                nombreB.setText("");
                apellidoB.setText("");
                emergenciaB.setText("");
                edadB.setText("");
                sangreB.setText("");
                padecimientosB.setText("");
                medicacionB.setText("");

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}