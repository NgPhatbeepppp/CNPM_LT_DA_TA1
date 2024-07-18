package com.example.cnpm_lt_da_ta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


import com.bumptech.glide.Glide;
import com.example.cnpm_lt_da_ta.Course.Course;
import com.example.cnpm_lt_da_ta.Adapter.CourseAdapter;
import com.example.cnpm_lt_da_ta.DAO.CourseDAO;
import com.example.cnpm_lt_da_ta.Lesson.LessonManagementActivity;
import com.example.cnpm_lt_da_ta.User.LoginActivity;
import com.example.cnpm_lt_da_ta.User.UserManagementActivity;
import com.example.cnpm_lt_da_ta.fragment.HomeFragment;
import com.example.cnpm_lt_da_ta.fragment.MyProfileFragment;
import com.example.cnpm_lt_da_ta.fragment.NewsFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private DrawerLayout mDL;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_PROFILE = 3;
    private  int mCurrentFRAGMENT =1;
    private DatabaseReference mDatabase;
    private RecyclerView rvCourseList;
    private CourseDAO courseDAO;
    private CourseAdapter courseAdapter;
    private DatabaseHelper dbHelper;
    @Override
    protected void onResume() { // Chuyển khởi tạo CourseDAO vào onResume()
        super.onResume();
        courseDAO = new CourseDAO(this);
        courseDAO.open();
        List<Course> courses = courseDAO.getAllCourses();
        courseAdapter = new CourseAdapter(courses, this);
        rvCourseList.setAdapter(courseAdapter);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDL = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        rvCourseList = findViewById(R.id.rvCourseList);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this); // Sử dụng phương thức riêng


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replacefragment(new HomeFragment());
                rvCourseList.setVisibility(View.VISIBLE);
                mCurrentFRAGMENT = FRAGMENT_HOME;
                return true;
            } else if (itemId == R.id.news) {
                rvCourseList.setVisibility(View.GONE);
                replacefragment(new NewsFragment());
                mCurrentFRAGMENT = FRAGMENT_NEWS;
                return true;
            } else if (itemId == R.id.user) {
                rvCourseList.setVisibility(View.GONE);
                replacefragment(new MyProfileFragment());
                mCurrentFRAGMENT = FRAGMENT_PROFILE;
                return true;
            } else if (itemId == R.id.back) {
              onBackPressed();
              return true;
            } else {
                // Xử lý trường hợp không có itemId nào khớp
                return false;
            }
        });





        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName(); // Lấy tên (có thể null nếu chưa đặt)
            String email = user.getEmail();

            // Cập nhật thông tin vào header của Navigation Drawer
            updateNavHeader();
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.child("role").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.getValue(String.class); // Lấy giá trị role
                        updateNavigationDrawerItems(userRole);

                        Log.d("MainActivity", "Vai trò người dùng: " + userRole);
                    } else {
                        // Xử lý trường hợp dữ liệu role không tồn tại
                        Log.w("MainActivity", "User role data not found for userId: " + userId);
                    }
                }
            });
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
            replacefragment(new MyProfileFragment());
            mCurrentFRAGMENT = FRAGMENT_PROFILE;
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
        // Chuyển hướng đến UserManagementActivity
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
        super.onBackPressed();
        if (mDL.isDrawerOpen(GravityCompat.END)) {
            mDL.closeDrawer(GravityCompat.END);
        } else {
            // Navigate back to the previous screen
            getSupportFragmentManager().popBackStack();
        }
    }

    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0); // Lấy header view

        TextView nameTextView = headerView.findViewById(R.id.nav_header_name);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);
        ImageView imgAvarta = headerView.findViewById(R.id.nav_header_avatar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String email =user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        nameTextView.setText(name);
        emailTextView.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.default_user).into(imgAvarta);
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