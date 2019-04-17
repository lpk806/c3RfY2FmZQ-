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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lpk806studio.ive_st_cafe.Model.Check_network;
import com.lpk806studio.ive_st_cafe.Model.User;

public class SigupActivity extends AppCompatActivity {
    private EditText email,username,password,check_password;
    private Button btn_signup;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private String name, user, pw, re_pw;
    private boolean network_state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);

        username = findViewById(R.id.txt_user_name);
        email = findViewById(R.id.txt_user_email);
        password = findViewById(R.id.txt_password);
        check_password = findViewById(R.id.check_password);
        btn_signup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                user = email.getText().toString();
                pw = password.getText().toString();
                re_pw = check_password.getText().toString();

                //get newtowk state
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                Check_network cn = new Check_network (connectivityManager);
                network_state = cn.get_network_state();

                error_Checking();
            }
        });
    }

    private void error_Checking() {
        if (!network_state) {
            Toast.makeText(SigupActivity.this, "Please connect to the newtork", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            Toast.makeText(SigupActivity.this, "Please input your username", Toast.LENGTH_SHORT).show();
        } else if (user.isEmpty()) {
            Toast.makeText(SigupActivity.this, "Please input your email address", Toast.LENGTH_SHORT).show();
        } else if (pw.isEmpty()) {
            Toast.makeText(SigupActivity.this, "Please input your password", Toast.LENGTH_SHORT).show();
        } else if (re_pw.isEmpty()) {
            Toast.makeText(SigupActivity.this, "Please input Re-enter password", Toast.LENGTH_SHORT).show();
        } else if (!pw.equals(re_pw)) {
            Toast.makeText(SigupActivity.this, "The Password and Re-enter password is not equal", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE); //progressbar display
            sigup(); //if no error, go sign up
        }
    }

    private void sigup() {
        mAuth.createUserWithEmailAndPassword(user, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            writeNewUser(user.getUid(), user.getEmail());
                            Intent menu = new Intent(SigupActivity.this,MenuActivity.class);
                            startActivity(menu);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(SigupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void writeNewUser(String uid, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(uid).setValue(user);
    }
}
