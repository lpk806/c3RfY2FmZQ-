package com.lpk806studio.ive_st_cafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lpk806studio.ive_st_cafe.Model.Order;

public class OrderActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseListAdapter<Order> adapter;
    ListView orderList;
    String Id,checkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderList = findViewById(R.id.orderList);
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadOrderList(user.getUid());

    }

    private void loadOrderList(String uid) {
        Query query = FirebaseDatabase.getInstance()
                .getReference("Order")
                .orderByChild("uid")
                .equalTo(uid);

        FirebaseListOptions<Order> options = new FirebaseListOptions.Builder<Order>()
                .setQuery(query,Order.class)
                .setLayout(R.layout.order_list)
                .build();

        adapter = new FirebaseListAdapter<Order>(options) {
            @Override
            protected void populateView(View v, final Order model, final int position) {
                TextView orderId = v.findViewById(R.id.order_orderId);
                TextView status = v.findViewById(R.id.order_status);
                TextView name = v.findViewById(R.id.order_name);
                TextView total = v.findViewById(R.id.order_total);

                orderId.setText("訂單:"+model.getId());
                status.setText("狀態:"+model.getStatus());
                name.setText(model.getTotalName());
                total.setText("$"+model.getTotalPrice());
//                Id = model.getId();
//                checkStatus = model.getStatus();
            }
        };
        orderList.setAdapter(adapter);
//        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(OrderActivity.this,checkStatus,Toast.LENGTH_LONG).show();
//
//            }
//        });


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
