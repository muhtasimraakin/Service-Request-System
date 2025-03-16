package com.example.srs.ui.providerProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProviderProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProviderProfileViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}