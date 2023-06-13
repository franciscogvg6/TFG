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
        cargarDatosDePrueba();

        // Configurar el RecyclerView de categorías
        categoriaAdapter = new CategoriaAdapter(categoriasList, R.layout.rv_render);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Configurar el RecyclerView de productos
        productoAdapter = new ProductoAdapter(productosList, R.layout.productos_render);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(productoAdapter);

        // Manejar el evento de clic en una categoría
        categoriaAdapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria categoria, int position) {
                textViewCategoriaSeleccionada.setText(categoria.getCategoria());
                obtenerProductosPorCategoria(categoria);
            }
        });
    }


    private void cargarDatosDePrueba() {
        // Cargar categorías de prueba
        categoriasList.add(new Categoria("Cervezas"));
        categoriasList.add(new Categoria("Vinos"));
        categoriasList.add(new Categoria("Licores"));
        categoriasList.add(new Categoria("Refrescos"));

        // Cargar productos de prueba
        productosList.add(new Producto("Cerveza 1", "Cruzcampo", "10.99", "", "Cervezas"));
        productosList.add(new Producto("Cerveza 2", "Estrella","15.99","", "Cervezas"));
        productosList.add(new Producto("Vino 1", "Cruzcampo","5.99","", "Vinos"));

    }

    private void obtenerProductosPorCategoria(Categoria categoria) {
        ArrayList<Producto> productosPorCategoria = new ArrayList<>();
        // Recorrer la lista de productos y filtrar los que pertenecen a la categoría seleccionada
        for (Producto producto : productosList) {
            if (producto.getCategoria().equals(categoria.getCategoria())) {
                productosPorCategoria.add(producto);
            }
        }
        // Actualizar el adaptador de productos con la nueva lista filtrada
        productoAdapter.updateProductos(productosPorCategoria);
    }

    public void mostrarMensajeTotalProductos(View view) {
        int totalProductos = productoAdapter.getItemCount();
        String mensaje = "El total de productos es: " + totalProductos;
        textViewCategoriaSeleccionada.setText(mensaje);
    }


}
