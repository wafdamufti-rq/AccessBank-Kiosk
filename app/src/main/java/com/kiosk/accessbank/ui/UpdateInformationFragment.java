package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.R;
import com.kiosk.accessbank.databinding.FragmentUpdateInformationBinding;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.util.UpdateType;
import com.kiosk.accessbank.viewmodel.MainViewModel;
import com.kiosk.accessbank.viewmodel.UpdateInformationViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class UpdateInformationFragment extends Fragment {

    private FragmentUpdateInformationBinding binding;

    private MainViewModel viewModel;

    private UpdateInformationViewModel updateInformationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateInformationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        updateInformationViewModel = new ViewModelProvider(this).get(UpdateInformationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initInput();
        initObserver();
        loadData();


        initTitle();

    }

    private void initInput() {
        if ((UpdateType) getArguments().get("updateType") == UpdateType.PHONE ){
            binding.inputForm.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }

    private void loadData() {
//        viewModel.loadAccount();
        updateInformationViewModel.loadAccount();
    }

    private void initObserver() {
        updateInformationViewModel.selectedAccountLiveData.observe(getViewLifecycleOwner(), this::initData);
        viewModel.updateInformationSubmitTrigger.observe(getViewLifecycleOwner(), aBoolean ->

                {
                    if (aBoolean != null && aBoolean) {
                        viewModel.closeUpdateInformationSubmitTrigger();
                        binding.animationViewFacial.setVisibility(View.VISIBLE);
                        updateInformationViewModel.submitUpdate( (UpdateType) getArguments().get("updateType"),Objects.requireNonNull(binding.inputForm.getEditText()).getText().toString());
                    }
                }
         );
        updateInformationViewModel.submittedLiveData.observe(getViewLifecycleOwner(), t ->{
            binding.animationViewFacial.setVisibility(View.GONE);

            if (t.getCode().equals("00")){
                validateSubmit(true);
            }else{
                Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateSubmit(Boolean aBoolean) {
        if (aBoolean != null && aBoolean)
            NavHostFragment.findNavController(UpdateInformationFragment.this).navigate(com.kiosk.accessbank.ui.UpdateInformationFragmentDirections.actionUpdateInformationFragmentToConfirmationUpdateInfoFragment());

    }

    private void initTitle() {
        UpdateType updateType = (UpdateType) getArguments().get("updateType");

        String type = updateType == UpdateType.EMAIL ? getString(R.string.email) : getString(R.string.phone_number);
        binding.title.setText(getResources().getString(R.string.enter_your_new_type, type));
    }

    private void initData(CustomerAccount account) {
        if (account == null) return;

        binding.textDate.setText("-");
        binding.textAccountNo.setText(account.account_no);
        binding.textPhone.setText(account.mobile_no);
        binding.textEmail.setText(account.email_address);

    }
}
