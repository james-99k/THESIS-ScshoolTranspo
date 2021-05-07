package com.example.schooltranspo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class OperatorHomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem carpool, driver;
    PagerAdapterOperator adapter;
    UserData ud = new UserData();
    TextView navOperatorName;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_homepage);

        Toolbar toolbar = findViewById(R.id.operatorToolbar);
        setSupportActionBar(toolbar);

        pager = findViewById(R.id.operatorViewPager);
        mTabLayout = findViewById(R.id.operatorTabLayout);

        carpool = findViewById(R.id.operatorTabCarpool);
        driver = findViewById(R.id.operatorTabDriver);

        drawerLayout = findViewById(R.id.operatorDrawer);
        navigationView = findViewById(R.id.operatorNavView);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        adapter = new PagerAdapterOperator(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        pager.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        ud = (UserData)getIntent().getSerializableExtra("UD");

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        try{
            setNavigationViewListener();
        }
        catch (Exception e)
        {
            Log.e("TAG", "EXCEPTION CAUGHT WHILE EXECUTING DATABASE TRANSACTION");
            e.printStackTrace();
        }

    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.operatorNavView);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        navOperatorName = header.findViewById(R.id.tvOperatorNameNav);
        navOperatorName.setText(ud.getFullName());

        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView passengerNavPic = findViewById(R.id.operatorImageViewNav);
                Picasso.get().load(uri).into(passengerNavPic);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.OperatorViewProfile){
            Intent i = new Intent(getApplicationContext(), OperatorViewProfileActivity.class);
            i.putExtra("UD",ud);
            startActivity(i);
        }
        if(item.getItemId() == R.id.OperatorAddDriver){
            startActivity(new Intent(getApplicationContext(), OperatorAddDriverActivity.class));
        }
        if(item.getItemId() == R.id.OperatorAddCarpool){
            startActivity(new Intent(getApplicationContext(), OperatorAddCarpoolActivity.class));
        }
        if(item.getItemId() == R.id.OperatorLogout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        return false;
    }
}