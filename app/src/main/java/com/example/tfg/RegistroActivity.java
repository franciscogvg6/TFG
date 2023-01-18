package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class RegistroActivity extends AppCompatActivity {

    EditText usuario, nombre, correo, contraseña, re_contraseña;
    String str_usuario, str_nombre, str_correo, str_contraseña, str_re_contraseña;
    String url="https://flitting-move.000webhostapp.com/registro.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuario = findViewById(R.id.editTextTextPersonName9);
        nombre = findViewById(R.id.editTextTextPersonName10);
        correo = findViewById(R.id.editTextTextPersonName8);
        contraseña = findViewById(R.id.editTextTextPassword3);
        re_contraseña = findViewById(R.id.editTextTextPassword2);

    }

    public void moveToLogin(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void Register(View view){
        final ProgressDialog progressDialog = new ProgressDialog( this);
        progressDialog.setMessage("Por favor espera");

        if(usuario.getText().toString().equals("")){
            Toast.makeText(this, "Introduce el usuario", Toast.LENGTH_SHORT).show();
        }
        else if(nombre.getText().toString().equals("")){
            Toast.makeText(this, "Introduce el nombre", Toast.LENGTH_SHORT).show();
        }
        else if(correo.getText().toString().equals("")){
            Toast.makeText(this, "Introduce el correo electrónico", Toast.LENGTH_SHORT).show();
        }
        else if(contraseña.getText().toString().equals("")){
            Toast.makeText(this, "Introduce la contraseña", Toast.LENGTH_SHORT).show();
        }
        else if(re_contraseña.getText().toString().equals("")){
            Toast.makeText(this, "Repite la contraseña", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.show();
            str_usuario = usuario.getText().toString().trim();
            str_nombre = nombre.getText().toString().trim();
            str_correo = correo.getText().toString().trim();
            str_contraseña = contraseña.getText().toString().trim();
            str_re_contraseña = re_contraseña.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    usuario.setText("");
                    nombre.setText("");
                    correo.setText("");
                    contraseña.setText("");
                    re_contraseña.setText("");
                    Toast.makeText(RegistroActivity.this, response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistroActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("usuario", str_usuario);
                    params.put("nombre", str_nombre);
                    params.put("correo", str_correo);
                    params.put("contraseña", str_contraseña);
                    params.put("re_contraseña", str_re_contraseña);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(RegistroActivity.this);
            requestQueue.add(request);

        }


    }
}