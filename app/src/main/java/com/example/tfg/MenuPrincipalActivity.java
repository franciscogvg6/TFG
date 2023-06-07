package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuPrincipalActivity extends AppCompatActivity {

    Button btn_cerrar_sesion;

    Button btn_perfil;

    private CategoriaAdapter cAdapter;
    private RecyclerView cRecyclerView;
    private ArrayList<Categoria> cCategoriasList = new ArrayList<>();

    private DatabaseReference cDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_menu_principal);



        btn_cerrar_sesion = findViewById(R.id.button3);
        btn_perfil = findViewById(R.id.perfil);
        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MenuPrincipalActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                irALogin();
            }
        });

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAPerfil();
            }
        });

        cRecyclerView = findViewById(R.id.rv);
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cAdapter = new CategoriaAdapter(cCategoriasList, R.layout.rv_render);
        cRecyclerView.setAdapter(cAdapter);

        cDatabase = FirebaseDatabase.getInstance().getReference();

        getCategoriasFromBd();





    }

    private void irALogin(){
        Intent i = new Intent(this, RegistrarActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void irAPerfil(){
        Intent i = new Intent(this, PerfilActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void getCategoriasFromBd(){

        cDatabase.child("Categorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cCategoriasList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String categoria = ds.child("Nombre").getValue(String.class);
                    cCategoriasList.add(new Categoria(categoria));
                }

                cAdapter = new CategoriaAdapter(cCategoriasList, R.layout.rv_render);
                cRecyclerView.setAdapter(cAdapter);

                Log.d("FirebaseData", "Total categorias: " + cCategoriasList.size());
                for (Categoria categoria : cCategoriasList) {
                    Log.d("FirebaseData", "Categoria: " + categoria.getCategoria());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }



}