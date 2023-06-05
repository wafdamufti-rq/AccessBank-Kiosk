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
import com.kiosk.accessbank.facedetection.FaceRecognitionHandler;
import com.kiosk.accessbank.fingerprint.FingerprintHandler;
import com.kiosk.accessbank.util.ToastUtils;
import com.kiosk.accessbank.viewmodel.MainViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VerificationFragment extends Fragment implements FingerprintHandler.FingerprintListener {

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
    
    
    private FaceRecognitionHandler faceRecognitionHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        fingerprintHandler.onBnStop();
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
        if (fingerprintHandler == null)
            fingerprintHandler = new FingerprintHandler(this.getContext());

        if (faceRecognitionHandler == null){
            faceRecognitionHandler = new FaceRecognitionHandler(requireContext(),binding.preview);
        }

        fingerprintHandler.addListener(this);


        fingerprintHandler.onBnStart();
        initSelection();
        initObserver();


        binding.cardFingerprint.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment());
            fingerprintHandler.onBnStart();
        });

        if (binding.cardFingerprint.getVisibility() == View.VISIBLE) {
            fingerprintHandler.onBnStop();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        fingerprintHandler.onBnStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        faceRecognitionHandler.bindCamera(getViewLifecycleOwner());
    }

    @Override
    public void onStop() {
        super.onStop();
        faceRecognitionHandler.unBindCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        faceRecognitionHandler.unBindCamera();
    }

    private void initObserver() {

    }

    private void initSelection() {
        binding.buttonFacial.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(binding.getRoot());

            binding.layoutFacial.setVisibility(View.VISIBLE);
            binding.layoutFingerprint.setVisibility(View.GONE);
            fingerprintHandler.onBnStop();
            faceRecognitionHandler.bindCamera(getViewLifecycleOwner());
        });

        binding.buttonFingerprint.setOnClickListener(v -> {
                    TransitionManager.beginDelayedTransition(binding.getRoot());
                    binding.layoutFacial.setVisibility(View.GONE);
                    binding.layoutFingerprint.setVisibility(View.VISIBLE);
                    fingerprintHandler.onBnStart();
                    faceRecognitionHandler.unBindCamera();
                }

        );

    }

    @Override
    public void onSuccess(byte[] value, String result) {
//        ToastUtils.show(requireContext(),result);
        Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment());
    }

    @Override
    public void onFailed(String message) {
        ToastUtils.show(requireContext(),message);

    }
}


