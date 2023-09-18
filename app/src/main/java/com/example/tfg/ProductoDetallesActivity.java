package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductoDetallesActivity extends AppCompatActivity {
    private Button agregarCarrito;
    private EditText numeroBoton;
    private ImageView productoImagen;

    TextView productoPrecio, productoNombre;
    private String productoID = "", estado="Normal" , CurrentUserID, establecimiento, mesa;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalles);

        Intent intent = getIntent();

        establecimiento = getIntent().getStringExtra("establecimiento");
        mesa = getIntent().getStringExtra("mesa");

        productoID= getIntent().getStringExtra("pid");

        agregarCarrito=(Button) findViewById(R.id.boton_siguiente_detalles);
        numeroBoton=(EditText) findViewById(R.id.numero_boton);
        productoImagen=(ImageView) findViewById(R.id.producto_imagen_detalles);
        productoPrecio=(TextView) findViewById(R.id.producto_precio_detalles);
        productoNombre=(TextView) findViewById(R.id.producto_nombre_detalles);
        ObtenerDatosProductos(productoID);
        auth = FirebaseAuth.getInstance();
        CurrentUserID = auth.getCurrentUser().getUid();
        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado.equals("Pedido") || estado.equals("Enviado")){
                    Toast.makeText(ProductoDetallesActivity.this, "Esperando a que el primer pedido finalice...", Toast.LENGTH_SHORT).show();
                }else{
                    agregarALaLista();
                }
            }
        });



    }

    @Override
    protected void onStart(){
        super.onStart();
        VerificarEstadoOrden();

    }

    private void agregarALaLista(){


        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Carrito");

        final HashMap<String, Object> map = new HashMap<>();
        map.put("pid", productoID);
        map.put("nombre", productoNombre.getText().toString());
        map.put("precio", productoPrecio.getText().toString());
        map.put("cantidad", numeroBoton.getText().toString());

        CartListRef.child("Usuario Compra").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    CartListRef.child("Admin").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductoDetallesActivity.this, "Agregado...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductoDetallesActivity.this, MenuPrincipalActivity.class);
                                intent.putExtra("establecimiento", establecimiento);
                                intent.putExtra("mesa", mesa);
                                System.out.println("Redireccion: " + establecimiento);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }

    private void ObtenerDatosProductos(String productoID){
        System.out.println("Establecimientpsiaf: " + establecimiento);
        DatabaseReference ProductoRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Productos");
        ProductoRef.child(productoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nombre = snapshot.child("Nombre").getValue(String.class);
                    double precio = snapshot.child("Precio").getValue(Double.class);
                    String foto = snapshot.child("Foto").getValue(String.class);
                    //productoNombre.setText(productos.getNombre());
                    //productoPrecio.setText(String.valueOf(productos.getPrecio()));
                    //Picasso.get().load(productos.getFoto()).into(productoImagen);

                    productoNombre.setText(nombre);
                    productoPrecio.setText(String.valueOf(precio));
                    Picasso.get().load(foto).into(productoImagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void VerificarEstadoOrden(){
        DatabaseReference OrdenRef;
        OrdenRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Ordenes").child(CurrentUserID);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String envioStado = snapshot.child("estado").getValue().toString();
                    if (envioStado.equals("Enviado")){
                        estado="Enviado";
                    }else if(envioStado.equals("No Enviado")){
                        estado="Pedido";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
