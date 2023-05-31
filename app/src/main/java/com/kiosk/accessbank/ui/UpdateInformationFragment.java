package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.R;
import com.kiosk.accessbank.databinding.FragmentUpdateInformationBinding;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.util.UpdateType;
import com.kiosk.accessbank.viewmodel.MainViewModel;

import java.util.Objects;

public class UpdateInformationFragment extends Fragment {

    private FragmentUpdateInformationBinding binding;

    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateInformationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initObserver();
        loadData();

    }

    private void loadData() {
        viewModel.loadAccount();
        viewModel.getUpdateType();
    }

    private void initObserver() {
        viewModel.selectedAccountLiveData.observe(getViewLifecycleOwner(), this::initData);
        viewModel.updateInformationSubmitTrigger.observe(getViewLifecycleOwner(), aBoolean ->
                {
                    if (aBoolean != null && aBoolean) {
                        viewModel.submitUpdate(Objects.requireNonNull(binding.inputForm.getEditText()).getText().toString());
                    }
                }
         );
        viewModel.updateTypeLiveData.observe(getViewLifecycleOwner(), this::initTitle);
        viewModel.submittedLiveData.observe(getViewLifecycleOwner(), this::validateSubmit);
    }

    private void validateSubmit(Boolean aBoolean) {
        if (aBoolean != null && aBoolean)
            NavHostFragment.findNavController(UpdateInformationFragment.this).navigate(com.kiosk.accessbank.ui.UpdateInformationFragmentDirections.actionUpdateInformationFragmentToConfirmationUpdateInfoFragment());

    }

    private void initTitle(UpdateType updateType) {
        String type = updateType == UpdateType.EMAIL ? getString(R.string.email) : getString(R.string.phone_number);
        binding.title.setText(getResources().getString(R.string.enter_your_new_type, type));
    }

    private void initData(Account account) {
        if (account == null) return;

        binding.textDate.setText(account.getDate());
        binding.textAccountNo.setText(account.getNumber());
        binding.textPhone.setText(account.getPhoneNumber());
        binding.textEmail.setText(account.getEmail());

    }
}
