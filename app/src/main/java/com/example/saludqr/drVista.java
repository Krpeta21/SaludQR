package com.example.saludqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class drVista extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView fecNac, lugNac, textoSeg, nombreC,tSang,diab,hip,disc,edad;
    ImageView imgA;
    String url = "http://localhost:80/saludqr/img/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_vista);
        nombreC = (TextView) findViewById(R.id.nombreC);
        edad = (TextView) findViewById(R.id.edad);
        fecNac = (TextView) findViewById(R.id.fecNac);
        lugNac = (TextView) findViewById(R.id.lugNac);
        textoSeg = (TextView) findViewById(R.id.textoSeg);
        tSang = (TextView) findViewById(R.id.tSang);
        diab = (TextView) findViewById(R.id.diab);
        hip = (TextView) findViewById(R.id.hip);
        disc = (TextView) findViewById(R.id.disc);
        imgA = (ImageView) findViewById(R.id.imgA);
        requestQueue  = Volley.newRequestQueue(getApplicationContext());
        String dato = getIntent().getStringExtra("dato");
        buscarPacientes("http://localhost:80/saludqr/buscar_paciente.php?id="+dato+"");

    }

    public void buscarPacientes(String URL) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String nombreCom = jsonObject.getString("nombre");
                        nombreCom = nombreCom+ " " + jsonObject.getString("apellido");
                        nombreC.setText(nombreCom);
                        edad.setText(jsonObject.getString("edad"));
                        fecNac.setText(jsonObject.getString("fecNac"));
                        lugNac.setText(jsonObject.getString("lugarNacimiento"));
                        textoSeg.setText(jsonObject.getString("textoSeg"));
                        tSang.setText(jsonObject.getString("sangre"));
                        diab.setText(jsonObject.getString("diabetes"));
                        hip.setText(jsonObject.getString("hipertension"));
                        disc.setText(jsonObject.getString("discapacidad"));
                        url += jsonObject.getString("imagen");
                        cargarWebServiceImage();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Datos inexistentes.", Toast.LENGTH_SHORT).show();
                qrscanner();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    public void qrscanner() {
        Intent i = new Intent(this, QRScanner.class);
        startActivity(i);
    }

    public void qrscanner2(View view) {
        Intent i = new Intent(this, QRScanner.class);
        startActivity(i);
        finish();
    }
    public void cargarWebServiceImage(){
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgA.setImageBitmap(response);
            }
        },0,0, ImageView.ScaleType.CENTER,null, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }

}