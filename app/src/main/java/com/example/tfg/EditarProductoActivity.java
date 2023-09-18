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
    private EditText cantidad;


    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    private Button buttonEditar, buttonEliminar;

    private String productoID, establecimiento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_producto);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        establecimiento = getIntent().getStringExtra("establecimiento");
        mDatabase = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Productos");

        nombre= findViewById(R.id.nombre_new);
        precio = findViewById(R.id.precio_new);
        categoria = findViewById(R.id.categoria_new);
        foto = findViewById(R.id.foto_new);
        cantidad = findViewById(R.id.cantidad_new);
        productoID = getIntent().getStringExtra("pid");

        obtenerDatosProductoAEditar(productoID);

        dialog = new ProgressDialog(this);

        buttonEditar = findViewById(R.id.button4);
        buttonEliminar= findViewById(R.id.button5);


        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarProducto(productoID);
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto(productoID); // Llama a la función eliminarProducto
            }
        });



    }

    private void obtenerDatosProductoAEditar(String productoID) {



            DatabaseReference productoRef = mDatabase;
            productoRef.child(productoID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String Nombre = snapshot.child("Nombre").getValue(String.class);
                        double PrecioDou = snapshot.child("Precio").getValue(Double.class);
                        double CantidadDou = snapshot.child("Cantidad").getValue(Double.class);
                        String Precio = String.valueOf(PrecioDou);
                        String Cantidad = String.valueOf(CantidadDou);
                        String Categoria = snapshot.child("Categoria").getValue(String.class);
                        String Foto = snapshot.child("Foto").getValue(String.class);

                        nombre.setText(Nombre);
                        precio.setText(Precio);
                        categoria.setText(Categoria);
                        foto.setText(Foto);
                        cantidad.setText(Cantidad);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar el error de lectura de datos si es necesario
                }
            });
        }



    private void editarProducto(String productoID) {


        DatabaseReference productoRef = mDatabase.child(productoID);

        String Nombre = nombre.getText().toString().trim();
        String Precio = precio.getText().toString().trim();
        double UnTipoPrecio = (Double.valueOf(Precio));
        String Cantidad = cantidad.getText().toString().trim();
        double UnTipoCantidad = (Double.valueOf(Cantidad));
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
        }  else if (TextUtils.isEmpty(Cantidad)) {
            Toast.makeText(this, "Ingrese la cantidad", Toast.LENGTH_SHORT).show();
        }else {
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor espere...");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);


            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("Nombre", Nombre);
            productoMap.put("Precio", UnTipoPrecio);
            productoMap.put("Categoria", Categoria);
            productoMap.put("Foto", Foto);
            productoMap.put("Cantidad", UnTipoCantidad);

            productoRef.updateChildren(productoMap)
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
        intent.putExtra("establecimiento", establecimiento);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void eliminarProducto(String productoID) {
        DatabaseReference productoRef = mDatabase.child(productoID);
        productoRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditarProductoActivity.this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                            EnviarAlInicio();
                        } else {
                            Toast.makeText(EditarProductoActivity.this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}