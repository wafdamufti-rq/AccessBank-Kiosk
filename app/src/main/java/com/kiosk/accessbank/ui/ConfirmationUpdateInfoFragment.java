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

import com.kiosk.accessbank.databinding.FragmentConfirmationUpdateInfoBinding;
import com.kiosk.accessbank.viewmodel.ConfirmationViewModel;
import com.kiosk.accessbank.viewmodel.MainViewModel;

public class ConfirmationUpdateInfoFragment extends Fragment {

    private MainViewModel viewModel;

    private ConfirmationViewModel confirmationViewModel;

    private FragmentConfirmationUpdateInfoBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        confirmationViewModel = new ViewModelProvider(this).get(ConfirmationViewModel.class);
        binding = FragmentConfirmationUpdateInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        confirmationViewModel.clearEverything();

        confirmationViewModel.onTimerFinish.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean !=null && aBoolean){
                viewModel.clearState();
                NavHostFragment.findNavController(ConfirmationUpdateInfoFragment.this).navigate(ConfirmationUpdateInfoFragmentDirections.actionConfirmationUpdateInfoFragmentToLoginFragment());
            }

        });

    }
}
