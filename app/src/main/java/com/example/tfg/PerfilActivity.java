package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
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

public class PerfilActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextNombre;
    private EditText editTextApellidos;
    private EditText editTextLocalidad;
    private EditText editTextFechaNacimiento;
    private Button buttonEditar;

    private Button buttonEliminar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    private Button regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");

        editTextEmail = findViewById(R.id.editTextTextPersonName8);
        editTextNombre = findViewById(R.id.editTextTextPersonName9);
        editTextApellidos = findViewById(R.id.editTextTextPersonName10);
        editTextLocalidad = findViewById(R.id.editTextTextPassword3);
        editTextFechaNacimiento = findViewById(R.id.editTextTextPassword2);

        buttonEditar = findViewById(R.id.button4);
        buttonEliminar = findViewById(R.id.button5);
        regresar = findViewById(R.id.atras);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        obtenerDatosUsuarioActivo();

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPerfil();
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPerfil();
            }
        });
    }

    private void obtenerDatosUsuarioActivo() {
        if (mUser != null) {
            String uid = mUser.getUid();
            String email = mUser.getEmail();
            editTextEmail.setText(email);

            DatabaseReference usuarioRef = mDatabase.child(uid);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nombre = snapshot.child("Nombre").getValue(String.class);
                        String apellidos = snapshot.child("Apellidos").getValue(String.class);
                        String localidad = snapshot.child("Localidad").getValue(String.class);
                        String fechaNacimiento = snapshot.child("Fecha de Nacimiento").getValue(String.class);

                        editTextNombre.setText(nombre);
                        editTextApellidos.setText(apellidos);
                        editTextLocalidad.setText(localidad);
                        editTextFechaNacimiento.setText(fechaNacimiento);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar el error de lectura de datos si es necesario
                }
            });
        }
    }

    private void editarPerfil() {
        String uid = mUser.getUid();

        DatabaseReference usuarioRef = mDatabase.child(uid);

        String nombre = editTextNombre.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        String localidad = editTextLocalidad.getText().toString().trim();
        String fechaNacimiento = editTextFechaNacimiento.getText().toString().trim();


        Map<String, Object> updates = new HashMap<>();
        updates.put("Nombre", nombre);
        updates.put("Apellidos", apellidos);
        updates.put("Localidad", localidad);
        updates.put("Fecha de Nacimiento", fechaNacimiento);

        usuarioRef.updateChildren(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PerfilActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PerfilActivity.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void eliminarPerfil() {
        String uid = mUser.getUid();

        // Eliminar el usuario de la base de datos
        mDatabase.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Eliminar el usuario de la autenticación
                    mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Cerrar la sesión actual
                                FirebaseAuth.getInstance().signOut();

                                Toast.makeText(PerfilActivity.this, "Perfil eliminado correctamente", Toast.LENGTH_SHORT).show();

                                // Redirigir al usuario a la pantalla de inicio de sesión
                                Intent intent = new Intent(PerfilActivity.this, RegistrarActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // Mostrar mensaje de error al eliminar el usuario de la autenticación
                                Toast.makeText(PerfilActivity.this, "Error al eliminar el usuario de la autenticación: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Mostrar mensaje de error al eliminar el perfil de la base de datos
                    Toast.makeText(PerfilActivity.this, "Error al eliminar el perfil de la base de datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}