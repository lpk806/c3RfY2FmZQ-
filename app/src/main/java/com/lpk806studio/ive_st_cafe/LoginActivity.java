package com.lpk806studio.ive_st_cafe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lpk806studio.ive_st_cafe.Model.Check_network;

public class LoginActivity extends AppCompatActivity {
    private EditText username, pass;
    private Button btn_login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String email, pw;
    private boolean network_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.user_email);
        pass = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = username.getText().toString();
                pw = pass.getText().toString();

                //get newtowk state
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                Check_network cn = new Check_network(connectivityManager);
                network_state = cn.get_network_state();

                error_Checking();

            }
        });
    }

    private void error_Checking() {
        if (!network_state) {
            Toast.makeText(LoginActivity.this, "Please connect to the newtork", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "請輸入Email", Toast.LENGTH_SHORT).show();
        } else if (pw.isEmpty()) {
            Toast.makeText(LoginActivity.this, "請輸入Password", Toast.LENGTH_SHORT).show();
        } else {
            Login();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    //when user click forget  password button
    public void onClick(View v) {
        Intent forget_password = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(forget_password); //go to forget password page
    }

    private void Login() {
        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent menu = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(menu);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}
