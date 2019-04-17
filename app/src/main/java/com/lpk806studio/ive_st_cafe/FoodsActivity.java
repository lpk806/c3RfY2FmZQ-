package com.lpk806studio.ive_st_cafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lpk806studio.ive_st_cafe.Model.Food;

public class FoodsActivity extends AppCompatActivity {
    TextView categoryname;
    String category ="";
    FirebaseListAdapter<Food> adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        categoryname = findViewById(R.id.categoryName);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); //progressbar display

        if (getIntent() !=null) {
            category = getIntent().getStringExtra("CategoryId");
            String CName = getIntent().getStringExtra("CName");
            categoryname.setText(CName);
        }
        if (!category.isEmpty()  && category != null){
            loadListFood(category);
        }

    }

    private void loadListFood(String category) {
        Query query = FirebaseDatabase.getInstance()
                .getReference("FoodItem")
                .orderByChild("category")
                .equalTo(category);

        FirebaseListOptions<Food> options = new FirebaseListOptions.Builder<Food>()
                .setLayout(R.layout.foodlist)
                .setQuery(query, Food.class)
                .build();

        adapter = new FirebaseListAdapter<Food>(options) {
            @Override
            protected void populateView(View v, Food model, int position) {
                TextView foodnameText=(TextView)v.findViewById(R.id.list_foodName);
                TextView foodpriceText=(TextView)v.findViewById(R.id.list_foodPrice);
                foodnameText.setText(model.getName());
                foodpriceText.setText("$"+model.getPrice());
                progressBar.setVisibility(View.INVISIBLE);//progressbar display=null
            }
        };
        ListView foodsList = findViewById(R.id.foodsList);
        foodsList.setAdapter(adapter);

        foodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent foodDetail = new Intent(FoodsActivity.this,FoodDetailActivity.class);
                foodDetail.putExtra("FoodItemId",adapter.getRef(position).getKey());
                startActivity(foodDetail);
            }
        });
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
