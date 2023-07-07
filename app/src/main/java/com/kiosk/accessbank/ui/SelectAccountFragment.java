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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiosk.accessbank.databinding.FragmentSelectAccountBinding;
import com.kiosk.accessbank.listener.OnAccountListener;
import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.model.Account;
import com.kiosk.accessbank.source.model.AccountSummary;
import com.kiosk.accessbank.source.model.CustomerAccount;
import com.kiosk.accessbank.ui.adapter.ButtonAccountAdapter;
import com.kiosk.accessbank.util.Constants;
import com.kiosk.accessbank.viewmodel.MainViewModel;
import com.kiosk.accessbank.viewmodel.SelectAccountViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SelectAccountFragment extends Fragment implements OnAccountListener {


    private FragmentSelectAccountBinding binding;

    private ButtonAccountAdapter adapter;

    private MainViewModel viewModel;

    private SelectAccountViewModel selectAccountViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        selectAccountViewModel = new ViewModelProvider(this).get(SelectAccountViewModel.class);
        binding = FragmentSelectAccountBinding.inflate(inflater,container,false);
        selectAccountViewModel.loadCustomers();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ButtonAccountAdapter(this);
        binding.recyclerview.setAdapter(adapter);
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        selectAccountViewModel.customerAccountsLiveData.observe(getViewLifecycleOwner(), arrayListApiResponse -> {
            binding.animationViewLoading.setVisibility(View.GONE);

            if (arrayListApiResponse != null) {
                GridLayoutManager layoutManager = (GridLayoutManager) binding.recyclerview.getLayoutManager();
                if (layoutManager != null) {
                    layoutManager.setSpanCount(arrayListApiResponse.size() > 2 ? 3 : arrayListApiResponse.size());
                }
                binding.recyclerview.setLayoutManager(layoutManager);
                adapter.setData(arrayListApiResponse);
            }
        });

        selectAccountViewModel.accountLiveData.observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String customerAccount) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < customerAccount.length(); i++) {
                    stringBuilder.append(i == 0 || i == 1 || i == customerAccount.length()-1 || i == customerAccount.length() -2 ? customerAccount.charAt(i) : "X");
                }
                binding.textNumber.setText(stringBuilder);

            }
        });

    }

    @Override
    public void onClick(AccountSummary data) {
//        viewModel.setSelectedAccount(data);
        NavHostFragment.findNavController(this).navigate(SelectAccountFragmentDirections.actionSelectAccountFragmentToSelectServiceFragment(data,getArguments().getParcelable(Constants.ACCOUNT_EXTRA)));
    }
}
