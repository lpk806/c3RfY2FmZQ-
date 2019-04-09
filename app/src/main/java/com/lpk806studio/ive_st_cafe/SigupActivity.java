package com.lpk806studio.ive_st_cafe;

import android.content.Intent;
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
import com.lpk806studio.ive_st_cafe.Model.User;

public class SigupActivity extends AppCompatActivity {
    private EditText email,username,password,check_password;
    private Button btn_signup;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;

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
                String name = username.getText().toString();
                String user = email.getText().toString();
                String password1 = password.getText().toString();
                String password2 = check_password.getText().toString();
                if (user.isEmpty() || name.isEmpty()){
                    Snackbar.make(v, "請輸入Email and name", Snackbar.LENGTH_LONG).show();
                }else if(password1.isEmpty() && password2.isEmpty()){
                    Snackbar.make(v, "請輸入Password", Snackbar.LENGTH_LONG).show();
                }else if(password1.equals(password2)){
                   sigup(user,password2,name);
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(v, "兩次密碼不相同", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sigup(final String user, String password2, final String name) {
        mAuth.createUserWithEmailAndPassword(user, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            writeNewUser(user.getUid(), name, user.getEmail());
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

    private void writeNewUser(String uid, String username, String email) {
        User user = new User(username, email);
        mDatabase.child("users").child(uid).setValue(user);
    }
}
