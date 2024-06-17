package com.example.ass_restadr;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class main1 extends AppCompatActivity {
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main1);
        viewPager2 = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.btnNav);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        frameLayout = findViewById(R.id.frameLayout);

        // Kiểm tra vai trò của người dùng và hiển thị frm_category nếu là admin
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (currentUserEmail != null && currentUserEmail.equals("admin@gmail.com")) {
            // Hiển thị frm_category
            bottomNavigationView.getMenu().findItem(R.id.botton_2).setVisible(true);
        } else {
            // Ẩn frm_category
            bottomNavigationView.getMenu().findItem(R.id.botton_2).setVisible(false);
        }
        if(savedInstanceState == null){
            frm_home frmHome1 =new frm_home();
            replaceFrg(frmHome1);
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                frameLayout.setVisibility(View.VISIBLE);
                viewPager2.setVisibility(View.GONE);
                if (menuItem.getItemId() == R.id.botton_home) {
                    frm_home frmHome = new frm_home();
                    replaceFrg(frmHome);
                }
                if (menuItem.getItemId() == R.id.botton_1) {
                    bill bill1 = new bill();
                    replaceFrg(bill1);
                }
                if (menuItem.getItemId() == R.id.botton_3) {
                    frm_category category = new frm_category();
                    replaceFrg(category);
                }
                if (menuItem.getItemId() == R.id.botton_2) {
                    Person person = new Person();
                    replaceFrg(person);
                }

                return false;
            }
        });
    }

    public void replaceFrg(Fragment frg) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, frg).commit();
    }
}