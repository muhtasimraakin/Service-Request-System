package com.example.srs.ui.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServicesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ServicesViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}