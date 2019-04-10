package com.lpk806studio.ive_st_cafe;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
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

public class CartActivity extends AppCompatActivity {
    FirebaseUser user;
    ListView cartList;
    FirebaseListAdapter<Cart> adapter;
    double total,itemPrice;
    TextView totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartList = findViewById(R.id.cartList);
        totalPrice = findViewById(R.id.totalPrice);

        //get user id to map the cart item
        user = FirebaseAuth.getInstance().getCurrentUser();
        loadCartList(user.getUid());

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
                TextView name = (TextView)v.findViewById(R.id.cart_name);
                final TextView price = (TextView)v.findViewById(R.id.cart_price);
                TextView count = (TextView)v.findViewById(R.id.cart_count);
                final Double numCount = Double.parseDouble(model.getCount());
                name.setText(model.getName());
                price.setText("$" + model.getPrice());
                count.setText(model.getCount());


                //check box
                final CheckBox checkBox = (CheckBox)v.findViewById(R.id.checkbox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (checkBox.isChecked()){
                            //user select the item
                            itemPrice = Double.parseDouble(model.getPrice());
                            itemPrice *= numCount;
                            totaPrice(itemPrice);
                        }else{
                            //user not select the item
                            itemPrice = Double.parseDouble(model.getPrice());
                            Double a = (itemPrice *= numCount)*-1;
                            totaPrice(a);
                        }
                    }
                });

                //delete button
                Button btn_delete = (Button)v.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("Cart").child(adapter.getRef(position).getKey()).removeValue();
                        Snackbar.make(v, "刪除成功", Snackbar.LENGTH_LONG).show();
                    }
                });
                //end of delete button
            }
        };
        cartList.setAdapter(adapter);


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
