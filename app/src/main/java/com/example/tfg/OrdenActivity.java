package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdenActivity extends AppCompatActivity {

    private RecyclerView recicler;
    private DatabaseReference OrdenRef;
    private FirebaseAuth auth;
    private static String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_ordenes);
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        OrdenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes");
        recicler = findViewById(R.id.recicler_ordenes);
        recicler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Ordenes> options = new FirebaseRecyclerOptions.Builder<Ordenes>()
                .setQuery(OrdenRef, Ordenes.class)
                .build();

        FirebaseRecyclerAdapter<Ordenes, OrdenesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Ordenes, OrdenesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdenesViewHolder holder, int position, @NonNull Ordenes model) {
                        holder.nombre.setText(model.getNombre());
                        holder.fecha.setText(model.getFecha() + " " + model.getHora());
                        holder.showProductos(currentUserId);
                    }

                    @NonNull
                    @Override
                    public OrdenesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordenes_layout, parent, false);
                        return new OrdenesViewHolder(view);
                    }
                };

        recicler.setAdapter(adapter);
        adapter.startListening();
    }

    public static class OrdenesViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha;
        RecyclerView reciclerProductos;

        Button btnConfirmar;

        public OrdenesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.ordenuser);
            fecha = itemView.findViewById(R.id.ordenfecha);
            reciclerProductos = itemView.findViewById(R.id.recycler_productos_orden);
            btnConfirmar = itemView.findViewById(R.id.confirmar);
        }

        public void showProductos(String orderId) {

            DatabaseReference ProductosRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(currentUserId).child("productos");
            FirebaseRecyclerOptions<ProductoOrden> options = new FirebaseRecyclerOptions.Builder<ProductoOrden>()
                    .setQuery(ProductosRef, ProductoOrden.class)
                    .build();

            FirebaseRecyclerAdapter<ProductoOrden, ProductoOrdenViewHolder> adapter =
                    new FirebaseRecyclerAdapter<ProductoOrden, ProductoOrdenViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductoOrdenViewHolder holder, int position, @NonNull ProductoOrden model) {
                            holder.textNombreProducto.setText(model.getNombre());
                            holder.textCantidadProducto.setText("Cantidad: " + model.getCantidad());

                        }

                        @NonNull
                        @Override
                        public ProductoOrdenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_orden, parent, false);
                            return new ProductoOrdenViewHolder(view);
                        }
                    };

            reciclerProductos.setAdapter(adapter);
            adapter.startListening();
            reciclerProductos.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

            btnConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Aquí realizamos la acción para eliminar la orden de la base de datos
                    DatabaseReference ordenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(currentUserId);
                    ordenRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(itemView.getContext(), "Orden enviada correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(itemView.getContext(), AdminActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    itemView.getContext().startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(itemView.getContext(), "Error al eliminar la orden", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }


    public static class ProductoOrdenViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreProducto, textCantidadProducto, textPrecioProducto;

        public ProductoOrdenViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            textCantidadProducto = itemView.findViewById(R.id.txtCantidadProducto);
        }


    }
}

