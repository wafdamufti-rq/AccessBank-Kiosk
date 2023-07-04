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

import com.kiosk.accessbank.databinding.FragmentSelectOptionBinding;
import com.kiosk.accessbank.util.UpdateType;
import com.kiosk.accessbank.viewmodel.MainViewModel;

public class SelectOptionFragment extends Fragment {


    private FragmentSelectOptionBinding binding;

    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectOptionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonUpdatePhone.setOnClickListener(v -> NavHostFragment.findNavController(SelectOptionFragment.this).navigate(SelectOptionFragmentDirections.actionSelectOptionFragmentToUpdateInformationFragment(getArguments().getParcelable("customer_account")).setUpdateType(UpdateType.PHONE)));
        binding.buttonUpdateEmail.setOnClickListener(v -> NavHostFragment.findNavController(SelectOptionFragment.this).navigate(SelectOptionFragmentDirections.actionSelectOptionFragmentToUpdateInformationFragment(getArguments().getParcelable("customer_account")).setUpdateType(UpdateType.EMAIL)));
    }
}
