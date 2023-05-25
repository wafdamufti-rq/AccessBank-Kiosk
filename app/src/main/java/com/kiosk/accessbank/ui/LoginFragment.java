package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.databinding.FragmentLoginBinding;
import com.kiosk.accessbank.viewmodel.MainViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private MainViewModel viewModel;
    private FragmentLoginBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.clearState();
        initInput();
        initObserver();


    }

    private void initObserver() {



        viewModel.loginLiveData.observe(getViewLifecycleOwner(), userApiResponse -> {
            if (userApiResponse != null) {
               NavHostFragment.findNavController(this).navigate( LoginFragmentDirections.actionLoginFragmentToVerificationFragment());
            }
        });

    }



    @NonNull
    private View.OnClickListener inputTrigger(String s) {
        return v -> {
            if (s.equals("back")) {
                Editable text = Objects.requireNonNull(binding.textInputAccountNumber.getEditText()).getText();
                Objects.requireNonNull(binding.textInputAccountNumber.getEditText()).setText(text.delete(text.length() - 1, text.length()));
                return;
            }
            Objects.requireNonNull(binding.textInputAccountNumber.getEditText()).append(s);
        };
    }




    private void initInput() {
        binding.inputNumberLayout.buttonOne.setOnClickListener(inputTrigger("1"));
        binding.inputNumberLayout.buttonTwo.setOnClickListener(inputTrigger("2"));
        binding.inputNumberLayout.buttonThree.setOnClickListener(inputTrigger("3"));
        binding.inputNumberLayout.buttonFour.setOnClickListener(inputTrigger("4"));
        binding.inputNumberLayout.buttonFive.setOnClickListener(inputTrigger("5"));
        binding.inputNumberLayout.buttonSix.setOnClickListener(inputTrigger("6"));
        binding.inputNumberLayout.buttonSeven.setOnClickListener(inputTrigger("7"));
        binding.inputNumberLayout.buttonEight.setOnClickListener(inputTrigger("8"));
        binding.inputNumberLayout.buttonNine.setOnClickListener(inputTrigger("9"));
        binding.inputNumberLayout.buttonZero.setOnClickListener(inputTrigger("0"));
        binding.inputNumberLayout.buttonRemove.setOnClickListener(inputTrigger("back"));

        binding.inputNumberLayout.buttonNext.setOnClickListener(v -> {
            if (binding.textInputAccountNumber.getEditText().getText().toString().length() > 0){
                viewModel.login(Long.parseLong(Objects.requireNonNull(binding.textInputAccountNumber.getEditText()).getText().toString()));
            }else{
                binding.textInputAccountNumber.setError("Please fill your account number");
            }
        });

        binding.textInputAccountNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.textInputAccountNumber.setError(null);
            }
        });
    }
}
