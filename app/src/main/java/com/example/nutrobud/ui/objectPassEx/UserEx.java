package com.example.nutrobud.ui.objectPassEx;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEx implements Parcelable {
    private String firstName;
    private String secondName;
    private String email;
    private String password;

    public UserEx(){

    }

    public UserEx(String firstName, String secondName, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
    }

    protected UserEx(Parcel in) {
        firstName = in.readString();
        secondName = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<UserEx> CREATOR = new Creator<UserEx>() {
        @Override
        public UserEx createFromParcel(Parcel in) {
            return new UserEx(in);
        }

        @Override
        public UserEx[] newArray(int size) {
            return new UserEx[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(secondName);
        parcel.writeString(email);
        parcel.writeString(password);
    }
}
