package com.example.tfg;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String currentUserId;

    private DatabaseReference userRef, establecimientoRef;

    private boolean handleDynamicLink = true;


    private String correo = "";
    private RecyclerView recyclerViewCategorias;
    private RecyclerView recyclerViewProductos;
    private CategoriaAdapter categoriaAdapter;
    private ProductoAdapter productoAdapter;

    private TextView textViewCategoriaSeleccionada;

    private ArrayList<Categoria> categoriasList;
    private ArrayList<Producto> productosList;

    Button btn_cerrar_sesion;

    FloatingActionButton btn_carr;

    Button btn_perfil;

    String establecimiento, mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        establecimiento = getIntent().getStringExtra("establecimiento");
        mesa = getIntent().getStringExtra("mesa");






        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            correo = bundle.getString("correo");
        }

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        // Inicializar las vistas
        recyclerViewCategorias = findViewById(R.id.rv);
        recyclerViewProductos = findViewById(R.id.rv_productos);

        textViewCategoriaSeleccionada = findViewById(R.id.textViewCategoriaSeleccionada);
        Categoria categoriaInicial = new Categoria("Cervezas");
        textViewCategoriaSeleccionada.setText(categoriaInicial.getNombre());


        // Inicializar las listas de categorías y productos
        categoriasList = new ArrayList<>();



        // Configurar el RecyclerView de categorías
        categoriaAdapter = new CategoriaAdapter(categoriasList, R.layout.rv_render);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Configurar el RecyclerView de productos
        productosList = new ArrayList<>();
        productoAdapter = new ProductoAdapter(productosList, R.layout.productos_render, establecimiento, mesa);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(productoAdapter);





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
        btn_carr = findViewById(R.id.carr);
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


        btn_carr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irACarrito();
            }
        });
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

        Intent intent = new Intent(MenuPrincipalActivity.this, RegistrarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("establecimiento", establecimiento);
        intent.putExtra("mesa", mesa);
        startActivity(intent);
        finish();
    }

    private void EnviarAPerfil() {
        Intent intent = new Intent(MenuPrincipalActivity.this, PerfilActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("correo", correo);
        intent.putExtra("establecimiento", establecimiento);
        intent.putExtra("mesa", mesa);
        startActivity(intent);
        finish();

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
                    Log.d("FirebaseData", "Nombre: " + nombre + ", Precio: " + precio + ", Foto: " + foto + ", Categoria: " + categoria);
                    Producto producto = new Producto(nombre,precio,foto,categoria,pid,cantidad);
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
        Intent i = new Intent(this, RegistrarActivity.class);
        i.putExtra("establecimiento", establecimiento);
        i.putExtra("mesa", mesa);
        startActivity(i);
    }

    private void irAPerfil() {
        Intent i = new Intent(this, PerfilActivity.class);
        i.putExtra("establecimiento", establecimiento);
        i.putExtra("mesa", mesa);
        startActivity(i);
    }

    private void irACarrito() {
        Intent i = new Intent(this, CarritoActivity.class);
        i.putExtra("establecimiento", establecimiento);
        i.putExtra("mesa", mesa);
        startActivity(i);
    }




}
