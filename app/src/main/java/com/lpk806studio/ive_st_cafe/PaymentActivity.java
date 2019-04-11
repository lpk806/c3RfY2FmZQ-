package com.lpk806studio.ive_st_cafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity {
    TextView orderID,payment_price;
    Button btn_fps,btn_creditCard;
    String OrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        orderID = findViewById(R.id.orderID);
        payment_price = findViewById(R.id.payment_price);
        btn_creditCard = findViewById(R.id.btn_creditCard);
        btn_fps = findViewById(R.id.btn_fps);

        if (getIntent() !=null) {
            OrderId= getIntent().getStringExtra("OrderId");
            orderID.setText("訂單號:"+OrderId);
            String price= getIntent().getStringExtra("price");
            payment_price.setText("盛惠$"+price);
        }

        btn_creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(OrderId);
            }
        });

        btn_fps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(OrderId);
            }
        });

    }

    private void changeStatus(String OrderId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String paid = "已付款";
        mDatabase.child("Order").child(OrderId).child("status").setValue(paid);
        Toast.makeText(PaymentActivity.this,"付款成功",Toast.LENGTH_SHORT).show();
        Intent order = new Intent(PaymentActivity.this,OrderActivity.class);
        startActivity(order);
    }
}
