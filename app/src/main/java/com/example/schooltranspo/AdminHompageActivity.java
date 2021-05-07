package com.example.schooltranspo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminHompageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

        DrawerLayout drawerLayout;
        ActionBarDrawerToggle toggle;
        NavigationView navigationView;
        ViewPager pager;
        TabLayout mTabLayout;
        TabItem users, feedbacks;
        PagerAdapterAdmin adapter;
        UserData ud = new UserData();
        TextView navAdminName;
        FirebaseAuth firebaseAuth;
        StorageReference storageReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_hompage);

            Toolbar toolbar = findViewById(R.id.adminToolbar);
            setSupportActionBar(toolbar);

            pager = findViewById(R.id.adminViewPager);
            mTabLayout = findViewById(R.id.adminTabLayout);

            users = findViewById(R.id.adminTabUsers);
            feedbacks = findViewById(R.id.adminTabFeedback);

            drawerLayout = findViewById(R.id.adminDrawer);
            navigationView = findViewById(R.id.adminNavView);
            navigationView.setNavigationItemSelectedListener(this);

            toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();

            adapter = new PagerAdapterAdmin(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
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
        NavigationView navigationView = findViewById(R.id.adminNavView);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        navAdminName = header.findViewById(R.id.tvAdminNameNavHeader);
        navAdminName.setText(ud.getFullName());

//        StorageReference profileRef = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/Profile.jpg");
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                ImageView adminNavPic = findViewById(R.id.adminImageViewNav);
//                Picasso.get().load(uri).into(adminNavPic);
//            }
//        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.AdminLogout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        return false;
    }
}