package com.lpk806studio.ive_st_cafe;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lpk806studio.ive_st_cafe.Model.Food;

import org.w3c.dom.Text;

public class FoodDetailActivity extends AppCompatActivity {
    TextView foodname,foodprice,counttxt;
    Button btn_add,btn_plus,btn_subtract;
    int count = 1;
    double price,total;
    String FoodId ="";
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        btn_add = findViewById(R.id.btn_add);
        btn_plus = findViewById(R.id.btn_plus);
        btn_subtract = findViewById(R.id.btn_subtract);
        counttxt = findViewById(R.id.detail_count);
        foodname = findViewById(R.id.detail_name);
        foodprice =findViewById(R.id.detail_price);

        counttxt.setText("1");




        if (getIntent() !=null) {
            FoodId = getIntent().getStringExtra("FoodItemId");

        }
        if (!FoodId.isEmpty()  && FoodId != null){
            loadFood(FoodId);
        }


        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                counttxt.setText(""+count);
                total = count*price;
                foodprice.setText("$"+total);

            }
        });

        btn_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count>1){
                    count--;
                    counttxt.setText(""+count);
                    total = count*price;
                    foodprice.setText("$"+total);
                }else{
                    Snackbar.make(v, "數量不能為低於1和負數", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "add buttton", Snackbar.LENGTH_LONG).show();
            }
        });
    }



    private void loadFood(final String foodId) {
        ValueEventListener foodListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.getValue(Food.class);
                foodprice.setText("$" + food.getPrice());
                foodname.setText(food.getName());
                price = Double.parseDouble(food.getPrice());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FoodDetailActivity.this, "Failed to load name",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference("FoodItem").child(foodId);


        mDatabase.addValueEventListener(foodListener);
    }
}
