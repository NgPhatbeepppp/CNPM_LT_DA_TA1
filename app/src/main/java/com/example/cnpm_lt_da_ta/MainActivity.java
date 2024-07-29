package com.example.cnpm_lt_da_ta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.bumptech.glide.Glide;
import com.example.cnpm_lt_da_ta.Adapter.DictionaryAdapter;
import com.example.cnpm_lt_da_ta.Course.Course;
import com.example.cnpm_lt_da_ta.Adapter.CourseAdapter;
import com.example.cnpm_lt_da_ta.Course.Dictionary;
import com.example.cnpm_lt_da_ta.DAO.CourseDAO;
import com.example.cnpm_lt_da_ta.DAO.DictionaryDAO;
import com.example.cnpm_lt_da_ta.Lesson.LessonManagementActivity;
import com.example.cnpm_lt_da_ta.Manager.FlashcardManagementActivity;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardManagementAdapter;
import com.example.cnpm_lt_da_ta.User.LoginActivity;
import com.example.cnpm_lt_da_ta.User.UserManagementActivity;
import com.example.cnpm_lt_da_ta.fragment.HomeFragment;
import com.example.cnpm_lt_da_ta.fragment.MyProfileFragment;
import com.example.cnpm_lt_da_ta.fragment.NewsFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private DrawerLayout mDL;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_PROFILE = 3;
    private  int mCurrentFRAGMENT =1;
    private DatabaseReference mDatabase;
    private RecyclerView rvCourseList, rvDictionary;
    private CourseDAO courseDAO;
    private CourseAdapter courseAdapter;
    private DictionaryDAO dictionaryDAO;
    private DictionaryAdapter dictionaryAdapter;
    private DatabaseHelper dbHelper;
    private Button btnCourse, btnDictionary;
    private TextInputEditText searchEditText;
    private boolean isCourseMode = true;
    private TextWatcher searchTextWatcher;
    @Override
    protected void onResume() { // Chuyển khởi tạo CourseDAO, DictionaryDAO vào onResume()
        super.onResume();
        courseDAO = new CourseDAO(this);
        courseDAO.open();
        // Lấy dữ liệu và cập nhật adapter
        List<Course> courses = courseDAO.getAllCourses();
        courseAdapter = new CourseAdapter(courses, this);
        Log.d("MainActivity", "onResume - Courses fetched: " + courses.size()); // Log số lượng khóa họ
        rvCourseList.setAdapter(courseAdapter);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));

        dictionaryDAO = new DictionaryDAO(this);
        dictionaryDAO.open();
        btnCourse.performClick();
        List<Dictionary> dictionaries = new ArrayList<>();
        dictionaryAdapter = new DictionaryAdapter(dictionaries, this);
        rvDictionary.setAdapter(dictionaryAdapter);
        rvDictionary.setLayoutManager(new LinearLayoutManager(this ));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDL = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Khởi tạo rv
        rvCourseList = findViewById(R.id.rvCourseList);
        rvDictionary = findViewById(R.id.rvDictionary);
        //Khởi tạo các nút, thanh tìm kiếm
        btnCourse = findViewById(R.id.btnCourse);
        btnDictionary = findViewById(R.id.btnDictionary);
        searchEditText = findViewById(R.id.searchEditText);
        searchTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                if (isCourseMode) {
                    // Tìm kiếm khóa học
                    List<Course> searchResults = courseDAO.searchCourses(query);
                    courseAdapter.updateData(searchResults);
                } else {
                    // Tìm kiếm từ vựng
                    List<Dictionary> searchResults = dictionaryDAO.searchWords(query);
                    dictionaryAdapter.updateData(searchResults);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Gán TextWatcher cho searchEditText
        searchEditText.addTextChangedListener(searchTextWatcher);

        //Khởi tạo các navigation
        NavigationView navigationView = findViewById(R.id.navigation_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this); // Sử dụng phương thức riêng

        // xử lý bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment(), false);
                rvCourseList.setVisibility(View.VISIBLE);
                mCurrentFRAGMENT = FRAGMENT_HOME;
                return true;
            } else if (itemId == R.id.news) {
                rvCourseList.setVisibility(View.GONE);
                replaceFragment(new NewsFragment(), true);
                mCurrentFRAGMENT = FRAGMENT_NEWS;
                return true;
            } else if (itemId == R.id.user) {
                rvCourseList.setVisibility(View.GONE);
                replaceFragment(new MyProfileFragment(), true);
                mCurrentFRAGMENT = FRAGMENT_PROFILE;
                return true;
            } else if (itemId == R.id.back) {
                onBackPressed();
                return true;
            } else {
                return false;
            }
        });

        // Xử lý hiển thị theo "role"
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

        //Button Course
        btnCourse.setOnClickListener(v -> {
            searchEditText.setHint("Tìm kiếm khóa học");
            rvCourseList.setVisibility(View.VISIBLE);
            rvDictionary.setVisibility(View.GONE);
            isCourseMode = true;

            // Lấy dữ liệu khóa học và cập nhật CourseAdapter
            List<Course> courses = courseDAO.getAllCourses();
            courseAdapter.updateData(courses);

            // Xóa nội dung của thanh tìm kiếm và ẩn bàn phím
            searchEditText.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        });

        //Button Dictionary
        btnDictionary.setOnClickListener(v -> {
            searchEditText.setHint("Tìm kiếm từ vựng");
            rvCourseList.setVisibility(View.GONE);
            rvDictionary.setVisibility(View.VISIBLE);
            isCourseMode = false;

            // Xóa nội dung của thanh tìm kiếm và ẩn bàn phím
            searchEditText.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        });
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
                replaceFragment(new HomeFragment(), false);
                mCurrentFRAGMENT = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_profile) {
            replaceFragment(new MyProfileFragment(), true);
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
            Intent intent = new Intent(MainActivity.this, FlashcardManagementActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_user_management) {
            // Chuyển hướng đến UserManagementActivity
            Intent intent = new Intent(MainActivity.this, UserManagementActivity.class);
            startActivity(intent);
        }

        mDL.closeDrawer(GravityCompat.END);
        return true;
    }




    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        else {
            // Xóa tất cả các Fragment trong back stack trừ HomeFragment
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        transaction.commit();

        if (fragment instanceof MyProfileFragment || fragment instanceof NewsFragment) {
            btnCourse.setVisibility(View.GONE);
            btnDictionary.setVisibility(View.GONE);
            searchEditText.setVisibility(View.GONE);
        } else {
            btnCourse.setVisibility(View.VISIBLE);
            btnDictionary.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
        }
    }




    @Override
    public void onBackPressed() {
        if (mDL.isDrawerOpen(GravityCompat.END)) {
            mDL.closeDrawer(GravityCompat.END);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Nếu có fragment trong back stack, quay lại fragment trước đó
                fragmentManager.popBackStack();

                // Kiểm tra nếu đã quay lại HomeFragment, hiển thị các button
                if (mCurrentFRAGMENT == FRAGMENT_HOME) {
                    btnCourse.setVisibility(View.VISIBLE);
                    btnDictionary.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.VISIBLE);
                }
            } else {
                // Nếu không có fragment nào trong back stack, thoát Activity
                super.onBackPressed();
            }
        }
    }


    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0); // Lấy header view
        TextView nameTextView = headerView.findViewById(R.id.nav_header_name);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);
        ImageView imgAvatar = headerView.findViewById(R.id.nav_header_avatar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uri photoUrl = user.getPhotoUrl();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.getValue(String.class);
                    nameTextView.setText(name);

                } else {
                    nameTextView.setText("Chưa có tên"); // Hoặc giá trị mặc định khác
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("MainActivity", "Failed to read name.", databaseError.toException());
            }
        });

        // Lấy ảnh đại diện từ Firebase (nếu có)
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).error(R.drawable.default_user).into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.default_user);
        }
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