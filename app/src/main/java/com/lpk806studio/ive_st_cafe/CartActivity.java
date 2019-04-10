package com.lpk806studio.ive_st_cafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lpk806studio.ive_st_cafe.Model.Cart;

public class CartActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseUser user;
    ListView cartList;
    FirebaseListAdapter<Cart> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartList = findViewById(R.id.cartList);

        user = FirebaseAuth.getInstance().getCurrentUser();

        loadCartList(user.getUid());
    }

    private void loadCartList(String uid) {
        Query query = FirebaseDatabase.getInstance()
                .getReference("Cart")
                .orderByChild("uid")
                .equalTo(uid);

        FirebaseListOptions<Cart> options = new FirebaseListOptions.Builder<Cart>()
                .setQuery(query,Cart.class)
                .setLayout(R.layout.cart_list)
                .build();

        adapter = new FirebaseListAdapter<Cart>(options) {
            @Override
            protected void populateView(View v, Cart model, int position) {
                TextView name = findViewById(R.id.cart_name);
                TextView price = findViewById(R.id.cart_price);
                TextView count = findViewById(R.id.cart_count);
                name.setText(model.getName());
                price.setText("$" + model.getPrice());
                count.setText(model.getCount());
            }
        };
        cartList.setAdapter(adapter);
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
