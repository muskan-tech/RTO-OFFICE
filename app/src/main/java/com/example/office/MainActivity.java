package com.example.office;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    Button login,reg;

    EditText email,pass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         login=(Button)findViewById(R.id.login);
        reg=(Button)findViewById(R.id.reg);
        email=(EditText)findViewById(R.id.email_edit);
        pass=(EditText)findViewById(R.id.pass_edit);

        login();
        reg();
        auth= FirebaseAuth.getInstance();
    }

    private void reg() {
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String mail=email.getText().toString();
            String pas=pass.getText().toString();
            auth.createUserWithEmailAndPassword(mail,pas).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Log.i("ABC","inside onsuccesslistener");
                    Toast.makeText(getApplicationContext(),"Registration done successful",Toast.LENGTH_SHORT).show();
                }



            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.i("ABC",e.getLocalizedMessage());
                }
            });
            }
        });
    }

    private void login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=email.getText().toString();
                String pas=pass.getText().toString();
                auth.signInWithEmailAndPassword(mail,pas).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this,login.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("ABC",e.getLocalizedMessage());
                    }
                });
            }
        });

    }

}