package com.example.nutrobud.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ContactViewModel {

    private MutableLiveData<String> mText;

    public ContactViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the contact us fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}