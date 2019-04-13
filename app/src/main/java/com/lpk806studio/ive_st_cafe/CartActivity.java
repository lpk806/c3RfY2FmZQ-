package com.lpk806studio.ive_st_cafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lpk806studio.ive_st_cafe.Model.Cart;
import com.lpk806studio.ive_st_cafe.Model.Order;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class CartActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseListAdapter<Cart> adapter;
    double total,itemPrice;
    TextView totalPrice;
    Button btn_order;
    String totalName="",itemname,itemcount,key;
    ArrayList keys;
    int j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        totalPrice = findViewById(R.id.totalPrice);
        btn_order = findViewById(R.id.btn_order);
        //get user id to map the cart item
        user = FirebaseAuth.getInstance().getCurrentUser();

        keys  = new ArrayList();
        j =0;
        loadCartList(user.getUid());

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = firebaseDatabase.getReference();
                Random x = new Random();
                String id = Integer.toString(x.nextInt(9999));
                String status = "待付款";
                String uid = user.getUid();
                String orderPrice = Double.toString(total);
                Order order = new Order(id,uid,status,totalName,orderPrice);
                ref.child("Order").child(id).setValue(order);


                for(int i =0;i<keys.size();i++){
                    String cartId = keys.get(i).toString();
                    firebaseDatabase.getReference().child("Cart").child(cartId).removeValue();
                }

                Intent payment = new Intent(CartActivity.this,PaymentActivity.class);
                payment.putExtra("price",orderPrice);
                payment.putExtra("OrderId",id);
                startActivity(payment);
            }
        });
    }

    private void loadCartList(String uid) {
        final Query query = FirebaseDatabase.getInstance()
                .getReference("Cart")
                .orderByChild("uid")
                .equalTo(uid);

        FirebaseListOptions<Cart> options = new FirebaseListOptions.Builder<Cart>()
                .setQuery(query,Cart.class)
                .setLayout(R.layout.cart_list)
                .build();

        adapter = new FirebaseListAdapter<Cart>(options) {
            @Override
            protected void populateView(View v, final Cart model, final int position) {
                TextView name = v.findViewById(R.id.cart_name);
                final TextView price = v.findViewById(R.id.cart_price);
                TextView count = v.findViewById(R.id.cart_count);
                final Double numCount = Double.parseDouble(model.getCount());
                name.setText(model.getName());
                price.setText("$" + model.getPrice());
                count.setText("x" + model.getCount());itemPrice = Double.parseDouble(model.getPrice());
                itemname = model.getName();
                itemcount = model.getCount();
                key = adapter.getRef(position).getKey();
                add_keyList(key);

                itemPrice *= numCount;
                totaPrice(itemPrice);
                String uniPrice = Double.toString(itemPrice);
                totalNamelist(itemname,itemcount,uniPrice);

                //delete button
                Button btn_delete = (Button)v.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("Cart").child(adapter.getRef(position).getKey()).removeValue();
                        Snackbar.make(v, "刪除成功", Snackbar.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());

                    }
                });
                //end of delete button
            }
        };
        ListView cartList = findViewById(R.id.cartList);
        cartList.setAdapter(adapter);
        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CartActivity.this, "on9", Toast.LENGTH_LONG).show();

//                TextView Tname = (TextView)view.findViewById(R.id.cart_name);
//                TextView Tcount = (TextView)view.findViewById(R.id.cart_count);
//                String name = Tname.getText().toString();
//                String count = Tcount.getText().toString();
//
//                //craete dialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
//                builder.setTitle("123");
//
//                builder.setCancelable(true);
//                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(CartActivity.this,"save button",Toast.LENGTH_SHORT).show();
//                        dialog.cancel();
//                    }
//                });
//                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(CartActivity.this,"delete button",Toast.LENGTH_SHORT).show();
//                        dialog.cancel();
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(CartActivity.this,"cancel button",Toast.LENGTH_SHORT).show();
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });

    }

    private void add_keyList(String key) {
        keys.add(key);
    }


    private void totalNamelist(String itemname,String itemcount,String uniprice) {
        totalName += (itemname +"\t" +itemcount+"\t$" +uniprice + "\n");
    }

    private void totaPrice(double itemPrice) {
        total += itemPrice;
        totalPrice.setText("Total $"+total);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
