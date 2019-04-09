package com.lpk806studio.ive_st_cafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_signupPage,btn_loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_loginPage = findViewById(R.id.btn_loginPage);
        btn_signupPage= findViewById(R.id.btn_signupPage);

        btn_signupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupPage = new Intent(MainActivity.this,SigupActivity.class);
                startActivity(signupPage);
            }
        });

        btn_loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginPage);
            }
        });

    }
}
