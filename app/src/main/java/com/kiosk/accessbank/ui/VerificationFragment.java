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
import androidx.biometric.BiometricPrompt;
import androidx.camera.core.Camera;
import androidx.camera.core.Preview;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kiosk.accessbank.camerax.CameraManager;
import com.kiosk.accessbank.databinding.FragmentVerificationBinding;
import com.kiosk.accessbank.viewmodel.MainViewModel;

import java.util.concurrent.Executor;

public class VerificationFragment extends Fragment {

    private static final String FINGERPRINT_TAG = "fingerprint";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private Preview preview;
    private Camera camera;
    private CameraManager cameraManager;
    private FragmentVerificationBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    private MainViewModel viewModel;

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

        createFingerprint();
        createCameraManager();
        initSelection();
        initObserver();
        cameraManager.startCamera();

        binding.buttonCancel.setOnClickListener(v -> {
            viewModel.clearState();
            NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment());
        });
        binding.buttonForceLogin.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "This test using hardcoded dummy data", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(VerificationFragment.this).navigate(VerificationFragmentDirections.actionVerificationFragmentToSelectAccountFragment());
        });

    }

    private void initObserver() {

    }

    private void createCameraManager() {
        cameraManager = new CameraManager(requireContext(), binding.preview, this, binding.graphicOverlayFinder);
    }

    private void initSelection() {
        binding.buttonFacial.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(binding.getRoot());

            binding.layoutFacial.setVisibility(View.VISIBLE);
            binding.layoutFingerprint.setVisibility(View.GONE);
        });

        binding.buttonFingerprint.setOnClickListener(v -> {
                    TransitionManager.beginDelayedTransition(binding.getRoot());
                    binding.layoutFacial.setVisibility(View.GONE);
                    binding.layoutFingerprint.setVisibility(View.VISIBLE);
                }

        );

    }

    private void createFingerprint() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            executor = ContextCompat.getMainExecutor(requireContext());
            biometricPrompt = new BiometricPrompt(requireActivity(), executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(requireContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(requireContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Verification").setSubtitle("Verify your BVN using fingerprint").setNegativeButtonText("Use Face recognition").build();
        }


    }
}


