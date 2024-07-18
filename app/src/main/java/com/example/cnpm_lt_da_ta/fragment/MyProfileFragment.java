package com.example.cnpm_lt_da_ta.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cnpm_lt_da_ta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class MyProfileFragment extends Fragment {

    private ImageView imageAvatar;
    private EditText edtFullName, edtEmail, edtPhone;
    private Button btnUpdateProfile;
    private Button btnChooseImage;
    private DatabaseReference userRef;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageAvatar = view.findViewById(R.id.image_view_avatar);
        edtFullName = view.findViewById(R.id.edt_full_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phone);
        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        btnChooseImage = view.findViewById(R.id.btn_choose_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String userId = user.getUid();

            userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("name").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String avatarUrl = snapshot.child("avatar").getValue(String.class);

                        edtFullName.setText(fullName);
                        edtPhone.setText(phone);
                        edtEmail.setText(email);

                        if (avatarUrl != null) {
                            Glide.with(requireContext()).load(avatarUrl).error(R.drawable.default_user).into(imageAvatar);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("My_Profile_Fragment", "Failed to read user data", error.toException());
                }
            });
        }

        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        btnChooseImage.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageAvatar);
        }
    }

    private void updateProfile() {
        String fullName = edtFullName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateUserData(fullName, phone, imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show();
                        updateUserData(fullName, phone, null); // Nếu tải ảnh thất bại, vẫn cập nhật thông tin khác
                    });
        } else {
            updateUserData(fullName, phone, null); // Không thay đổi ảnh
        }
    }

    private void updateUserData(String fullName, String phone, String imageUrl) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("name", fullName);
        updates.put("phone", phone);
        if (imageUrl != null) {
            updates.put("avatar", imageUrl);
        }

        userRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show());
    }
}
