package com.example.ass_restadr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class singup extends AppCompatActivity {
    EditText edtTextEmail, edtTextPassword;
    Button btnSingup;
    FirebaseAuth mauth;
    ProgressBar progressbar;
    TextView loginNow;
    /*
    public void  onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mauth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
    }

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        mauth = FirebaseAuth.getInstance();
        edtTextEmail = findViewById(R.id.mail);
        edtTextPassword = findViewById(R.id.passwordSU);
        btnSingup = findViewById(R.id.btn_singup);
        progressbar = findViewById(R.id.progressbar);
        loginNow = findViewById(R.id.loginNow);
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(edtTextEmail.getText());
                password = String.valueOf(edtTextPassword.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(singup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(singup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    Toast.makeText(singup.this, "Create account", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(singup.this, "fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}