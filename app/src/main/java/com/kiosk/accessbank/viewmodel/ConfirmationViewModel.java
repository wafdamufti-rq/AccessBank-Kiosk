package com.kiosk.accessbank.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class ConfirmationViewModel extends BaseViewModel {

    private SavedStateHandle savedStateHandle;


    @Inject
    public ConfirmationViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }


    private final MutableLiveData<Boolean> _onTimerFinish = new MutableLiveData<>(false);
    public LiveData<Boolean> onTimerFinish = _onTimerFinish;


    public void clearEverything() {

        Single.timer(10, TimeUnit.SECONDS).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                _onTimerFinish.postValue(true);

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });
    }

}
