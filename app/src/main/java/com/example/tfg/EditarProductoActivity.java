package com.example.tfg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditarProductoActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText precio;
    private EditText categoria;
    private EditText foto;


    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    private Button buttonEditar;

    private String productoID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_producto);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Productos");

        nombre= findViewById(R.id.nombre_new);
        precio = findViewById(R.id.precio_new);
        categoria = findViewById(R.id.categoria_new);
        foto = findViewById(R.id.foto_new);
        productoID= getIntent().getStringExtra("pid");
        obtenerDatosProductoAEditar(productoID);

        dialog = new ProgressDialog(this);

        buttonEditar = findViewById(R.id.button4);


        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarProducto();
            }
        });



    }

    private void obtenerDatosProductoAEditar(String productoId) {



            DatabaseReference productoRef = mDatabase.child(productoId);
            productoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String Nombre = snapshot.child("Nombre").getValue(String.class);
                        double PrecioDou = snapshot.child("Precio").getValue(Double.class);
                        String Precio = String.valueOf(PrecioDou);
                        String Categoria = snapshot.child("Categoria").getValue(String.class);
                        String Foto = snapshot.child("Foto").getValue(String.class);

                        nombre.setText(Nombre);
                        precio.setText(Precio);
                        categoria.setText(Categoria);
                        foto.setText(Foto);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar el error de lectura de datos si es necesario
                }
            });
        }



    private void editarProducto() {
        String uid = mUser.getUid();

        DatabaseReference usuarioRef = mDatabase.child(uid);

        String Nombre = nombre.getText().toString().trim();
        String Precio = precio.getText().toString().trim();
        double UnTipoPrecio = (Double.valueOf(Precio));
        String Categoria = categoria.getText().toString().trim();
        String Foto = foto.getText().toString().trim();

        if (TextUtils.isEmpty(Nombre)) {
            Toast.makeText(this, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Precio)) {
            Toast.makeText(this, "Ingrese el precio", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Categoria)) {
            Toast.makeText(this, "Ingrese la categoria", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Foto)) {
            Toast.makeText(this, "Ingrese la foto", Toast.LENGTH_SHORT).show();
        }  else {
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor espere...");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);


            DatabaseReference nuevoProductoRef = mDatabase.push();
            String pid = nuevoProductoRef.getKey();

            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("Nombre", Nombre);
            productoMap.put("Precio", UnTipoPrecio);
            productoMap.put("Categoria", Categoria);
            productoMap.put("Foto", Foto);

            usuarioRef.updateChildren(productoMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditarProductoActivity.this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                                EnviarAlInicio();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(EditarProductoActivity.this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });



        }



    }


    private void EnviarAlInicio() {
        Intent intent = new Intent(EditarProductoActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}