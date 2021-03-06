package com.lpk806studio.ive_st_cafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lpk806studio.ive_st_cafe.Model.Category;
import com.lpk806studio.ive_st_cafe.Model.Check_network;
import com.lpk806studio.ive_st_cafe.Model.User;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView email,username;
    private DatabaseReference mUserDatabase;
    private FirebaseUser user;
    FirebaseListAdapter<Category> adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); //progressbar display

        user = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        network_Checking();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(MenuActivity.this,CartActivity.class);
                startActivity(cart);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //create category list
        showCategoryList();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //import Nav header
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        email = headerView.findViewById(R.id.email);


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.username);
                email.setText(user.email);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MenuActivity.this, "Failed to load name",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mUserDatabase.addValueEventListener(userListener);
        //end of nav header



    }

    private void network_Checking() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Check_network cn = new Check_network (connectivityManager);
        boolean network_state = cn.get_network_state();
        if (!network_state) {
            Toast.makeText(MenuActivity.this, "Please connect to the newtork", Toast.LENGTH_SHORT).show();
        }
    }


    public void showCategoryList() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Category");

        FirebaseListOptions<Category> options = new FirebaseListOptions.Builder<Category>()
                .setLayout(android.R.layout.simple_list_item_1)
                .setQuery(query, Category.class)
                .build();

        adapter = new FirebaseListAdapter<Category>(options) {
            @Override
            protected void populateView(View v,Category model, int position) {
                TextView nameText=(TextView)v.findViewById(android.R.id.text1);
                nameText.setText(model.getName());
                progressBar.setVisibility(View.INVISIBLE);//progressbar display=null
            }
        };
        ListView categoryList = findViewById(R.id.categoryList);
        categoryList.setAdapter(adapter);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView)view;
                String categoryName = v.getText().toString();
                Intent food = new Intent(MenuActivity.this,FoodsActivity.class);
                food.putExtra("CName",categoryName);
                food.putExtra("CategoryId",adapter.getRef(position).getKey());
                startActivity(food);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finishAffinity();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent menu = new Intent(MenuActivity.this,MenuActivity.class);
            startActivity(menu);
        } else if (id == R.id.nav_order) {
            Intent order = new Intent(MenuActivity.this,OrderActivity.class);
            startActivity(order);
        }else if(id == R.id.nav_geo){
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:22.365304,114.116329"));
            startActivity(mapIntent);
        }else if(id == R.id.nav_phone){
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:26141088"));
            startActivity(phoneIntent);
        }else if(id == R.id.nav_git){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lpk806/c3RfY2FmZQ-"));
            startActivity(browserIntent);
        }else  if (id == R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent main = new Intent(MenuActivity.this,MainActivity.class);
            startActivity(main);
            Toast.makeText(MenuActivity.this,"你已登出",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
