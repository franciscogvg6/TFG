package com.example.tfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button Siguiente;
    private TextView TotalPrecio, mensaje;
    private double PrecioTotalD = 0.0;

    private String CurrentUserId, establecimiento, mesa;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerView=(RecyclerView)findViewById(R.id.carrito_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Siguiente=(Button)findViewById(R.id.siguiente_proceso);
        TotalPrecio=(TextView)findViewById(R.id.precio_total);
        mensaje=(TextView)findViewById(R.id.mensaje1);
        auth=FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarYConfirmarOrden();
            }
        });

        establecimiento = getIntent().getStringExtra("establecimiento");
        mesa = getIntent().getStringExtra("mesa");
        System.out.println("Establecimientoo: " + establecimiento);
        System.out.println("Mesa carrito: " + mesa);
    }


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Establecimientoo2: " + establecimiento);
        System.out.println("Mesa carrito2: " + mesa);

        VerificarEstadoOrden();


        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Carrito");

        FirebaseRecyclerOptions<Carrito> options = new FirebaseRecyclerOptions.Builder<Carrito>()
                .setQuery(CartListRef.child("Usuario Compra").child(CurrentUserId).child("Productos"), Carrito.class).build();

        FirebaseRecyclerAdapter<Carrito, CarritoViewHolder> adapter = new FirebaseRecyclerAdapter<Carrito, CarritoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CarritoViewHolder holder, int position, @NonNull Carrito model){

                holder.carrProductoNombre.setText(model.getNombre());
                holder.carrProductoCantidad.setText("Cantidad: " + model.getCantidad());
                holder.carrProductoPrecio.setText("Precio: " + model.getPrecio() + " €");
                double UnTipoPrecio = (Double.valueOf(model.getPrecio()))*Integer.valueOf(model.getCantidad());
                PrecioTotalD = PrecioTotalD + UnTipoPrecio;
                TotalPrecio.setText("Total: "+String.valueOf(PrecioTotalD) + " €");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Editar",
                                "Eliminar"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                        builder.setTitle("Opciones del producto");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i ==0){
                                    Intent intent = new Intent(CarritoActivity.this, ProductoDetallesActivity.class);
                                    intent.putExtra("establecimiento", establecimiento);
                                    intent.putExtra("mesa", mesa);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i ==1){
                                    CartListRef.child("Usuario Compra")
                                            .child(CurrentUserId)
                                            .child("Productos")
                                            .child(model.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CarritoActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                                                        //adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });

                                }

                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carr_item, parent, false);
                CarritoViewHolder holder = new CarritoViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void VerificarEstadoOrden() {
        DatabaseReference ordenRef;
        ordenRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Ordenes").child(CurrentUserId);

        ordenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String estado = snapshot.child("estado").getValue().toString();
                    if (estado.equals("Enviado")) {
                        TotalPrecio.setText("Su pedido fue enviado");
                        recyclerView.setVisibility(View.GONE);
                        mensaje.setText("Su pedido se enviará pronto");
                        mensaje.setVisibility(View.VISIBLE);
                        Siguiente.setVisibility(View.GONE);
                    }else if (estado.equals("No Enviado")){
                        TotalPrecio.setText("Su orden está siendo procesada");
                        recyclerView.setVisibility(View.GONE);
                        mensaje.setVisibility(View.VISIBLE);
                        Siguiente.setVisibility(View.GONE);
                        Toast.makeText(CarritoActivity.this, "Puedes comprar más productos cuando el anterior finalice", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ConfirmarOrden() {


        final DatabaseReference OrdenesRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Ordenes");

        String nuevaOrdenId = OrdenesRef.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("idOrden", nuevaOrdenId);
        map.put("estado" , "No Enviado");
        map.put("mesa", mesa);

        DatabaseReference productosRef = OrdenesRef.child(nuevaOrdenId).child("productos");


        OrdenesRef.child(nuevaOrdenId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference carritoRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Carrito")
                            .child("Usuario Compra").child(CurrentUserId);
                    carritoRef.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                                    String productId = productSnapshot.getKey();
                                    Carrito productoCarrito = productSnapshot.getValue(Carrito.class);

                                    // Guarda el producto y su cantidad en la orden recién creada
                                    productosRef.child(productId).setValue(productoCarrito)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Elimina el producto del carrito una vez que se ha añadido a la orden
                                                        carritoRef.child("Productos").child(productId).removeValue();
                                                    }
                                                }
                                            });
                                }
                                // Aquí puedes mostrar un mensaje de éxito o realizar otras acciones después de completar la orden
                                Toast.makeText(CarritoActivity.this, "¡Orden realizada!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CarritoActivity.this, MenuPrincipalActivity.class);
                                intent.putExtra("establecimiento", establecimiento);
                                intent.putExtra("mesa", mesa);
                                System.out.println("mesa orden: " + mesa);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Maneja el error si es necesario
                        }
                    });
                }
            }
        });



    }

    private void verificarYConfirmarOrden() {
        DatabaseReference carritoRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Carrito")
                .child("Usuario Compra").child(CurrentUserId).child("Productos");

        carritoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Carrito productoCarrito = productSnapshot.getValue(Carrito.class);

                        DatabaseReference productosRef = FirebaseDatabase.getInstance().getReference().child(establecimiento).child("Productos")
                                .child(productoCarrito.getNombre());

                        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int cantidadDisponible = Integer.parseInt(dataSnapshot.child("Cantidad").getValue().toString());
                                    int cantidadPedida = Integer.parseInt(productoCarrito.getCantidad());

                                    if (cantidadPedida <= cantidadDisponible) {
                                        // La cantidad pedida es válida, confirma la orden y resta la cantidad
                                        ConfirmarOrden();
                                        int nuevaCantidad = cantidadDisponible - cantidadPedida;
                                        dataSnapshot.getRef().child("Cantidad").setValue(nuevaCantidad);
                                    } else {
                                        // La cantidad pedida excede la cantidad disponible, muestra un mensaje de error
                                        Toast.makeText(CarritoActivity.this, "La cantidad pedida excede la cantidad disponible", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar el error si es necesario
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error si es necesario
            }
        });
    }



}
