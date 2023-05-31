package com.kiosk.accessbank.ui;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.databinding.FragmentVerificationBinding;
import com.kiosk.accessbank.fingerprint.FingerprintHandler;
import com.kiosk.accessbank.viewmodel.MainViewModel;
import com.zkteco.biometric.FingerprintCaptureListener;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.WithFragmentBindings;

@AndroidEntryPoint
public class VerificationFragment extends Fragment {

    private static final String FINGERPRINT_TAG = "fingerprint";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    @Inject
     FingerprintHandler fingerprintHandler;
    private FragmentVerificationBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        fingerprintHandler.stopScanning();
        fingerprintHandler.closeDevice();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVerificationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fingerprintHandler == null ) fingerprintHandler = new FingerprintHandler(this.getContext());
        fingerprintHandler.openDevice();
        fingerprintHandler.listener(new FingerprintCaptureListener() {
            @Override
            public void captureOK(byte[] bytes) {
                fingerprintHandler.stopScanning();
                fingerprintHandler.closeDevice();

                Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment());
            }

            @Override
            public void captureError(int i) {

            }

            @Override
            public void extractOK(byte[] bytes) {

            }
        });
        initSelection();
        initObserver();


        binding.cardFingerprint.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment());
        });

        if (binding.cardFingerprint.getVisibility() == View.VISIBLE) {
            fingerprintHandler.startScanning();
        }

    }

    private void initObserver() {

    }
    private void initSelection() {
        binding.buttonFacial.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(binding.getRoot());

            binding.layoutFacial.setVisibility(View.VISIBLE);
            binding.layoutFingerprint.setVisibility(View.GONE);
            fingerprintHandler.stopScanning();
        });

        binding.buttonFingerprint.setOnClickListener(v -> {
                    TransitionManager.beginDelayedTransition(binding.getRoot());
                    binding.layoutFacial.setVisibility(View.GONE);
                    binding.layoutFingerprint.setVisibility(View.VISIBLE);
                    fingerprintHandler.startScanning();
                }

        );

    }
}


