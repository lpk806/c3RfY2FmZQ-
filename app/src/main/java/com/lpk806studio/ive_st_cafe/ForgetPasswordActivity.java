package com.lpk806studio.ive_st_cafe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.lpk806studio.ive_st_cafe.Model.Check_network;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText edit_fp_email;
    Button btn_fp_send;
    private ProgressBar progressBar_fp;
    FirebaseAuth firebaseAuth;
    private String email;
    boolean network_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        edit_fp_email = (EditText)findViewById(R.id.edit_fp_email);
        btn_fp_send = (Button)findViewById(R.id.btn_fp_send);
        progressBar_fp = (ProgressBar) findViewById(R.id.progressBar_fp);

        //initialize the FirebaseAuth instance.
        firebaseAuth = FirebaseAuth.getInstance();

        //send email to user and reset password
        btn_fp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set Var
                email = edit_fp_email.getText().toString();

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
            Toast.makeText(ForgetPasswordActivity.this, "Please connect to the newtork", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(ForgetPasswordActivity.this, "Please input your email address", Toast.LENGTH_SHORT).show();
        } else {
            progressBar_fp.setVisibility(View.VISIBLE); //progressbar display
            send_email(); //if no error, go send email
        }
    }

    private void send_email() {
        //initialize the FirebaseAuth instance.
        firebaseAuth = FirebaseAuth.getInstance();

        //send email
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //if send successful
                            Toast.makeText(ForgetPasswordActivity.this, "email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                            Intent fp_sent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                            startActivity(fp_sent);
                            progressBar_fp.setVisibility(View.INVISIBLE);//progressbar display=null
                        } else {
                            //if send fail
                            Toast.makeText(ForgetPasswordActivity.this, "send failed. Please input correct email.", Toast.LENGTH_SHORT).show();
                            progressBar_fp.setVisibility(View.INVISIBLE);//progressbar display=null
                        }
                    }
                });

    }
}
