package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class RegistrarAdminActivity extends AppCompatActivity {

    EditText email, password;

    String correo, establecimiento;
    Button btn_registrar, btn_login;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_admin);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuth mAuth= FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        establecimiento = getIntent().getStringExtra("establecimiento");

        if(user !=null){
            irAHome();
        }
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.correoAdmin, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.editTextTextPassword, ".{6,}", R.string.invalid_password);


        email = findViewById(R.id.correoAdmin);
        password = findViewById(R.id.editTextTextPassword);
        btn_registrar = findViewById(R.id.button);
        btn_login = findViewById(R.id.button2);


        btn_registrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String correo = email.getText().toString();
                String pass = password.getText().toString();

                if(awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrarAdminActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                                irAHome();


                            }else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistrarAdminActivity.this, "Completa todos los datos", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(awesomeValidation.validate()){

                    String mail = email.getText().toString();
                    String pass = password.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                irAHome();

                            }else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                }

            }
        });
    }

    private void irAHome(){
        Intent i = new Intent(RegistrarAdminActivity.this, AdminActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("correo", correo);
        i.putExtra("rol", "admin");
        i.putExtra("establecimiento", establecimiento);
        startActivity(i);
        finish();
    }

    private void dameToastdeerror(@NotNull String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegistrarAdminActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegistrarAdminActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegistrarAdminActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegistrarAdminActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico está mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegistrarAdminActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                password.setError("la contraseña es incorrecta ");
                password.requestFocus();
                password.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegistrarAdminActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegistrarAdminActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegistrarAdminActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegistrarAdminActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegistrarAdminActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegistrarAdminActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegistrarAdminActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegistrarAdminActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegistrarAdminActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegistrarAdminActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                password.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                password.requestFocus();
                break;

        }

    }



}
