package pt.ulisboa.tecnico.cmov.shopist.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;


    public MyViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModel(mApplication);
    }
}