package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
        binding = FragmentUpdateInformationBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.loadAccount();
        viewModel.getUpdateType();

        viewModel.selectedAccountLiveData.observe(getViewLifecycleOwner(), account -> {
            if (account != null){
                binding.textDate.setText(account.getDate());
                binding.textAccountNo.setText(account.getNumber());
                binding.textPhone.setText(account.getPhoneNumber());
                binding.textEmail.setText(account.getEmail());

            }


        });


        viewModel.updateTypeLiveData.observe(getViewLifecycleOwner(), updateType -> {
            String type = updateType == UpdateType.EMAIL ? getString(R.string.email) : getString(R.string.phone_number);
            binding.title.setText(getResources().getString(R.string.enter_your_new_type, type));
            binding.labelNewS.setText(getResources().getString(R.string.new_s, type));
        });
        viewModel.submittedLiveData.observe(getViewLifecycleOwner(), aBoolean ->
        {
            if (aBoolean != null  && aBoolean) {
                NavHostFragment.findNavController(UpdateInformationFragment.this).navigate(UpdateInformationFragmentDirections.actionUpdateInformationFragmentToConfirmationUpdateInfoFragment());
            }
        });

        binding.buttonSubmit.setOnClickListener(v -> viewModel.submitUpdate(Objects.requireNonNull(binding.textInput.getEditText()).getText().toString()));
        binding.buttonCancel.setOnClickListener(v -> NavHostFragment.findNavController(UpdateInformationFragment.this).navigate(UpdateInformationFragmentDirections.actionUpdateInformationFragmentToLoginFragment()));
    }
}
