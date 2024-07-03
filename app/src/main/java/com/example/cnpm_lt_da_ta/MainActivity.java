package com.example.cnpm_lt_da_ta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cnpm_lt_da_ta.Lesson.LessonManagementActivity;
import com.example.cnpm_lt_da_ta.User.LoginActivity;
import com.example.cnpm_lt_da_ta.User.UserManagementActivity;
import com.example.cnpm_lt_da_ta.fragment.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDL;
    public static final int FRAGMENT_HOME = 1;
    private  int mCurrentFRAGMENT =1;
    private Button btnLesson;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDL = findViewById(R.id.drawer_layout);
        btnLesson = findViewById(R.id.btn_Lesson);
        Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Lấy tham chiếu đến node người dùng
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Lắng nghe sự thay đổi dữ liệu người dùng
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String userRole = dataSnapshot.child("role").getValue(String.class);

                        // Cập nhật header và các item trong Navigation Drawer
                        updateNavHeader(name, email);
                        updateNavigationDrawerItems(userRole);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi
                }
            });
        } else {
            // Xử lý trường hợp người dùng chưa đăng nhập
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
                if (id== R.id.menu_toolbar)
                {
                    mDL.openDrawer(GravityCompat.END);
                }

        return true;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (mCurrentFRAGMENT != FRAGMENT_HOME) {
                replacefragment(new HomeFragment());
                mCurrentFRAGMENT = FRAGMENT_HOME;
            }


        } else if (id == R.id.nav_profile) {
            // Xử lý khi chọn item "My Profile"
        } else if (id == R.id.nav_sign_out) {
            // Xử lý đăng xuất
            FirebaseAuth.getInstance().signOut();

            Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
     else if (id == R.id.nav_lesson_management) {
        // Chuyển hướng đến LessonManagementActivity
        Intent intent = new Intent(MainActivity.this, LessonManagementActivity.class);
        startActivity(intent);
    } else if (id == R.id.nav_user_management) {
        // Chuyển hướng đến UserManagementActivity (bạn cần tự tạo activity này)
        Intent intent = new Intent(MainActivity.this, UserManagementActivity.class);
        startActivity(intent);
    }

        mDL.closeDrawer(GravityCompat.END);
        return true;
    }

    private void replacefragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (mDL.isDrawerOpen(GravityCompat.END))
        {
            mDL.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }

    }
    private void updateNavHeader(String name, String email) {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.nav_header_name);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);

        nameTextView.setText(name != null ? name : "Chưa có tên"); // Hiển thị "Chưa có tên" nếu name là null
        emailTextView.setText(email);
    }
    private void updateNavigationDrawerItems(String userRole) {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        MenuItem lessonManagementItem = menu.findItem(R.id.nav_lesson_management);
        MenuItem userManagementItem = menu.findItem(R.id.nav_user_management);

        if ("admin".equals(userRole)) {
            lessonManagementItem.setVisible(true);
            userManagementItem.setVisible(true);
        } else {
            lessonManagementItem.setVisible(false);
            userManagementItem.setVisible(false);
        }
    }

}