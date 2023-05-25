package com.kiosk.accessbank.viewmodel;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

abstract class BaseViewModel extends ViewModel {

    CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCleared() {
        if (!disposable.isDisposed()) disposable.dispose();
        super.onCleared();
    }
}
