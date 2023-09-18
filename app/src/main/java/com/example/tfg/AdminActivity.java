package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.tfg.CategoriaAdapter;
import com.example.tfg.CategoriaAdapter.OnItemClickListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCategorias;
    private RecyclerView recyclerViewProductos;
    private CategoriaAdapter categoriaAdapter;

    private String correo;

    private FirebaseAuth auth;
    private String currentUserId, establecimiento;

    private DatabaseReference userRef;

    private ProductoAdapterAdmin productoAdapter;

    private TextView textViewCategoriaSeleccionada;

    private ArrayList<Categoria> categoriasList;
    private ArrayList<Producto> productosList;

    Button btn_cerrar_sesion;

    Button btn_perfil;

    Button btn_ordenes;

    Button btn_anadir;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        /*FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();

                        establecimiento = deepLink.getQueryParameter("establecimiento");
                        String mesa = deepLink.getQueryParameter("mesa");

                        System.out.println("Establecimiento: " + establecimiento);
                        System.out.println("Mesa: " + mesa);



                        obtenerCategoriasDesdeFirebase();
                        Categoria categoriaInicial = new Categoria("Cervezas");
                        textViewCategoriaSeleccionada.setText(categoriaInicial.getNombre());
                        obtenerProductosDesdeFirebase(categoriaInicial);


                        recyclerViewProductos = findViewById(R.id.rv_productos);
                        productosList = new ArrayList<>();
                        productoAdapter = new ProductoAdapterAdmin(productosList, R.layout.productos_admin_render, establecimiento);
                        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
                        recyclerViewProductos.setAdapter(productoAdapter);
                    }
                })
                .addOnFailureListener(this, e -> {

                });*/

        establecimiento = getIntent().getStringExtra("establecimiento");

        // Inicializar las vistas
        recyclerViewCategorias = findViewById(R.id.rv);
        recyclerViewProductos = findViewById(R.id.rv_productos);
        textViewCategoriaSeleccionada = findViewById(R.id.textViewCategoriaSeleccionada);

        // Inicializar las listas de categorías y productos
        categoriasList = new ArrayList<>();
        productosList = new ArrayList<>();


        // Simular datos de prueba
        //cargarDatosDePrueba();

        // Configurar el RecyclerView de categorías
        categoriaAdapter = new CategoriaAdapter(categoriasList, R.layout.rv_render);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Configurar el RecyclerView de productos
        recyclerViewProductos = findViewById(R.id.rv_productos);
        productoAdapter = new ProductoAdapterAdmin(productosList, R.layout.productos_admin_render, establecimiento);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(productoAdapter);

        Categoria categoriaInicial = new Categoria("Cervezas");
        textViewCategoriaSeleccionada.setText(categoriaInicial.getNombre());

        obtenerCategoriasDesdeFirebase();
        obtenerProductosDesdeFirebase(categoriaInicial);




        // Manejar el evento de clic en una categoría
        categoriaAdapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria categoria, int position) {
                textViewCategoriaSeleccionada.setText(categoria.getNombre());
                obtenerProductosDesdeFirebase(categoria);
            }
        });


        btn_cerrar_sesion = findViewById(R.id.button3);
        btn_perfil = findViewById(R.id.perfil);
        btn_ordenes = findViewById(R.id.ordenes);
        btn_anadir = findViewById(R.id.anadir_prod);

        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                irALogin();
            }
        });

        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAPerfil();
            }
        });

        btn_ordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAOrdenes();
            }
        });

        btn_anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAAnadir();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            correo = bundle.getString("correo");
        }

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Admin");
    }

    private void obtenerCategoriasDesdeFirebase() {
        Query query = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Categorias");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriasList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nombre = snapshot.child("Nombre").getValue(String.class);
                    Categoria categoria = new Categoria(nombre);
                    categoriasList.add(categoria);
                }
                categoriaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }

    private void obtenerProductosDesdeFirebase(Categoria categoria) {
        Query query = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Productos")
                .orderByChild("Categoria").equalTo(categoria.getNombre());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productosList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String nombre = snapshot.child("Nombre").getValue(String.class);
                    double precio = snapshot.child("Precio").getValue(Double.class);
                    double cantidad = snapshot.child("Cantidad").getValue(Double.class);
                    String foto = snapshot.child("Foto").getValue(String.class);
                    String categoria = snapshot.child("Categoria").getValue(String.class);
                    String pid = snapshot.child("pid").getValue(String.class);
                    String fecha = snapshot.child("fecha").getValue(String.class);
                    String hora = snapshot.child("hora").getValue(String.class);
                    Log.d("FirebaseData", "Nombre: " + nombre + ", Precio: " + precio + ", Foto: " + foto + ", Categoria: " + categoria);
                    Producto producto = new Producto(nombre,precio,foto,categoria,pid,fecha,hora,cantidad);
                    productosList.add(producto);
                }
                productoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error al leer los productos desde Firebase: " + databaseError.getMessage());
            }
        });
    }

    private void irALogin(){
        Intent i = new Intent(this, RegistrarAdminActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("establecimiento", establecimiento);
        startActivity(i);
    }

    private void irAPerfil() {
        Intent i = new Intent(this, PerfilAdminActivity.class);
        i.putExtra("establecimiento", establecimiento);
        startActivity(i);
    }

    private void irAOrdenes() {
        Intent i = new Intent(this, OrdenActivity.class);
        i.putExtra("establecimiento", establecimiento);
        startActivity(i);
    }

    private void irAAnadir() {
        Intent i = new Intent(this, NuevoProductoActivity.class);
        i.putExtra("establecimiento", establecimiento);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            EnviarAlLogin();
        }else {
            VerificarUsuarioExistente();
        }
    }

    private void VerificarUsuarioExistente() {
        final String currentUserId = auth.getCurrentUser().getUid();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(currentUserId)) {
                    EnviarAPerfil();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void EnviarAlLogin() {

        Intent intent = new Intent(AdminActivity.this, RegistrarAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("establecimiento", establecimiento);
        startActivity(intent);
        finish();
    }

    private void EnviarAPerfil() {
        Intent intent = new Intent(AdminActivity.this, PerfilAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("correo", correo);
        intent.putExtra("establecimiento", establecimiento);
        startActivity(intent);
        finish();

    }




}
