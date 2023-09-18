package com.example.tfg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.ArrayList;



public class LoginActivity extends AppCompatActivity {

    private Button boton_usuario, boton_administrador;
    private String establecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        establecimiento = deepLink.getQueryParameter("establecimiento");

                        System.out.println("Establecimiento: " + establecimiento);


                    }
                })
                .addOnFailureListener(this, e -> {

                });

        boton_usuario=(Button) findViewById(R.id.boton_usuario);
        boton_administrador=(Button) findViewById(R.id.boton_administrador);

        boton_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                intent.putExtra("establecimiento", establecimiento);
                startActivity(intent);
            }
        });
        boton_administrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrarAdminActivity.class);
                intent.putExtra("establecimiento", establecimiento);
                startActivity(intent);
            }
        });
    }

}
