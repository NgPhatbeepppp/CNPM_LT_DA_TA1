package com.example.cnpm_lt_da_ta.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable {
    private String userId;
    private String email;
    private String role;
    private String name;
    private String phone;

    public User() {
    }

    public User(String userId, String email, String role, String name, String phone) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.name = name;
        this.phone =phone;
    }
    // Constructor for Parcelable
    protected User(Parcel in) {
        userId = in.readString();
        email = in.readString();
        role = in.readString();
        name = in.readString();
        phone = in.readString();
    }
    public  String getUserId() {return userId; }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }

    // Setters
    public  void setUserId(String userId) {this.userId = userId;}
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.name = phone;
    }

    // Parcelable Implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);

        dest.writeString(email);
        dest.writeString(role);

        dest.writeString(name);
        dest.writeString(phone);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel
                in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new
            User[size];
        }
    };
}
