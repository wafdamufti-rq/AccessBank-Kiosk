package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.databinding.FragmentSelectServiceBinding;
import com.kiosk.accessbank.listener.OnServiceListener;
import com.kiosk.accessbank.source.model.AccountSummary;
import com.kiosk.accessbank.source.model.Service;
import com.kiosk.accessbank.ui.adapter.ButtonServiceAdapter;
import com.kiosk.accessbank.util.Constants;
import com.kiosk.accessbank.viewmodel.MainViewModel;
import com.kiosk.accessbank.viewmodel.SelectServiceViewModel;

public class SelectServiceFragment extends Fragment implements OnServiceListener {

    private FragmentSelectServiceBinding binding;

    private MainViewModel viewModel;

    private ButtonServiceAdapter adapter;

    private SelectServiceViewModel selectServiceViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectServiceBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        selectServiceViewModel = new ViewModelProvider(this).get(SelectServiceViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ButtonServiceAdapter(this);
        adapter.hideBalance();
        binding.recyclerview.setAdapter(adapter);
        viewModel.loadServices();

        initObserver();
    }

    private void initObserver() {
        viewModel.servicesLiveData.observe(getViewLifecycleOwner(), arrayListApiResponse -> {
            if (arrayListApiResponse != null) {
                adapter.setData(arrayListApiResponse.getData());
            }
        });

        selectServiceViewModel.accountSummaryLiveData.observe(getViewLifecycleOwner(), accountSummary -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < accountSummary.getAccountNo().length(); i++) {
                stringBuilder.append(i == 0 || i == 1 || i == accountSummary.getAccountNo().length()-1 || i == accountSummary.getAccountNo().length() -2 ? accountSummary.getAccountNo().charAt(i) : "X");
            }
            binding.textAccountName.setText(stringBuilder);

        });
    }

    @Override
    public void onClick(Service data) {
        viewModel.setSelectedService(data);
        if (data.getId() == 8)
            NavHostFragment.findNavController(this).navigate(SelectServiceFragmentDirections.actionSelectServiceFragmentToSelectOptionFragment(getArguments().getParcelable(Constants.ACCOUNT_SUMMARY_EXTRA),getArguments().getParcelable(Constants.ACCOUNT_EXTRA)));
    }
}
