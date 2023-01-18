package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText correo, contraseña;
    Button btRegistrate;

    String str_correo, str_contraseña;
    String url = "https://flitting-move.000webhostapp.com/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegistrate = (Button)findViewById(R.id.button);

        btRegistrate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }

        });

        correo = findViewById(R.id.editTextTextPersonName);
        contraseña = findViewById(R.id.editTextTextPassword);
    }

    public void Login(View view) {
        if(correo.getText().toString().equals("")){
            Toast.makeText(this, "Introduce el correo", Toast.LENGTH_SHORT).show();
        }
        else if(contraseña.getText().toString().equals("")){
            Toast.makeText(this, "Introduce la contraseña", Toast.LENGTH_SHORT).show();
        }
        else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Por favor espera");

            progressDialog.show();

            str_correo = correo.getText().toString().trim();
            str_contraseña= contraseña.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    if(response.equalsIgnoreCase("ingresaste correctamente")){
                        correo.setText("");
                        contraseña.setText("");
                        startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("correo", str_correo);
                    params.put("contraseña", str_contraseña);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);

        }
    }
}