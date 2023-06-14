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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.tfg.CategoriaAdapter;
import com.example.tfg.CategoriaAdapter.OnItemClickListener;

import java.util.ArrayList;

public class MenuPrincipalActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCategorias;
    private RecyclerView recyclerViewProductos;
    private CategoriaAdapter categoriaAdapter;
    private ProductoAdapter productoAdapter;

    private TextView textViewCategoriaSeleccionada;

    private ArrayList<Categoria> categoriasList;
    private ArrayList<Producto> productosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

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
        productoAdapter = new ProductoAdapter(productosList, R.layout.productos_render);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(productoAdapter);

        // Obtener las categorías desde la base de datos
        obtenerCategoriasDesdeFirebase();


        // Manejar el evento de clic en una categoría
        categoriaAdapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria categoria, int position) {
                textViewCategoriaSeleccionada.setText(categoria.getNombre());
                obtenerProductosDesdeFirebase(categoria);
            }
        });

        Categoria categoriaInicial = new Categoria("Cervezas");
        textViewCategoriaSeleccionada.setText(categoriaInicial.getNombre());
        obtenerProductosDesdeFirebase(categoriaInicial);
    }

    private void obtenerCategoriasDesdeFirebase() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Categorias");
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
        Query query = FirebaseDatabase.getInstance().getReference().child("Productos")
                .orderByChild("Categoria").equalTo(categoria.getNombre());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productosList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String nombre = snapshot.child("Nombre").getValue(String.class);
                    String precio = snapshot.child("Precio").getValue(String.class);
                    String foto = snapshot.child("Foto").getValue(String.class);
                    String categoria = snapshot.child("Categoria").getValue(String.class);
                    Log.d("FirebaseData", "Nombre: " + nombre + ", Precio: " + precio + ", Foto: " + foto + ", Categoria: " + categoria);
                    Producto producto = new Producto("",nombre,precio,foto,categoria);
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



    public void mostrarMensajeTotalProductos(View view) {
        int totalProductos = productoAdapter.getItemCount();
        String mensaje = "El total de productos es: " + totalProductos;
        textViewCategoriaSeleccionada.setText(mensaje);
    }


}
