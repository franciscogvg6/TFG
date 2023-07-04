package com.example.tfg;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdenActivity extends AppCompatActivity {

    private RecyclerView recicler;
    private DatabaseReference OrdenRef;
    private FirebaseAuth auth;
    private String currentUserId;

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
                        holder.boton.setOnClickListener(v -> {
                            Toast.makeText(OrdenActivity.this, "CORRECTO", Toast.LENGTH_SHORT).show();
                        });
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
        Button boton;

        public OrdenesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.ordenuser);
            fecha = itemView.findViewById(R.id.ordenfecha);
            boton = itemView.findViewById(R.id.verproductosorden);
        }
    }
}
